import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

/**
 * UI element to display game stats (scores, times, etc.)
 */
class StatView extends UIElementCommon {
	public StatView(Parent parent) {
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
