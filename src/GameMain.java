/*
 * Copyright (C) 2008 Dawid Ciężarkiewicz
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.CommandListener;

/**
 * Main game class.
 *
 * Take care of the user command and main canvas creation.
 */
public class GameMain extends MIDlet implements CommandListener {

	private Command exitCmd = new Command("Exit", Command.SCREEN, 1);


	private Display display;
	private Goban goban;

	private Thread thread;

	/**
	 * Create midlet.
	 */
	public GameMain() {
		goban = new Goban();



		goban.addCommand(exitCmd);
		goban.setCommandListener(this);
	}

	/**
	 * Destroup midlet.
	 */
	protected void destroyApp(boolean unconditional) {
		display.setCurrent((Displayable)null);
	}

	/**
	 * Pause application.
	 */
	protected void pauseApp() {
		//goban.mute();
		//stop game's timing thread

		try {
			//we will start another thread after this one finishes
			goban.stop();
			thread.join();
		} catch (InterruptedException ie) {
			//
		}
	}

	/**
	 * Start application.
	 */
	protected void startApp() {
		//theGame.unMute();

		display = Display.getDisplay(this);
		display.setCurrent(goban);

		try {
			// Start the game in its own thread
			thread = new Thread(goban);
			//ensure the game thread will work after pause
			//goban.setDestroyed(false);
			thread.start();
		} catch (Error e) {
			destroyApp(false);
			notifyDestroyed();
		}
	}

	/**
	 * Handle user commands.
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == exitCmd) {
			destroyApp(false);
			notifyDestroyed();
		}
	}
}
