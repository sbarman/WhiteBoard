package whiteboard.packet;

public class ChangeBrushColorPacket extends Packet {
	
	private static final long serialVersionUID = -8305167280872469059L;

	private int h, s, b;
	
	public ChangeBrushColorPacket(int h, int s, int b) {
		this.h = h;
		this.s = s;
		this.b = b;
	}
	
	public int getHue() {
		return h;
	}
	
	public int getSat() {
		return s;
	}
	
	public int getBright() {
		return b;
	}
}
