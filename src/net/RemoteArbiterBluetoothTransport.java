import javax.microedition.io.StreamConnection;
import java.lang.Thread;
import javax.bluetooth.*;
import java.io.*;
import javax.microedition.io.Connector;

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
	Thread thread;
	DiscoveryAgent discoveryAgent;

	public RemoteArbiterBluetoothTransport(Parent parent) {
		this.parent = parent;
		parent.handleTransportInfo(this, "starting thread...");
		thread = new Thread(this);
	}

	private boolean closed;

	public void start() {
		closed = false;
		thread.start();
	}

	public void stop() {
		closed = true;
		try {
			thread.join();
		} catch (InterruptedException ie) {}
	}

	public void run() {
		try {
			initBluetooth();
			parent.handleTransportConnected(
					this,
					"unknown server"
					);
			DataInputStream datain =
				streamConnection.openDataInputStream();
			while (!closed) {
				// use the connection
				byte buf[] = new byte[100];
				datain.read(buf);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
			finishBluetooth();
		} catch (IOException e) {
			parent.handleTransportDisconnected(
					this,
					e.getMessage()
					);
			return;
		}
		parent.handleTransportDisconnected(
					this,
					"closed normally"
					);
	}

	protected void initBluetooth()
		throws IOException
	{
		try {
			// Select the service. Indicate no
			// authentication or encryption is required.
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			discoveryAgent = localDevice.getDiscoveryAgent();
			parent.handleTransportInfo(this, "selecting service...");
			String connectionURL =
				discoveryAgent.selectService(
						new UUID(CommonBluetooth.MAGIC_UUID, false),
						ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
						false
						);

			parent.handleTransportInfo(this, "opening connection...");
			StreamConnection streamConnection
				= (StreamConnection) Connector.open(
						connectionURL,
						Connector.READ_WRITE,
						true
						);

		} catch (BluetoothStateException bse) {
			throw new IOException(
					"BTMIDlet.btConnect2:" + bse
					);
		}
	}

	protected void finishBluetooth()
		throws IOException
	{
		streamConnection.close();
	}

	public void sendMsg(String s)
		throws IOException
	{
		DataOutputStream dataout =
			streamConnection.openDataOutputStream();
		dataout.writeUTF(s);
	}

	public int type() {
		return BLUETOOTH;
	}
}


