package whiteboard.packet;

import java.awt.Point;

public class DrawLinePacket extends Packet {
	
	private static final long serialVersionUID = 4007473409957319810L;

	private int x1, y1, x2, y2;
	
	public DrawLinePacket(Point startPoint, Point endPoint) {
		this.x1 = startPoint.x;
		this.y1 = startPoint.y;
		this.x2 = endPoint.x;
		this.y2 = endPoint.y;
	}
	
	public Point getStartPoint() {
		return new Point(x1, y1);
	}
	
	public Point getEndPoint() {
		return new Point(x2, y2);
	}
}
