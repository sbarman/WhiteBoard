package whiteboard.packet;

public class ClearScreenPacket extends Packet {
	
	private static final long serialVersionUID = 7652768662329754540L;

	public ClearScreenPacket() {
		// Nothing to do
	}
	
	public String toString() {
		return "(" + seqNum + "," + screenId + ") Clear screen";
	}
	
}
