import java.util.Vector;

class LocalArbiter implements Arbiter {
	public final int BLACK_PLAYER = 0;
	public final int WHITE_PLAYER = 1;
	GameController playingControllers[] = new GameController[2];

	Vector connectedControllers = new Vector();

	public LocalArbiter() {
	}

	public void connect(GameController gc) {
		connectedControllers.addElement(gc);
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
