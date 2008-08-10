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

	public interface Parent {
		public void handleRemoteGameControllerInfo(
				RemoteGameController rgc,
				String str
				);
	}

	private Parent parent;

	public RemoteGameController(Parent parent)
		throws IOException {
		this.parent = parent;
	}

	public void registerChildTransport(
			RemoteGameControllerTransport transport
			) {
		this.transport = transport;
	}


	public void connectedTo(Arbiter arbiter) {
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

	public void clearBoard() {
		transport.sendMsg(
				Protocol.CLEAR_BOARD
				);
	}

	public void gameInfo(String s) {
		transport.sendMsg(
				Protocol.GAME_INFO + " " +
				s
				);
	}


	public void receiveMsg(String msg) {
		if (arbiter == null) {
			parent.handleRemoteGameControllerInfo(
					this,
					"but the arbiter is null!"
					);
		}

		Tokenizer s = new Tokenizer(msg);
		String cmd = s.next();
		if (cmd.equals(Protocol.MOVE_REQUEST)) {
			String xs = s.next();
			String ys = s.next();
			if (xs.equals("") || ys.equals("")) {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int x = Integer.parseInt(xs);
			int y = Integer.parseInt(ys);
			arbiter.moveRequest(this, x, y);
			return;
		} else if (
				(cmd.equals(Protocol.HANDLE_COLOR))
				|| (cmd.equals(Protocol.UNHANDLE_COLOR))
				)
		{
			String cs = s.next();
			if (cs.equals("")) {
				protocolFailure("no color argument for cmd: " + cmd);
				return;
			}
			int c = Integer.parseInt(cs);
			if (cmd.equals(Protocol.HANDLE_COLOR)) {
				arbiter.handleColor(this, c);
			} else {
				arbiter.unhandleColor(this, c);
			}
			return;
		}
		protocolFailure("unknown command: '" + cmd + "'");
	}

	protected void protocolFailure(String s) {
		// TODO: FIXME
		parent.handleRemoteGameControllerInfo(
				this,
				"GC Transport: protocol failure: " + s
				);
	}

	public void handleTransportDisconnected(
			RemoteGameControllerTransport t, String s
			) {

		parent.handleRemoteGameControllerInfo(
				this,
				"GC Transport: disconnected: " + s
				);
	}

	/**
	 * Called after connection was estabilished.
	 */
	public void handleTransportConnected(
			RemoteGameControllerTransport t, String s
			) {
		parent.handleRemoteGameControllerInfo(
				this,
				"GC Transport: connected: " + s
				);
	}

	public void handleTransportInfo(RemoteGameControllerTransport t, String s) {
		parent.handleRemoteGameControllerInfo(
				this,
				"GC Transport: " + s
				);
	}

	public String name() {
		return "remote";
	}
}
