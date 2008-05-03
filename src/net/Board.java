
import javax.microedition.media.*;
import java.io.*;
import javax.microedition.media.control.*;

/**
 * Internal board representation.
 *
 * Object of this class will remember where stones
 * are placed and other special things about them
 * so that UI is properly redrawn.
 */
class Board {
	public static final int MOVE_UP = 0;
	public static final int MOVE_DOWN = 1;
	public static final int MOVE_LEFT = 2;
	public static final int MOVE_RIGHT = 3;
	public static final int ZOOM_IN = 0;
	public static final int ZOOM_OUT = 1;

	public static final int COLOR_BLACK = 0;
	public static final int COLOR_WHITE = 1;
	public static final int COLOR_NOTHING  = 2;
	public static final int STATE_NORMAL = 0;
	public static final int STATE_LAST = 1;
	public static final int STATE_KO = 2;


	protected BoardView ui;
	protected int boardSize = 19;

	protected int lastMoveX = 0;
	protected int lastMoveY = 0;

	protected int colors[][];

	Board(BoardView ui) {
		setUiBoard(ui);
		clearBoard();
	}

	public void setUiBoard(BoardView ui) {
		this.ui = ui;
		ui.resetBoard(boardSize, ui.getStoneSize());

		colors = new int[boardSize][boardSize];
	}

	public void makeMoveSound() {
		try {
			InputStream is = this.getClass().getResourceAsStream("/move.wav");
			Player p = Manager.createPlayer(is, "audio/x-wav");
			p.realize();
			VolumeControl vc = (VolumeControl) p.getControl("VolumeControl");
			vc.setLevel(100);
			p.start();
		} catch (Exception ex) {
			//System.out.printnln(ex.getMessage());
		}
	}

	void moveCrosshair(int dir) {
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

	void zoomView(int dir) {
		int size = ui.getStoneSize();
		int x = ui.getCrosshairX();
		int y = ui.getCrosshairY();
		switch (dir) {
			case ZOOM_IN:
				size+=2;
				break;
			case ZOOM_OUT:
				size-=2;
				break;
		}
		size = (size / 2);
		size = size * 2 + 1;
		ui.resetBoard(boardSize, size);
		ui.setCrosshairPosition(x, y);
		redrawBoard();
	}

	public void clearBoard() {
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				placeStone(x, y, COLOR_NOTHING);
			}
		}
	}

	public int getStoneState(int x, int y) {
		int state = STATE_NORMAL;

		if (x == lastMoveX && y == lastMoveY) {
			state = STATE_LAST;
		}
		return state;
	}

	public void placeStone(int x, int y, int color) {
		ui.drawStone(x, y, color, getStoneState(x, y));
		colors[x][y] = color;
	}

	public void makeMove(int x, int y, int color) {
		makeMoveSound();
		int lmx = lastMoveX;
		int lmy = lastMoveY;
		lastMoveX = x;
		lastMoveY = y;
		placeStone(lmx, lmy, colors[lmx][lmy]);
		placeStone(x, y, color);
	}

	public void redrawBoard() {
		for (int x = 0; x < boardSize; ++x) {
			for (int y = 0; y < boardSize; ++y) {
				if (colors[x][y] != COLOR_NOTHING) {
					ui.drawStone(x, y, colors[x][y], getStoneState(x, y));
				}
			}
		}
	}

	public int getCrosshairX() {
		return ui.getCrosshairX();
	}

	public int getCrosshairY() {
		return ui.getCrosshairY();
	}
}