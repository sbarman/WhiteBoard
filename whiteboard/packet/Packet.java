package whiteboard.packet;

import java.io.Serializable;

public abstract class Packet implements Serializable{
	
	private static final long serialVersionUID = 7778199136243999651L;
	
	protected int seqNum;
	protected int screenId;
	
	public int getSeqNum() {
		return seqNum;
	}
	
	public int getScreenId() {
		return screenId;
	}
	
	public void setSeqNum(int num) {
		seqNum = num;
	}
	
	public void setClientId(int id) {
		screenId = id;
	}
}
