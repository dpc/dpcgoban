interface GameController {
	public void placeStone(int x, int y, int c, int s);
	public void move(int x, int y, int c);
	public void clearBoard();
	public void gameInfo(String s);
	public String name();
	public boolean isActive();
	public void shutdown();

	public void connectedTo(LocalArbiter arbiter);
}
