import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

class Timers extends UIElementCommon {
	public Timers(Parent parent) {
		super(parent);
	}

	protected void repaint(Graphics g) {
		g.setColor(0x08f2020);
		g.setClip(
			parent.getXDiv(), 0,
			parent.getXSize(), parent.getYDiv()
			);
		g.fillRect(
			parent.getXDiv(), 0,
			parent.getXSize(), parent.getYDiv()
			);
	}
}
