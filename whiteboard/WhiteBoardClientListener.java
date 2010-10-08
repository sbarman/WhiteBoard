package whiteboard;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Vector;

import whiteboard.packet.ChangeBrushColorPacket;
import whiteboard.packet.ChangeBrushSizePacket;
import whiteboard.packet.ClearScreenPacket;
import whiteboard.packet.CursorMovedPacket;
import whiteboard.packet.DrawImagePacket;
import whiteboard.packet.DrawLinePacket;
import whiteboard.packet.Packet;
import whiteboard.packet.TextMessagePacket;

public class WhiteBoardClientListener extends Thread {
	
	private ObjectInputStream input;
	private DrawingPanel panel;
	private Vector<Packet> packetHistory;
	
	// Stores the current state of each client seen
	private HashMap<Integer, WhiteBoardState> clientStates;
  private int clientId;
  private WhiteBoard wb;
	
	class WhiteBoardState {
		int brushSize;
		Color color;
		
		public WhiteBoardState() {
			brushSize = WhiteBoard.DEFAULT_RADIUS;
			color = WhiteBoard.DEFAULT_COLOR;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof WhiteBoardState) { 
				WhiteBoardState other = (WhiteBoardState) o;
				return color.equals(other.color) && brushSize == other.brushSize;
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return color.hashCode() + brushSize;
		}
	}
	
	
	public WhiteBoardClientListener(ObjectInputStream input,
			WhiteBoard wb, int clientId) {
		this.input = input;
		this.panel = wb.getDrawingPanel();
		this.clientId = clientId;
		this.wb = wb;
		packetHistory = new Vector<Packet>();
		clientStates = new HashMap<Integer, WhiteBoardState>();
	}
	
	public void run() {
		System.out.println("Starting listener.");
		while(true) {
			try {
				Object o = input.readObject();
				if (o instanceof Packet) {
					System.out.println("Received new packet:" + o);
					Packet packet = (Packet) o;
					handlePacket(packet);
					packetHistory.add(packet);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public Vector<Packet> getPacketHistory() {
		return new Vector<Packet>(packetHistory);
	}
	
	private void handlePacket(Packet packet) {
		int id = packet.getScreenId();
		if (!clientStates.containsKey(packet.getScreenId())) {
			clientStates.put(id, new WhiteBoardState());
		}
		
		if(packet instanceof ChangeBrushColorPacket) {
			clientStates.get(id).color = ((ChangeBrushColorPacket) packet).getColor();
		} else if(packet instanceof ChangeBrushSizePacket) {
			clientStates.get(id).brushSize = ((ChangeBrushSizePacket) packet).getSize();	
		} else if(packet instanceof ClearScreenPacket) {
			panel.clearImage();
		} else if(packet instanceof CursorMovedPacket) {
			CursorMovedPacket cmp = (CursorMovedPacket) packet;
			if (packet.getScreenId() != clientId) {
			  panel.setRemoteCursor(cmp.getPoint(), clientStates.get(id).brushSize);
			  panel.repaint();
			}
		} else if(packet instanceof DrawImagePacket) {
			
		} else if(packet instanceof DrawLinePacket) {
			DrawLinePacket dlp = (DrawLinePacket) packet;
			Color color;
			if (dlp.getErasing()) {
				color = Color.WHITE;
			} else {
				System.out.println(clientStates.containsKey(id));
				color = clientStates.get(id).color;
			}
			System.out.println(color);
			panel.drawLine(dlp.getStartPoint(), dlp.getEndPoint(), color,
					clientStates.get(id).brushSize);
			if (dlp.getScreenId() != clientId) {
			  panel.setRemoteCursor(dlp.getEndPoint(), clientStates.get(id).brushSize);
			}
			panel.repaint();
		} else if(packet instanceof TextMessagePacket) {
		  TextMessagePacket tmp = (TextMessagePacket) packet;
		  if (tmp.getScreenId() == clientId) {
		    wb.addMessage(tmp.getMessage());
		  } else {
		    wb.addMessage("> " + tmp.getMessage());
		  }
		} else {
			System.out.println("Unknown packet");
		}
	}
}
