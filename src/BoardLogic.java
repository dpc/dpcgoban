class BoardLogic {

	static final int COLOR_NONE = Board.COLOR_NOTHING;
	static final int COLOR_WHITE = Board.COLOR_WHITE;
	static final int COLOR_BLACK = Board.COLOR_BLACK;

	static final int STATE_NORMAL = BoardLogic.STATE_NORMAL;

	interface Parent {
		void handleBoardStoneChange (
				BoardLogic bl, int x, int y, int color, int status
				);
		void handleBoardResize(
				BoardLogic bl, int new_size
				);
		void handleBoardClear(
				BoardLogic bl
				);
	}

	int colors[][];
	int states[][];

	private Parent parent;

	public BoardLogic(Parent parent) {
		this.parent = parent;
		resize(19);
	}

	public void resize(int size) {
		colors = new int[size][size];
		states = new int[size][size];
		parent.handleBoardResize(this, 19);
		clear();
	}

	public void clear() {
		for (int x = 0; x < getSize(); ++x) {
			for (int y = 0; y < getSize(); ++y) {
				colors[x][y] = COLOR_NONE;
				states[x][y] = STATE_NORMAL;
			}
		}
		parent.handleBoardClear(this);
	}


	public int getColor(int x, int y) {
		return colors[x][y];
	}

	public int getState(int x, int y) {
		return states[x][y];
	}

	public int getSize() {
		return colors.length;
	}

	public void moveRequest(int x, int y, int color)
	throws InvalidArgumentException {
		if (getColor(x, y) != COLOR_NONE) {
			throw new InvalidArgumentException("already occupied");
		}

		colors[x][y] = color;
		states[x][y] = STATE_NORMAL;
		parent.handleBoardStoneChange(this, x, y, color, STATE_NORMAL);
	}

}

