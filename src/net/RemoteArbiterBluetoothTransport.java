/**
 * Remote arbiter from local perspective.
 *
 * This class pretends to be arbiter, while just communicating remotely with
 * real LocalArbiter on the other side of the connection.
 */
class RemoteArbiterBluetoothTransport
	implements RemoteArbiterTransport, Runnable {
	Parent parent;
	StreamConnection streamConnection;

	public RemoteArbiterBluetoothTransport(Parent parent) {
		this.parent = parent;
		thread = new Thread(this);
	}

	private boolean closed;

	public void initBluetooth() {
		try {
			// Select the service. Indicate no
			// authentication or encryption is required.
			String connectionURL = 
				discoveryAgent.selectService(CommonBluetooth.MAGIC_UUID, 
						ServiceRecord.NOAUTHENTICATE_NOENCRYPT, 
						false);

			StreamConnection streamConnection
				= (StreamConnection) Connector.open(connectionURL);

		} catch (BluetoothStateException bse) {
			System.out.println("BTMIDlet.btConnect2, 
					exception " + bse);
		} catch (IOException ioe) {
			System.out.println("BTMIDlet.btConnect2, 
					exception " + ioe);
		}
	}

	public void start() {
		thread.start();
		closed = false;
	}
		public void stop() {
		closed = true;
		try {
			thread.join();
		} catch (InterruptedException ie) {}
	}

	public void run() {
		try {
			initListener();
			while (!closed) {
				StreamConnection conn = notifier.acceptAndOpen();
			}
		} catch (IOException e) {
			//XXX:TODO: ?
		}
	}

	public void finishBluetooth() {
		streamConnection.close();
	}

	public void sendMsg(String s) {

			DataOutputStream dataout = 
				streamConnection.openDataOutputStream();
			dataout.writeUTF(s);

	}

	public int type() {
		return BLUETOOTH;
	}
}


