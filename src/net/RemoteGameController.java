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
				Protocol.PLACE_STONE + " "
				+ String.valueOf(x) + " "
				+ String.valueOf(y) + " "
				+ String.valueOf(c)
				);
	}

	public void receiveMsg(String msg) {
		Tokenizer s = new Tokenizer(msg);
		String cmd = s.next();
		if (cmd == Protocol.MOVE_REQUEST) {
			String xs = s.next();
			String ys = s.next();
			if (xs == "" || ys == "") {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int x = Integer.parseInt(xs);
			int y = Integer.parseInt(ys);
			arbiter.moveRequest(this, x, y);
			return;
		} else if (
				cmd == Protocol.HANDLE_COLOR
				|| cmd == Protocol.UNHANDLE_COLOR
				)
		{
			String cs = s.next();
			if (cs == "") {
				protocolFailure("no color argument for cmd: " + cmd);
				return;
			}
			int c = Integer.parseInt(cs);
			if (cmd == Protocol.HANDLE_COLOR) {
				arbiter.handleColor(this, c);
			} else {
				arbiter.unhandleColor(this, c);
			}
			return;
		}
		protocolFailure("unknown command: " + cmd);
	}

	protected void protocolFailure(String s) {
		// TODO: FIXME
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
