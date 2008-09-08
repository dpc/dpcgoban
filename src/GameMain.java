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

	/**
	 * Game display.
	 */
	private Display display;

	/**
	 * Main game controler.
	 */
	private Goban goban;

	/**
	 * Main game thread.
	 */
	private Thread thread;

	/**
	 * Create midlet.
	 */
	public GameMain() {
		goban = new Goban();

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
		try {
			goban.stop();
			thread.join();
		} catch (InterruptedException ie) {}
	}

	/**
	 * Start application.
	 */
	protected void startApp() {
		display = Display.getDisplay(this);
		display.setCurrent(goban);

		try {
			thread = new Thread(goban);
			thread.start();
		} catch (Error e) {
			destroyApp(false);
			notifyDestroyed();
		}
	}

	/**
	 * Handle user menu commands.
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == goban.exitCmd) {
			goban.shutdown();
			destroyApp(false);
			notifyDestroyed();
			return;
		} else if (c == goban.moveReqCmd) {
			goban.moveReq();
		} else if (c == goban.passReqCmd) {
			goban.passReq();
		} else if (c == goban.beServerCmd) {
			goban.beServer();
		} else if (c == goban.beClientCmd) {
			goban.beClient();
		} else if (c == goban.playWhiteCmd) {
			goban.playWhite();
		} else if (c == goban.playBlackCmd) {
			goban.playBlack();
		} else if (c == goban.printHelpCmd) {
			goban.printUIHelp();
		} else if (c == goban.muteToggleCmd) {
			goban.muteToggle();
		}

		goban.repaintUI();
	}
}
