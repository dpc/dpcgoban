import java.io.IOException;

//import javax.microedition.lcdui.*;
//import javax.microedition.m3g.*;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import java.lang.System;
import javax.microedition.lcdui.Command;

public class Goban extends Canvas implements Runnable {

	public Command exitCmd = new Command("Exit", Command.SCREEN, 1);
	public Command skipDemoCmd = new Command("Skip", Command.SCREEN, 1);

	/**
	 * Is game thread in state of being stopped.
	 */
	private boolean stopped;

	/**
	 * Is game paused.
	 */
	private boolean paused;

	private Demo demo;

	public Goban() {
		stopped = false;
		paused = false;

		addCommand(skipDemoCmd);

		try {
			demo = new Cube(this);
		} catch (Exception e) {
			stopped = true;
		}
	}

	public void paint(Graphics g) {
		long time = System.currentTimeMillis();
		if (demo != null) {
			demo.paint(g, time);
			if (demo.done()) {
				skipDemo();
			}
			return;
		}

		//Set color to white
		g.setColor( 0x0FFFFFF );
		//Fill entire screen
		g.fillRect( 0, 0, getWidth(), getHeight() );
		//Set color to black
		g.setColor( 0 );

		g.drawString(Long.toString(time), 0, 0,
			Graphics.TOP | Graphics.LEFT );
	}

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

	public void skipDemo() {
		removeCommand(skipDemoCmd);
		addCommand(exitCmd);
		demo = null;
	}

	public void stop() {
		stopped = true;
	}
}
