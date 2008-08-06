import java.io.*;

/**
 * Remote arbiter.
 *
 * Remote arbiter from the point of view of LocalGameController.
 */
class RemoteArbiter implements Arbiter, RemoteArbiterTransport.Parent {
	RemoteArbiterTransport transport;
	GameController gameController;

	public class CreationError extends Exception {
		public CreationError(String s) {
			super(s);
		}
	}

	public Parent parent;

	public RemoteArbiter(Parent parent, int type) throws CreationError {
		this.parent = parent;

		try {
			transport = RemoteArbiterTransportFactory.Create(this, type);
			transport.start();
		} catch (InvalidArgumentException e) {
			throw new CreationError(e.getMessage());
		}
	}

	public void connected(GameController gc) {
		gameController = gc;
	}
	public void disconnected(GameController gc) {
		gameController = null;
	}
	public void handleColor(GameController gc, int color) {
		transport.sendMsg(
				Protocol.HANDLE_COLOR + " "
				+ String.valueOf(color)
				);
	}
	public void unhandleColor(GameController gc, int color) {
		transport.sendMsg(
				Protocol.UNHANDLE_COLOR + " "
				+ String.valueOf(color)
				);
	}
	public void moveRequest(GameController gc, int x, int y) {
		transport.sendMsg(
				Protocol.MOVE_REQUEST + " "
				+ String.valueOf(x) + " "
				+ String.valueOf(y)
				);
	}

	/**
	 * Analyze the message that comes from transport.
	 *
	 * Will be called from different thread.
	 */
	public void receiveMsg(String msg) {
		Tokenizer s = new Tokenizer(msg);
		String cmd = s.next();
		if (cmd == Protocol.PLACE_STONE) {
			String xs = s.next();
			String ys = s.next();
			String cs = s.next();
			if (xs == "" || ys == "" || cs == "") {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int x = Integer.parseInt(xs);
			int y = Integer.parseInt(ys);
			int c = Integer.parseInt(ys);
			gameController.placeStone(x, y, c);
			return;
		}
		protocolFailure("unknown command: " + cmd);
	}

	protected void protocolFailure(String s) {
		// TODO: FIXME
	}

	public void handleTransportConnected(RemoteArbiterTransport t, String s) {
		parent.handleArbiterInitFinished();
		parent.handleArbiterMsg("Connected to: " + s);
		t.sendMsg("INIT");
	}

	public void handleTransportDisconnected(
			RemoteArbiterTransport t,
			String s
			) {
		parent.handleArbiterMsg(s);
		t.stop();
		t = null;
	}

	public void handleTransportInfo(RemoteArbiterTransport t, String s) {
		parent.handleArbiterMsg("Info: " + s);
	}
}

