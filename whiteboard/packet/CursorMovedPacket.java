package whiteboard.packet;

public class CursorMovedPacket extends Packet {
	
	private static final long serialVersionUID = -2960079927906087523L;

	private int x, y;
	
	public CursorMovedPacket(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
}
