
/**
 * Go board logic.
 *
 * I'm not very fond of it, but it seems to do its job.
 *
 * TODO: ko
 */
class BoardLogic {

	static final int COLOR_NONE = Board.COLOR_NOTHING;
	static final int COLOR_WHITE = Board.COLOR_WHITE;
	static final int COLOR_BLACK = Board.COLOR_BLACK;

	static final int STATE_NORMAL = Board.STATE_NORMAL;
	static final int STATE_KO = Board.STATE_KO;
	static final int STATE_LAST = Board.STATE_LAST;

	/**
	 * BoardLogic's callbacks.
	 */
	interface Parent {
		void handleBoardStoneChange (
				BoardLogic bl, int x, int y, int color, int status
				);
		void handleMoveCommited(int x, int y, int c);
		void handlePassCommited(int c);

		void handleBoardResize(
				BoardLogic bl, int new_size
				);
		void handleBoardClear(
				BoardLogic bl
				);
		void handleCaptureCounterChange(
				BoardLogic bl,
				int b, int c
				);
	}

	int koX = -1;
	int koY = -1;
	int lastX = -1;
	int lastY = -1;
	int lastColor = COLOR_BLACK;

	int whiteCaptured = 0;
	int blackCaptured = 0;

	private Group groups[][];

	private Parent parent;

	public BoardLogic(Parent parent) {
		this.parent = parent;
		resize(19);
	}

	public void resize(int size) {
		groups = new Group[size][size];
		parent.handleBoardResize(this, 19);
		clear();
	}

	public void clear() {
		for (int x = 0; x < getSize(); ++x) {
			for (int y = 0; y < getSize(); ++y) {
				groups[x][y] = null; //is already like it, but whatever...
			}
		}
		parent.handleBoardClear(this);
		koX = -1;
		koY = -1;
		lastX = -1;
		lastY = -1;
	}


	public int getColor(int x, int y) {
		if (groups[x][y] == null) {
			return COLOR_NONE;
		}
		return groups[x][y].color;
	}

	
	public int getState(int x, int y) {
		if (x == koX && y == koY) {
			return STATE_KO;
		}
		if (x == lastX && y == lastX) {
			return STATE_LAST;
		}
		return STATE_NORMAL;
	}


	public int getSize() {
		return groups.length;
	}

	public void passRequest(int color)
		throws InvalidArgumentException {

		clearKo();
		clearLast();

		parent.handlePassCommited(color);
	}

	public void moveRequest(int x, int y, int color)
		throws InvalidArgumentException {
		if (getColor(x, y) != COLOR_NONE) {
			throw new InvalidArgumentException("already occupied");
		}

		if (x == koX && y == koY) {
			throw new InvalidArgumentException("ko");
		}

		if (checkSuicide(x, y, color)) {
			throw new InvalidArgumentException("suicidal");
		}

		doMove(x, y, color);
	}

