import javax.microedition.lcdui.Graphics;

/**
 * Interface to game intro.
 */
public interface Intro {
	/**
	 * Repaint intro.
	 */
	void paint(Graphics g, long time);

	/**
	 * Is demo finished?
	 */
	boolean done();
}
