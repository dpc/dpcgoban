interface GameController {
	public void placeStone(int x, int y, int c);
	public void clearBoard();
	public void gameInfo(String s);
	public String name();

	public void connectedTo(Arbiter arbiter);
}
