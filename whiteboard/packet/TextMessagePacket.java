package whiteboard.packet;

public class TextMessagePacket extends Packet {
  
  private static final long serialVersionUID = -8977868389609480830L;
  
  private String message;

  public TextMessagePacket(String message) {
    this.message = message;
  }
  
  public String getMessage() {
    return this.message;
  }
}
