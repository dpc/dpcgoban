import javax.microedition.lcdui.Graphics;
import java.lang.Long;

class Board extends UIElementCommon {

	/**
	 * Ctor.
	 */
	public Board(Parent parent) {
		super(parent);
	}

	public void paint(Graphics g) {
		long time = System.currentTimeMillis();
		//Set color to white
		g.setColor( 0x0FFFFFF);
		//Fill entire screen
		g.fillRect( 0, 0, parent.getXDiv(), parent.getYDiv() );
		//Set color to black
		g.setColor( 0 );

		g.drawString(Long.toString(time), 0, 0,
			Graphics.TOP | Graphics.LEFT );
	}
}