	 boolean isOtherColor(int x, int y, int color) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}
		if (groups[x][y] == null) {
			return false;
		}

		if (groups[x][y].color != color) {
			return true;
		}

		return false;
	}

	 boolean isSameColor(int x, int y, int color) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}
		if (groups[x][y] == null) {
			return false;
		}

		if (groups[x][y].color == color) {
			return true;
		}

		return false;
	}
	protected void clearKo() {
		if (koX != -1 && koY != -1) {
			parent.handleBoardStoneChange(this, koX, koY, COLOR_NONE, STATE_NORMAL);
			koX = -1;
			koY = -1;
		}
	}
	protected void clearLast() {
		if (lastX != -1 && lastY != -1) {
			parent.handleBoardStoneChange(
					this, lastX, lastY, lastColor, STATE_NORMAL
					);
			lastX = -1;
			lastY = -1;
		}
	}
	protected void setKo(int x, int y) {
		koX = x;
		koY = y;
		parent.handleBoardStoneChange(this, koX, koY, COLOR_NONE, STATE_KO);
	}

	protected void setLast(int x, int y, int c) {
		lastX = x;
		lastY = y;
		lastColor = c;
		parent.handleBoardStoneChange(this, x, y, c, STATE_LAST);
	}

	public void doMove(int x, int y, int color) {
		groups[x][y] = new Group();
		groups[x][y].color = color;
		setLast(x, y, color);
		parent.handleMoveCommited(x, y, color);

		// TODO: this part is lame
		// recounting liberties each time
		// sucks, but just decrasing it
		// int takeOneLiberty is vulearable
		// to double liberty taking problem
		if (isOtherColor(x, y-1, color)) {
			takeOneLiberty(x, y-1);
		}
		if (isOtherColor(x, y+1, color)) {
			takeOneLiberty(x, y+1);
		}
		if (isOtherColor(x-1, y, color)) {
			takeOneLiberty(x-1, y);
		}
		if (isOtherColor(x+1, y, color)) {
			takeOneLiberty(x+1, y);
		}

		if (isSameColor(x, y-1, color)) {
			joinGroups(x, y, x, y-1);
		}
		if (isSameColor(x, y+1, color)) {
			joinGroups(x, y, x, y+1);
		}
		if (isSameColor(x+1, y, color)) {
			joinGroups(x, y, x+1, y);
		}
		if (isSameColor(x-1, y, color)) {
			joinGroups(x, y, x-1, y);
		}

		recountLiberties(x, y);
	}

	void takeOneLiberty(int x, int y) {
		recountLiberties(x, y);
		if (groups[x][y].liberties == 0) {
			removeGroup(x, y);
		}
	}

	protected void removeGroup(int x, int y) {
		Group toDel = groups[x][y];
		int c = groups[x][y].color;
		for (int nx = 0; nx < getSize(); ++nx) {
			for (int ny = 0; ny < getSize(); ++ny) {
				if (groups[nx][ny] == toDel) {
					groups[nx][ny] = null;
					switch(c) {
						case COLOR_WHITE:
							blackCaptured++;
							break;
						case COLOR_BLACK:
							whiteCaptured++;
							break;
					}
					parent.handleBoardStoneChange(
							this, nx, ny, COLOR_NONE, getState(nx, ny)
							);
					if (isOtherColor(nx - 1, ny, c)) {
						groups[nx - 1][ny].needsRecount = true;
					}
					if (isOtherColor(nx + 1, ny, c)) {
						groups[nx + 1][ny].needsRecount = true;
					}
					if (isOtherColor(nx , ny -1, c)) {
						groups[nx][ny -1].needsRecount = true;
					}
					if (isOtherColor(nx, ny + 1, c)) {
						groups[nx][ny + 1].needsRecount = true;
					}
				}
			}
		}
		recountMarkedLiberties();
		parent.handleCaptureCounterChange(this, blackCaptured, whiteCaptured);
	}

	protected void recountMarkedLiberties() {
		for (int nx = 0; nx < getSize(); ++nx) {
			for (int ny = 0; ny < getSize(); ++ny) {
				if (groups[nx][ny] != null) {
					if (groups[nx][ny].needsRecount) {
						groups[nx][ny].needsRecount = false;
						recountLiberties(nx, ny);
					}
				}
			}
		}
	}

	void recountLiberties(int x, int y) {
		Group current = groups[x][y];
		int liberties = 0;

		for (int nx = 0; nx < getSize(); ++nx) {
			for (int ny = 0; ny < getSize(); ++ny) {
				if (isLibertyOf(nx, ny, current)) {
					liberties++;
				}
			}
		}
		current.liberties = liberties;
	}

	boolean isLibertyOf(int x, int y, Group current) {
		if (!isEmpty(x, y)) {
			return false;
		}
		if (isGroup(x-1, y, current)) { return true; }
		if (isGroup(x+1, y, current)) { return true; }
		if (isGroup(x, y-1, current)) { return true; }
		if (isGroup(x, y+1, current)) { return true; }
		return false;
	}

	boolean isGroup(int x, int y, Group current) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}
		if (groups[x][y] == current) {
			return true;
		}
		return false;
	}

	void joinGroups(int x, int y, int jx, int jy) {
		Group toJoin = groups[jx][jy];
		for (int nx = 0; nx < getSize(); ++nx) {
			for (int ny = 0; ny < getSize(); ++ny) {
				if (groups[nx][ny] == toJoin) {
					groups[nx][ny] = groups[x][y];
				}
			}
		}
	}
	
	public boolean checkSuicide(int x, int y, int color) {
		if (
			isEmpty(x, y-1) || isEmpty(x, y+1) ||
			isEmpty(x-1, y) || isEmpty(x+1, y)
			)
		{
			commitMove(x, y, color);
			return false;
		}

		if (
				isSameColorNotInAtari(x, y-1, color) ||
				isSameColorNotInAtari(x, y+1, color) ||
				isSameColorNotInAtari(x-1, y, color) ||
				isSameColorNotInAtari(x+1, y, color)
		   )
		{
			commitMove(x, y, color);
			return false;
		}

		int groupsToKill = 0;
		int oneStoneGroupsToKill = 0;
		int oneStoneGroupsToKillX = -1;
		int oneStoneGroupsToKillY = -1;

		if (isOtherColorInAtari(x, y-1, color)) {
			groupsToKill++;
			if (isSingleStone(x, y-1)) {
				oneStoneGroupsToKillX = x;
				oneStoneGroupsToKillY = y-1;
				oneStoneGroupsToKill++;
			}
		}
		if (isOtherColorInAtari(x, y+1, color)) {
			groupsToKill++;
			if (isSingleStone(x, y+1)) {
				oneStoneGroupsToKillX = x;
				oneStoneGroupsToKillY = y+1;
				oneStoneGroupsToKill++;
			}
		}
		if (isOtherColorInAtari(x-1, y, color)) {
			groupsToKill++;
			if (isSingleStone(x-1, y)) {
				oneStoneGroupsToKillX = x-1;
				oneStoneGroupsToKillY = y;
				oneStoneGroupsToKill++;
			}
		}
		if (isOtherColorInAtari(x+1, y, color)) {
			groupsToKill++;
			if (isSingleStone(x+1, y)) {
				oneStoneGroupsToKillX = x+1;
				oneStoneGroupsToKillY = y;
				oneStoneGroupsToKill++;
			}
		}

		if (groupsToKill > 0) {
			commitMove(x, y, color);
		}

		if (oneStoneGroupsToKill == 1) {
			setKo(oneStoneGroupsToKillX, oneStoneGroupsToKillY);
		}

		if (groupsToKill > 0) {
			return false;
		}

		return true;
	}

	protected boolean isSingleStone(int x, int y) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}

		if (groups[x][y] == null) {
			return false;
		}
		int c = groups[x][y].color;

		if (isSameColor(x - 1, y, c)) {
			return false;
		}
		if (isSameColor(x + 1, y, c)) {
			return false;
		}
		if (isSameColor(x, y - 1, c)) {
			return false;
		}
		if (isSameColor(x, y + 1, c)) {
			return false;
		}
		return true;
	}


	/**
	 * Triggered as soon as the move is considered valid.
	 */
	protected void commitMove(int x, int y, int c) {
		clearKo();
		clearLast();
	}

	public boolean isEmpty(int x, int y) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}

		if (groups[x][y] == null) {
			return true;
		}

		return false;
	}

	public boolean isSameColorNotInAtari(int x, int y, int color) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}

		if (groups[x][y] == null) {
			return false;
		}

		if (groups[x][y].color == color &&
			groups[x][y].liberties > 1)
		{
			return true;
		}

		return false;
	}

	public boolean isOtherColorInAtari(int x, int y, int color) {
		if (isOutOfTheBoard(x, y)) {
			return false;
		}

		if (groups[x][y] == null) {
			return false;
		}

		if ((groups[x][y].color != color) &&
			(groups[x][y].liberties == 1))
		{
			return true;
		}

		return false;
	}

	public boolean isOutOfTheBoard(int x, int y) {
		if (x < 0 || y < 0) {
			return true;
		}

		if (x >= groups.length || y >= groups.length) {
			return true;
		}

		return false;
	}

	/**
	 * Set of stonest of same colors connected (being alive) together.
	 */
	protected class Group {
		public int liberties;
		public int color;
		public boolean needsRecount;
	}
}
