import java.io.IOException;

//import javax.microedition.lcdui.*;
//import javax.microedition.m3g.*;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import java.lang.System;

public class Goban extends Canvas implements Runnable {

	/**
	 * Is game thread in state of being stopped.
	 */
	private boolean stopped;

	/**
	 * Is game paused.
	 */
	private boolean paused;

	public Goban() {
		stopped = false;
		paused = false;
	}

	public void paint(Graphics g) {
		//Set color to white
		g.setColor( 0x0FFFFFF );
		//Fill entire screen
		g.fillRect( 0, 0, getWidth(), getHeight() );
		//Set color to black
		g.setColor( 0 );

		g.drawString(Long.toString(System.currentTimeMillis()), 0, 0,
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

	public void stop() {
		stopped = true;
	}
}
