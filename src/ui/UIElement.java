import javax.microedition.lcdui.Graphics;

interface UIElement {
	/**
	 * Interface that class aggregating UIElements must
	 * provide.
	 */
	interface Parent {
		public int getXDiv();
		public int getYDiv();
		public int getXSize();
		public int getYSize();
	}

	/**
	 * Repaint element.
	 */
	public void paint(Graphics g);
}
