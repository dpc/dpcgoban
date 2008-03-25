import javax.microedition.lcdui.Graphics;

/**
 * Common parts of UIElement implementations.
 */
class UIElementCommon implements UIElement {
	protected Parent parent;

	UIElementCommon(Parent parent) {
		this.parent = parent;
	}

	public void paint(Graphics g) {
		// TODO: assert NotImplemented?
	}
}
