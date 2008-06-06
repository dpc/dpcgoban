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

	public RemoteArbiter(int type) throws CreationError {
		try {
			transport = RemoteArbiterTransportFactory.Create(this, type);
		} catch (InvalidArgumentException e) {
			throw new CreationError(e.getMessage());
		}
	}

	public void connect(GameController gc) {
	}
	public void disconnect(GameController gc) {
	}
	public void handleColor(GameController gc, int color) {
	}
	public void unhandleColor(GameController gc, int color) {
	}
	public void moveRequest(int x, int y) {
	}

	public void receiveMsg(String s) {
	}
}

