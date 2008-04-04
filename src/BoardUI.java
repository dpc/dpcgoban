import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.lang.Long;
import java.lang.Math;

class BoardUI extends UIElementCommon {

	Image boardImage = null;
	/**
	 * Stone size in pixels.
	 */
	int stoneSize;
	long lastTime = 0;
	long firstTime = 0;

	/**
	 * Board size in stones.
	 */
	int boardSize;

	int boardColor = 0x00FFFFF;
	int lineColor = 0x0000000;
	int backgroundColor= 0x000D000;

	/**
	 * Croshair X in stones.
	 */
	int cx = 0;

	/**
	 * Croshair Y in stones.
	 */
	int cy = 0;


	/**
	 * Board offset on the screen - X;
	 */
	int sx;

	/**
	 * Board offset on the screen - Y;
	 */
	int sy;

	/**
	 * Board background visible.
	 */
	boolean backgroundVisible;


	/**
	 * Ctor.
	 */
	public Board(Parent parent) {
		super(parent);

		firstTime = System.currentTimeMillis();
	}

	public void resetBoard(int boardSize, int stoneSize) {
		setBoardSize(boardSize);
		setStoneSize(stoneSize);

		recreateBoardImage();
		drawEmptyBoard();
	}


	protected void drawEmptyBoard() {
		Graphics g = boardImage.getGraphics();
		g.setColor(boardColor);
		g.fillRect(0, 0, boardImage.getWidth(), boardImage.getHeight());

		g.setColor(lineColor);
		for (int i = 0; i < boardSize; ++i) {
			int x = getStoneX(i);
			int y1 = getStoneY(0);
			int y2 = getStoneY(boardSize - 1);
			g.drawLine(x, y1, x, y2);

			int y = getStoneY(i);
			int x1 = getStoneX(0);
			int x2 = getStoneX(boardSize - 1);
			g.drawLine(x1, y, x2, y);
		}
	}

	protected void recreateBoardImage() {
		int size = boardImageSize() + 1;

		boardImage = Image.createImage(size, size);
		Graphics g = boardImage.getGraphics();
	}

	protected void setBoardSize(int size) {
		boardSize = size;
	}

	protected void setStoneSize(int size) {
		stoneSize = size;
	}

	protected int boardImageSize() {
		return (1 + boardSize) * stoneSize;
	}

	/**
	 * Get offset of the stone on some position relative to board (in pixels).
	 */
	protected int getStoneX(int i) {
		return (int)((1.0 + i) * stoneSize);
	}

	protected int getStoneY(int i) {
		return getStoneX(i);
	}

	/**
	 * Recheck if background is visible and if board needs any offset.
	 */
	void checkBoardOffset() {
		sx = sy = 0;

		backgroundVisible = false;

		if (boardImage == null) {
			backgroundVisible = true;
			return;
		}

		if (parent.getXDiv() > boardImage.getWidth()) {
			sx = (parent.getXDiv() - boardImage.getWidth()) / 2;
			backgroundVisible = true;
		}
		if (parent.getYDiv() > boardImage.getHeight()) {
			sy = (parent.getYDiv() - boardImage.getHeight()) / 2;
			backgroundVisible = true;
		}
	}


	protected void repaint(Graphics g) {
		checkBoardOffset();

		if (backgroundVisible) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, parent.getXDiv(), parent.getYDiv());
		}

		if (boardImage != null) {
			g.clipRect(0, 0, parent.getXDiv(), parent.getYDiv());
			g.drawImage(boardImage, sx, sy, Graphics.TOP|Graphics.LEFT);
		}

	}

	/**
	 * Repaint board.
	 *
	 * Board need redrawing crosshair every time, so it overloads paint function.
	 */
	public void paint(Graphics g) {
		super.paint(g);
		lastTime = System.currentTimeMillis();
	}

	void paintCrosshair() {
		if (boardImage == null) {
			return;
		}

		long time = System.currentTimeMillis();

		int c = (int)(Math.sin((firstTime - time) / 100) * 100) + 128;
		g.setColor(((c * 256) + c) * 256 + c );
		int x = getStoneX(cx) + sx;
		int y = getStoneY(cx) + sy;

		g.drawLine(x-1, y, x+1, y);
		g.drawLine(x, y-1, x, y+1);
	}



}
