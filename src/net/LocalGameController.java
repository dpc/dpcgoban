

class LocalGameController implements GameController {

	Arbiter arbiter;
	Board board;
	LogView log;

	public LocalGameController(Board board, LogView log) {
		this.board = board;
		this.log = log;
	}

	public void connectedTo(Arbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void moveRequest() {
		arbiter.moveRequest(this, board.getCrosshairX(), board.getCrosshairY());
	}

	public void placeStone(int x, int y, int c) {
		board.placeStone(x, y, c);
	}

	public void clearBoard() {
		board.clearBoard();
	}

	public void gameInfo(String s) {
		log.appendString("GAME: " + s);
	}

	public void handleColor(int color) {
		arbiter.handleColor(this, color);
	}

	public void unhandleColor(int color) {
		arbiter.unhandleColor(this, color);
	}

	public String name() {
		return "local";
	}

}

