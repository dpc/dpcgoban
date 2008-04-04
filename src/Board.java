

class Board {
	public static final int MOVE_UP = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_LEFT = 2;
	public static final int MOVE_RIGHT = 3;
	public static final int ZOOM_IN = 0;
	public static final int ZOOM_OUT = 1;

	protected BoardUI ui;
	protected int boardSize = 19;

	Board(BoardUI ui) {
		this.ui = ui;

		ui.resetBoard(boardSize, ui.getStoneSize());
	}

	void move(int dir) {
		int x = ui.getCrosshairX();
		int y = ui.getCrosshairY();
		switch (dir) {
			case MOVE_UP:
				y--;
				break;
			case MOVE_DOWN:
				y++;
				break;
			case MOVE_LEFT:
				x--;
				break;
			case MOVE_RIGHT:
				x++;
				break;
		}
		ui.setCrosshairPosition(x, y);
	}

	void zoom(int dir) {
		int size = ui.getStoneSize();
		int x = ui.getCrosshairX();
		int y = ui.getCrosshairY();
		switch (dir) {
			case ZOOM_IN:
				size++;
				break;
			case ZOOM_OUT:
				size--;
				break;
		}
		ui.resetBoard(boardSize, size);
		ui.setCrosshairPosition(x, y);
		redrawBoard();
	}

	public void redrawBoard() {
	}

}
