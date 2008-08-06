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
	extends CommonTransportHandler
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
			in =
				streamConnection.openDataInputStream();
			while (!closed) {
				// use the connection
				receive();
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
			StreamConnection stream
				= (StreamConnection) Connector.open(
						connectionURL,
						Connector.READ_WRITE,
						true
						);

		in = stream.openDataInputStream();
		out = stream.openDataOutputStream();

		} catch (BluetoothStateException bse) {
			throw new IOException(
					"BTMIDlet.btConnect2:" + bse
					);
		}
	}

	protected void finishBluetooth()
		throws IOException
	{
	}

	public void sendMsg(String s)
	{
		send(s);
	}

	public int type() {
		return BLUETOOTH;
	}

	public void protocolFailure(String str) {
		//TODO: implement me
		stop();
	}

	public void handleIncomingCommand(String cmd) {
		//TODO: implement me
	}
}


