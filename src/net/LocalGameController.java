

class LocalGameController implements GameController {

	Arbiter arbiter;
	Board board;

	public LocalGameController(Board board) {
		this.board = board;
	}

	public void connected(Arbiter arbiter) {
		this.arbiter = arbiter;
	}

	public void moveRequest() {
		arbiter.moveRequest(this, board.getCrosshairX(), board.getCrosshairY());
	}

	public void placeStone(int x, int y, int c) {
		board.placeStone(x, y, c);
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

