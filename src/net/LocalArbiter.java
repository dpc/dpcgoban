import java.util.Vector;

/**
 * Local implementation of arbiter.
 *
 * This class is actual arbiter implementation.
 */
class LocalArbiter
	implements Arbiter,
			   LocalArbiterListener.Parent
{
	public class CreationError extends Exception {
		public CreationError(String s) {
			super(s);
		}
	}

	protected Parent parent;

	GameController blackController;
	GameController whiteController;

	LocalArbiterListener listener;

	Vector connectedControllers = new Vector();

	public LocalArbiter(Parent parent) throws CreationError {
		this.parent = parent;
		try {
			listener = LocalArbiterListenerFactory.Create(
				this, LocalArbiterListener.BLUETOOTH
				);
		} catch (InvalidArgumentException e) {
			throw new CreationError(e.getMessage());
		}
		listener.start();
	}

	public void handleListenerUp() {
		parent.handleArbiterInitFinished();
		parent.handleArbiterMsg("server started");
	}

	public void connected(GameController ngc) {
		appendToControllersList(ngc);
	}

	protected void appendToControllersList(GameController ngc) {
		for (int i = 0; i < connectedControllers.size(); ++i) {
			GameController gc = (GameController)(
				connectedControllers.elementAt(i)
				);
			if (gc == null) {
				connectedControllers.setElementAt(ngc, i);
				return;
			}
		}
		// no nulls, just grow
		connectedControllers.addElement(ngc);
	}

	public void disconnected(GameController gc) {
		for (int i = 0; i < connectedControllers.size(); ++i) {
			if (connectedControllers.elementAt(i) == gc) {
				connectedControllers.setElementAt(gc, i);
			}
		}
	}
	public void handleColor(GameController gc, int color) {
		switch (color) {
			case COLOR_WHITE:
				whiteController = gc;
				break;
			case COLOR_BLACK:
				blackController = gc;
				break;
		}
	}

	public void unhandleColor(GameController gc, int color) {
		switch (color) {
			case COLOR_WHITE:
				if (whiteController == gc) {
					whiteController = null;
				}
				break;
			case COLOR_BLACK:
				if (blackController == gc) {
					blackController = null;
				}
				break;
		}
	}

	public void moveRequest(GameController gcx, int x, int y) {
		for (int i = 0; i < connectedControllers.size(); ++i) {
			GameController gc = (GameController)(
				connectedControllers.elementAt(i)
				);
			if (gc != null) {
				gc.placeStone(x, y, Board.COLOR_WHITE);
			}
		}
	}

	public void handleListenerInfo(LocalArbiterListener l, String msg) {
	}

	public void handleListenerUp(LocalArbiterListener l) {
		parent.handleArbiterInitFinished();
	}

	public void handleListenerDown(LocalArbiterListener l, String msg) {
	}

	public void handleControllerConnected(RemoteGameController c) {
		parent.handleArbiterMsg("new client connection");
	}

	public void handleControllerDisconnected(RemoteGameController c) {
		parent.handleArbiterMsg("client disconnected connection");
	}
}
