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
	public Command exitCmd = new Command("Exit", Command.BACK, 10);

	/**
	 * Command event when skip intro was requested.
	 */
	public Command skipIntroCmd = new Command("Skip", Command.SCREEN, 10);

	/**
	 * Element of UI for board display.
	 */
	public BoardView boardui = new BoardView(this);

	public Command beClientCmd = new Command("Connect to remote", Command.SCREEN, 1);
	public Command beServerCmd = new Command("Host game", Command.SCREEN, 1);
	public Command playWhiteCmd = new Command("Play white", Command.SCREEN, 1);
	public Command playBlackCmd = new Command("Play black", Command.SCREEN, 1);

	public Command printHelpCmd = new Command("Print help", Command.HELP, 1);
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
		chat.appendString("Welcome in DPC Goban!");
		chat.appendString("Connect to or host a game to start.");
		chat.appendString("---");
	}

	public void beServer() {
		Arbiter arbiter = new LocalArbiter();
		gameController = new LocalGameController(board);
		gameController.connect(arbiter);
	}

	public void beClient() {
	}
	public void playBlack() {
	}
	public void playWhite() {
	}

	public void printUIHelp() {
		chat.appendString("1, 3 - zoom in/out");
		chat.appendString("7, 9 - toggle side, chat bars");
		chat.appendString("arrows - move; action - place stone");
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
		addCommand(playWhiteCmd);
		addCommand(playBlackCmd);
		addCommand(beClientCmd);
		addCommand(beServerCmd);
		addCommand(printHelpCmd);
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
			// TODO: XXX: FIXME: setting volume makes player silent
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
				if (gameController != null) {
					gameController.moveRequest();
				}
				break;

		}
	}

	public boolean isActive() {
		return gameController != null;
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
