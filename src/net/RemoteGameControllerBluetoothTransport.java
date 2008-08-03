import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

class RemoteGameControllerBluetoothTransport
	extends CommonTransportHandler
	implements RemoteGameControllerTransport {

	protected boolean alive = true;

	protected StreamConnection stream;
	protected Parent parent;

	RemoteGameControllerBluetoothTransport(
			Parent parent,
			StreamConnection stream
			)
		throws IOException {

		this.stream = stream;
		in = stream.openDataInputStream();
		out = stream.openDataOutputStream();

		this.parent = parent;
		parent.registerChildTransport(this);
	}

	/**
	 * Protocol failure handling.
	 */
	protected void protocolFailure(String msg) {
		alive = false;
		//TODO: any msg?
	}

	/**
	 * Handle stream connection.
	 *
	 * Should be called by an owner
	 * frequently to allow object
	 * to handle new arriving data for that
	 * connection.
	 */
	public void poll() throws IOException {
		receive();
	}

	protected void handleIncomingCommand(String str) {
		parent.receiveMsg(str);
	}

	public void sendMsg(String str) {
		send(str);
	}

	public int type() {
		return BLUETOOTH;
	}
}
