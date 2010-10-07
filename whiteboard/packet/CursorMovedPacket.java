package whiteboard.packet;

import java.awt.Point;

public class CursorMovedPacket extends Packet {
	
	private static final long serialVersionUID = -2960079927906087523L;

	private Point p;
	
	public CursorMovedPacket(Point p) {
		this.p = p;
	}
	
	public Point getPoint() {
		return p;
	}
		
}
