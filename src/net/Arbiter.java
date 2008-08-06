interface Arbiter {
	public interface Parent {
		public void handleArbiterMsg(String s);
		public void handleArbiterInitFinished();
	}
	final static int COLOR_BLACK = 0;
	final static int COLOR_WHITE = 1;
	public void connected(GameController gc);
	public void disconnected(GameController gc);


	// =========================================
	// Things that are requests from the GameControler
	// =========================================
	/**
	 * Request from GC to handle specified color in the game.
	 */
	public void handleColor(GameController gc, int color);
	/**
	 * Request from GC to unhandle specified color in the game.
	 */
	public void unhandleColor(GameController gc, int color);
	/**
	 * Request from GC to make a move.
	 */
	public void moveRequest(GameController gc, int x, int y);
}
