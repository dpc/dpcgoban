import java.util.Vector;

/**
 * Local interface to arbiter.
 *
 * This class is actual arbiter implementation.
 */
class LocalArbiter implements Arbiter {
	GameController blackController;
	GameController whiteController;

	LocalArbiterListener listener;

	Vector connectedControllers = new Vector();

	public LocalArbiter() {
		try {
		listener = LocalArbiterListenerFactory.Create(this, LocalArbiterListener.BLUETOOTH);
		} catch (InvalidArgumentException e) {
			//XXX: TODO: what to do?
		}
		listener.start();
	}

	public void connect(GameController ngc) {

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

	public void disconnect(GameController gc) {
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
	public void moveRequest(int x, int y) {
		for (int i = 0; i < connectedControllers.size(); ++i) {
			GameController gc = (GameController)(
				connectedControllers.elementAt(i)
				);
			if (gc != null) {
				gc.placeStone(x, y, Board.COLOR_WHITE);
			}
		}
	}
}
