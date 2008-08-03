import java.io.*;

/**
 * Remote arbiter.
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
	public void moveRequest(int x, int y) {
	}

	public void receiveMsg(String s) {
	}

	public void handleTransportConnected(RemoteArbiterTransport t, String s) {
		parent.handleArbiterInitFinished();
		parent.handleArbiterMsg("Connected to: " + s);
		try {
			t.sendMsg("INIT");
		} catch (IOException e) {
			parent.handleArbiterMsg("IO ERROR");
		}
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

