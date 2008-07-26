

class LocalGameController implements GameController {

	Arbiter arbiter;
	Board board;

	public LocalGameController(Board board) {
		this.board = board;
	}

	public void connect(Arbiter arbiter) {
		this.arbiter = arbiter;
		arbiter.connect(this);
	}

	public void moveRequest() {
		arbiter.moveRequest(board.getCrosshairX(), board.getCrosshairY());
	}

	public void placeStone(int x, int y, int c) {
		board.placeStone(x, y, c);
	}

	public String name() {
		return "local";
	}

}

