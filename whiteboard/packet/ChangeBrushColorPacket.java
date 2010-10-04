package whiteboard.packet;

import java.awt.Color;

public class ChangeBrushColorPacket extends Packet {
	
	private static final long serialVersionUID = -8305167280872469059L;

	private Color color;
	
	public ChangeBrushColorPacket(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
