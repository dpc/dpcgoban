import javax.microedition.lcdui.Graphics;

/**
 * Common parts of UIElement implementations.
 */
class UIElementCommon implements UIElement {
	/**
	 * Parent implementing UIElement.Parent.
	 */
	protected Parent parent;

	/**
	 * Is dirty?
	 */
	private boolean needsRedraw = true;

	/**
	 * Ctor.
	 */
	UIElementCommon(Parent parent) {
		this.parent = parent;
	}

	/**
	 * Set needsRedraw to true.
	 */
	public void markDirty() {
		needsRedraw = true;
	}

	/**
	 * Is needsRedraw true?
	 */
	protected boolean isDirty() {
		return needsRedraw;
	}


	/**
	 * Repaint UIElement.
	 *
	 * This should be overloaded in child classes.
	 */
	protected void repaint(Graphics g) {
		// TODO: assert not implemented
	}

	/**
	 * Test if element is dirt and repaint if needed.
	 */
	public void paint(Graphics g) {
		if (isDirty()) {
			repaint(g);
		}
		needsRedraw = false;
	}
}
