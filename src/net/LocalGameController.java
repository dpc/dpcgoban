

class LocalGameController implements GameController {

	Arbiter arbiter;
	Board board;
	LogView log;

	public LocalGameController(Board board, LogView log) {
		this.board = board;
		this.log = log;
	}

	public void connectedTo(LocalArbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void connectedTo(RemoteArbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void moveRequest() {
		if (arbiter != null) {
			arbiter.moveRequest(this, board.getCrosshairX(), board.getCrosshairY());
		}
	}

	public void placeStone(int x, int y, int c, int s) {
		board.placeStone(x, y, c, s);
	}

	public void clearBoard() {
		board.clearBoard();
	}

	public boolean isActive() {
		return arbiter != null;
	}

	public void shutdown() {
		arbiter = null;
	}

	public void gameInfo(String s) {
		log.appendString("GAME: " + s);
	}

	public void handleColor(int color) {
		if (arbiter != null) {
			arbiter.handleColor(this, color);
		}
	}

	public void move(int x, int y, int c) {
		board.move(x, y, c);
	}

	public void unhandleColor(int color) {
		if (arbiter != null) {
			arbiter.unhandleColor(this, color);
		}
	}

	public String name() {
		return "local";
	}

}

