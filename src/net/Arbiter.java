interface Arbiter {
	public interface Parent {
		public void handleArbiterMsg(String s);
		public void handleArbiterInitFinished();
	}
	final static int COLOR_BLACK = 0;
	final static int COLOR_WHITE = 1;
	public void connected(GameController gc);
	public void disconnected(GameController gc);
	public void handleColor(GameController gc, int color);
	public void unhandleColor(GameController gc, int color);
	public void moveRequest(int x, int y);
}
