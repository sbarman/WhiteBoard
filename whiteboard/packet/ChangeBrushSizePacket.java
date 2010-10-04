package whiteboard.packet;

public class ChangeBrushSizePacket extends Packet {
	
	private static final long serialVersionUID = 2797620145485678107L;

	private int size;
	
	public ChangeBrushSizePacket(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public String toString() {
		return "(" + seqNum + "," + screenId + ") Size: " + size;
	}
}
