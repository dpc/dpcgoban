

class LocalGameController implements GameController {

	Arbiter arbiter;
	Board board;

	public LocalGameController(Board board) {
		this.board = board;
		this.arbiter = new LocalArbiter();
		arbiter.connect(this);
	}

	public void moveRequest() {
		arbiter.moveRequest(board.getCrosshairX(), board.getCrosshairY());
	}

	public void placeStone(int x, int y, int c) {
		board.placeStone(x, y, c);
	}
}

