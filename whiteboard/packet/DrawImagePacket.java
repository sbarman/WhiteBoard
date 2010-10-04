package whiteboard.packet;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;

public class DrawImagePacket extends Packet {
	
	private static final long serialVersionUID = -3140881403667469861L;
	private ImageIcon image;
	private int x, y;

	public DrawImagePacket(Image image, Point topLeft) {
		this.image = new ImageIcon(image);
		this.x = topLeft.x;
		this.y = topLeft.y;
	}
	
	public Image getImage() {
		return image.getImage();
	}
	
	public Point getTopLeftPos() {
		return new Point(x, y);
	}
}
