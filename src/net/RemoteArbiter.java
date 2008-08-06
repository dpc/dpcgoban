import java.io.*;

/**
 * Remote arbiter.
 *
 * Remote arbiter from the point of view of LocalGameController.
 */
class RemoteArbiter implements Arbiter, RemoteArbiterTransport.Parent {
	RemoteArbiterTransport transport;

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
	}
	public void disconnected(GameController gc) {
	}
	public void handleColor(GameController gc, int color) {
	}
	public void unhandleColor(GameController gc, int color) {
	}
	public void moveRequest(GameController gc, int x, int y) {
		transport.sendMsg(
				"move "
				+ String.valueOf(x) + " "
				+ String.valueOf(y)
				);
	}

	/**
	 * Analyze the message that comes from transport.
	 *
	 * Will be called from different thread.
	 */
	public void receiveMsg(String s) {

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

