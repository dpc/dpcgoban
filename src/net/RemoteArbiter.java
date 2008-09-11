//import java.io.*;
import java.lang.System;

/**
 * Remote arbiter.
 *
 * Remote arbiter from the point of view of LocalGameController.
 */
class RemoteArbiter implements Arbiter, RemoteArbiterTransport.Parent {
	RemoteArbiterTransport transport;
	LocalGameController gameController;

	public class CreationError extends Exception {
		public CreationError(String s) {
			super(s);
		}
	}

	private long lastPong;
	private long lastPing;

	public Parent parent;

	public RemoteArbiter(Parent parent, int type) throws CreationError {
		this.parent = parent;

		try {
			transport = RemoteArbiterTransportFactory.Create(this, type);
		} catch (InvalidArgumentException e) {
			throw new CreationError(e.getMessage());
		}
		// set last pong time to 60 sec. in the future
		// to allo some initial (connecting) delay
		touchLastPong();
		lastPong += 1000 * 60;

		touchLastPing();
		transport.start();
	}

	protected void touchLastPong() {
		lastPong = System.currentTimeMillis();
	}

	protected void touchLastPing() {
		lastPing = System.currentTimeMillis();
	}

	public boolean isLastPongValid() {
		return ((lastPing - lastPong) < 1000 * 3);
	}

	public boolean shouldSentNewPing() {
		return (System.currentTimeMillis() > (lastPing + 1000 * 2));
	}

	public void sentNewPing() {
		transport.sendMsg(Protocol.PING);
		touchLastPing();
	}

	public void shutdown() {
		parent.handleArbiterMsg("hiere");
		if (gameController != null) {
		parent.handleArbiterMsg("shuting down game controller");
			gameController.shutdown();
		}
		if (transport != null) {
			transport.stop();
			transport = null;
		}
	}

	public void connected(LocalGameController gc) {
		gameController = gc;
		gc.connectedTo(this);
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

	public void passRequest(GameController gc) {
		transport.sendMsg(
				Protocol.PASS_REQUEST
				);
	}

	/**
	 * Analyze the message that comes from transport.
	 *
	 * Will be called from different thread.
	 */
	public void receiveMsg(String msg) {
		if (this.gameController == null) {
			protocolFailure("null gameController on receive");
			return;
		}
		Tokenizer s = new Tokenizer(msg);
		String cmd = s.next();
		if (cmd.equals(Protocol.PLACE_STONE)) {
			String xs = s.next();
			String ys = s.next();
			String cs = s.next();
			String ss = s.next();
			if (xs.equals("") || ys.equals("") || cs.equals("") || ss.equals("")) {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int x = Integer.parseInt(xs);
			int y = Integer.parseInt(ys);
			int c = Integer.parseInt(cs);
			int sss = Integer.parseInt(ss);
			gameController.placeStone(x, y, c, sss);
			return;
		} else if (cmd.equals(Protocol.MOVE)) {
			String xs = s.next();
			String ys = s.next();
			String cs = s.next();
			if (xs.equals("") || ys.equals("") || cs.equals("")) {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int x = Integer.parseInt(xs);
			int y = Integer.parseInt(ys);
			int c = Integer.parseInt(cs);
			gameController.move(x, y, c);
			return;
		} else if (cmd.equals(Protocol.CLEAR_BOARD)) {
			gameController.clearBoard();
			return;
		} else if (cmd.equals(Protocol.GAME_INFO)) {
			gameController.gameInfo(s.rest());
			return;
		} else if (cmd.equals(Protocol.CAPTURE)) {
			String bs = s.next();
			String ws = s.next();
			if (bs.equals("") || ws.equals("")) {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int b = Integer.parseInt(bs);
			int w = Integer.parseInt(ws);
			gameController.updateCaptures(b, w);
			return;
		} else if (cmd.equals(Protocol.PASS)) {
			String cs = s.next();
			if (cs.equals("")) {
				protocolFailure("no enought arguments for cmd: " + cmd);
				return;
			}
			int c = Integer.parseInt(cs);
			gameController.pass(c);
			return;
		} else if (cmd.equals(Protocol.PONG)) {
			touchLastPong();
			return;
		}
		protocolFailure("unknown command: '" + cmd + "'");
	}

	protected void protocolFailure(String s) {
		// TODO: FIXME
		parent.handleArbiterMsg("Protocol failure: " + s);
	}

	public void handleTransportConnected(RemoteArbiterTransport t, String s) {
		parent.handleArbiterInitFinished();
		parent.handleArbiterMsg("Connected to: " + s);
	}

	public void handleTransportDisconnected(
			RemoteArbiterTransport t,
			String s
			) {
		shutdown();
		parent.handleArbiterMsg(s);
	}

	public void handleTransportInfo(RemoteArbiterTransport t, String s) {
		parent.handleArbiterMsg("Info: " + s);
	}
}

