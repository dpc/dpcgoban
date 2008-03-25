import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Command;

/**
 * Game board, timers and everything used to play go.
 *
 * This is main controlling class. It has its own thread
 * to control game, networking etc.
 */
public class Goban extends Canvas implements Runnable, UIElement.Parent {

	/**
	 * Command event when application close was requested.
	 */
	public Command exitCmd = new Command("Exit", Command.SCREEN, 1);

	/**
	 * Command event when skip intro was requested.
	 */
	public Command skipIntroCmd = new Command("Skip", Command.SCREEN, 1);

	/**
	 * Element of UI for board display.
	 */
	public Board board = new Board(this);

	/**
	 * Element of UI for timers display and such.
	 */
	public Timers timers = new Timers(this);

	/**
	 * Element of UI for chat and logs.
	 */
	public Chat chat = new Chat(this);

	/**
	 * Is game thread in state of being stopped.
	 */
	private boolean stopped;

	/**
	 * Is game paused.
	 */
	private boolean paused;

	/**
	 * Intro.
	 *
	 * Nil if none.
	 */
	private Intro demo;

	/**
	 * Ctor.
	 */
	public Goban() {
		stopped = false;
		paused = false;

		addCommand(skipIntroCmd);

		try {
			demo = new Cube(this);
		} catch (Exception e) {
			/* fails on problems with textures etc. */
			stopped = true;
		}
	}

	/**
	 * Main paint() function for this midlet.
	 */
	public void paint(Graphics g) {
		long time = System.currentTimeMillis();
		if (demo != null) {
			demo.paint(g, time);
			if (demo.done()) {
				skipIntro();
			}
			return;
		}

		board.paint(g);
	}

	/**
	 * Thread runs here.
	 */
	public void run() {
		while (!stopped) {
			if (!paused) {
				repaint();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
	}

	/**
	 * Skip intro mode.
	 */
	public void skipIntro() {
		removeCommand(skipIntroCmd);
		addCommand(exitCmd);
		demo = null;
	}

	/**
	 * Stop game.
	 */
	public void stop() {
		stopped = true;
	}

	public int getXDiv() {
		return getWidth() * 3 / 4;
	}

	public int getYDiv() {
		return getHeight() * 3 / 4;
	}

	public int getXSize() {
		return getWidth();
	}

	public int getYSize() {
		return getHeight();
	}
}
