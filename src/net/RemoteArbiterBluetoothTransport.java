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
		thread = new Thread(this);
	}

	private boolean closed;

	public void initBluetooth() {
		try {
			// Select the service. Indicate no
			// authentication or encryption is required.
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			discoveryAgent = localDevice.getDiscoveryAgent();
			String connectionURL =
				discoveryAgent.selectService(
						new UUID(CommonBluetooth.MAGIC_UUID, false),
						ServiceRecord.NOAUTHENTICATE_NOENCRYPT,
						false
						);

			StreamConnection streamConnection
				= (StreamConnection) Connector.open(connectionURL);

		} catch (BluetoothStateException bse) {
			System.out.println("BTMIDlet.btConnect2, " +
					"exception " + bse);
		} catch (IOException ioe) {
			System.out.println("BTMIDlet.btConnect2, " +
					"exception " + ioe);
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
			initBluetooth();
			DataInputStream datain =
				streamConnection.openDataInputStream();
			while (!closed) {
				// use the connection
				byte buf[] = new byte[100];
				datain.read(buf);
			}
		} catch (IOException e) {
			//XXX:TODO: ?
		}
	}

	public void finishBluetooth() {
		try {
			streamConnection.close();
		} catch (IOException e) {
			//XXX:TODO: ?
		}
	}

	public void sendMsg(String s) {
		try {
		DataOutputStream dataout =
			streamConnection.openDataOutputStream();
		dataout.writeUTF(s);
		} catch (IOException e) {
			//XXX:TODO: ?
		}
	}

	public int type() {
		return BLUETOOTH;
	}
}


