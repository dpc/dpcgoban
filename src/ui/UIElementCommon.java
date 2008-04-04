import javax.microedition.lcdui.Graphics;

/**
 * Common parts of UIElement implementations.
 */
class UIElementCommon implements UIElement {
	protected Parent parent;
	private boolean needsRedraw;

	UIElementCommon(Parent parent) {
		this.parent = parent;
		needsRedraw = true;
	}

	protected void markDirty() {
		needsRedraw = true;
	}

	protected boolean isDirty() {
		return needsRedraw;
	}


	protected void repaint(Graphics g) {
		// TODO: assert not implemented
	}

	public void paint(Graphics g) {
		if (isDirty()) {
			repaint(g);
		}
		needsRedraw = false;
	}
}
