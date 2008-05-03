import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Command;
import javax.microedition.media.*;

import javax.microedition.media.control.*;

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
	public BoardView boardui = new BoardView(this);

	Player player;

	/**
	 * Element of UI for timers display and such.
	 */
	public Timers timers = new Timers(this);

	/**
	 * Element of UI for chat and logs.
	 */
	public Chat chat = new Chat(this);

	/**
	 * Board representation.
	 */
	public Board board = null;

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


	public boolean timerToggled = false;
	public boolean chatToggled = false;

	public LocalGameController gameController;
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
		playMusic();

		board = new Board(boardui);
		gameController = new LocalGameController(board);
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

		timers.paint(g);
		boardui.paint(g);
		chat.paint(g);
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

	public void playMusic() {
		try
		{
			player = Manager.createPlayer(
				getClass().getResourceAsStream("/intro.midi"), "audio/midi"
				);
			// XXX: FIXME: setting volume makes player silent
			//VolumeControl vc = (VolumeControl) player.getControl("VolumeControl");
			//vc.setLevel(100);
			//player.start();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	public void stopMusic() {
		try
		{
			player.stop();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}
	/**
	 * Handle key presses.
	 */
	public void keyPressed(int keycode) {
		if (board == null) {
			return;
		}

		switch (getGameAction(keycode)) {
			case UP:
				board.moveCrosshair(Board.MOVE_UP);
				break;
			case DOWN:
				board.moveCrosshair(Board.MOVE_DOWN);
				break;
			case LEFT:
				board.moveCrosshair(Board.MOVE_LEFT);
				break;
			case RIGHT:
				board.moveCrosshair(Board.MOVE_RIGHT);
				break;
			case GAME_A:
				board.zoomView(Board.ZOOM_IN);
				break;
			case GAME_B:
				board.zoomView(Board.ZOOM_OUT);
				break;
			case GAME_C:
				timerToggled = !timerToggled;
				repaintUI();
				break;
			case GAME_D:
				chatToggled = !chatToggled;
				repaintUI();
				break;
			case FIRE:
				gameController.moveRequest();
				break;

		}
	}

	public void repaintUI() {
		timers.markDirty();
		boardui.markDirty();
		chat.markDirty();
	}

	public int getXDiv() {
		if (timerToggled) {
			return getWidth();
		}
		return getWidth() * 3 / 4;
	}

	public int getYDiv() {
		if (chatToggled) {
			return getHeight();
		}
		return getHeight() * 5 / 6;
	}

	public int getXSize() {
		return getWidth();
	}

	public int getYSize() {
		return getHeight();
	}
}
