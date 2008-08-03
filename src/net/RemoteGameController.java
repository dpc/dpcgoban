import javax.microedition.io.StreamConnection;
import java.io.IOException;

/**
 * Remote controller from the point of view
 * of local arbiter.
 */
class RemoteGameController
	implements GameController,
			   RemoteGameControllerTransport.Parent {

	protected Arbiter arbiter;
	protected RemoteGameControllerTransport transport;

	public RemoteGameController()
		throws IOException {
	}

	public void registerChildTransport(
			RemoteGameControllerTransport transport
			) {
		this.transport = transport;
	}


	public void connected(Arbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void placeStone(int x, int y, int c) {
		transport.sendMsg(
				"stone "
				+ String.valueOf(x) + " "
				+ String.valueOf(y) + " "
				+ String.valueOf(c)
				);
	}

	public void receiveMsg(String msg) {
		int first_break = msg.indexOf(' ');
		if (first_break == -1) {
			// without arguments.
			// TODO: implement
			return;
		}
		String cmd = msg.substring(0, first_break);
		if (cmd == "anything") {
			//TODO: implement
		}
		return;
	}

	public void handleTransportDisconnected(
			RemoteGameControllerTransport t, String s
			) {

	}

	/**
	 * Called after connection was estabilished.
	 */
	public void handleTransportConnected(
			RemoteGameControllerTransport t, String s
			) {
	}

	public void handleTransportInfo(RemoteGameControllerTransport t, String s) {
	}

	public String name() {
		return "remote";
	}
}
