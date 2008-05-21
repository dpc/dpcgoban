import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

class Chat extends UIElementCommon {

	Image chatImage = null;

	public Chat(Parent parent) {
		super(parent);

		chatImage = Image.createImage(
			parent.getXSize(),
			parent.getYSize() - parent.getYDiv()
			);

	}

	public void appendString(String str) {
		Font f = Font.getDefaultFont();
		if (str.length() == 0) {
			return;
		}
		for (int l = str.length(); l > 0; --l) {
			if (f.stringWidth(str.substring(0, l)) < parent.getXSize()) {
				drawString(str.substring(0, l));
				appendString(str.substring(l, str.length()));
				return;
			}
		}
		drawString("??? ASSERT ERROR !!!");
	}

	public void drawString(String str) {
		Graphics g = chatImage.getGraphics();
		Font f = Font.getDefaultFont();

		g.copyArea(
			0, f.getHeight(),
			chatImage.getWidth(), chatImage.getHeight() - f.getHeight(),
			0, 0,
			Graphics.TOP | Graphics.LEFT
			);
		g.setColor(0x0f0f0f0);
		g.fillRect(
			0, chatImage.getHeight() - f.getHeight(),
			chatImage.getWidth(), chatImage.getHeight()
			);
		g.setColor(0x0000040);
		g.drawString(
			str,
			0, chatImage.getHeight(), Graphics.BOTTOM | Graphics.LEFT
			);
		markDirty();
	}

	protected void repaint(Graphics g) {
		g.setClip(0, parent.getYDiv(), parent.getXSize(), parent.getYDiv());
		g.drawImage(chatImage, 0, parent.getYDiv(), Graphics.TOP|Graphics.LEFT);
	}
}
