import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

class Chat extends UIElementCommon {
	public Chat(Parent parent) {
		super(parent);
	}

	protected void repaint(Graphics g) {
		g.setClip(
			0, parent.getYDiv(),
			parent.getXSize(), parent.getYSize()
			);
		g.setColor(0x0f0f0f0);
		g.fillRect(
			0, parent.getYDiv(),
			parent.getXSize(), parent.getYSize()
			);
		g.setColor(0x0000040);
		g.drawString(
			"1, 3 - zoom in/out",
			0, parent.getYDiv(), Graphics.TOP | Graphics.LEFT
			);
		g.drawString(
			"arrows - move",
			0, parent.getYDiv() + 10, Graphics.TOP | Graphics.LEFT
			);
		g.drawString(
			"action - place stone",
			0, parent.getYDiv() + 20, Graphics.TOP | Graphics.LEFT
			);
	}
}
