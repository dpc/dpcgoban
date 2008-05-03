class LocalArbiter implements Arbiter {
	GameController gameController;

	public LocalArbiter() {
	}

	public void connect(GameController gc) {
		gameController = gc;
	}

	public void moveRequest(int x, int y) {
		gameController.placeStone(x, y, Board.COLOR_WHITE);
	}
}
