package whiteboard;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import whiteboard.packet.ChangeBrushColorPacket;
import whiteboard.packet.ChangeBrushSizePacket;
import whiteboard.packet.ClearScreenPacket;
import whiteboard.packet.CursorMovedPacket;
import whiteboard.packet.DrawImagePacket;
import whiteboard.packet.DrawLinePacket;
import whiteboard.packet.Packet;

public class WhiteBoardClientListener extends Thread {
	
	private ObjectInputStream input;
	private ArrayList<Packet> newPackets;
	private WhiteBoard board;
	private DrawingPanel panel;
	
	
	public WhiteBoardClientListener(ObjectInputStream input, WhiteBoard board,
			DrawingPanel panel) {
		this.input = input;
		this.board = board;
		this.panel = panel;
		newPackets = new ArrayList<Packet>();
	}
	
	public void run() {
		System.out.println("Starting listener.");
		while(true) {
			try {
				Object o = input.readObject();
				if (o instanceof Packet) {
					synchronized(newPackets) {
						System.out.println("Received new packet:" + o);
						Packet packet = (Packet) o;
						newPackets.add(packet);
						handlePacket(packet);
					}
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
	
	private void handlePacket(Packet packet) {
		if(packet instanceof ChangeBrushColorPacket) {
			
		} else if(packet instanceof ChangeBrushSizePacket) {
			
		} else if(packet instanceof ClearScreenPacket) {
			
		} else if(packet instanceof CursorMovedPacket) {
			CursorMovedPacket cmp = (CursorMovedPacket) packet;
			panel.drawPoint(cmp.getX(), cmp.getY(), Color.BLACK, 5);
			panel.repaint();
		} else if(packet instanceof DrawImagePacket) {
			
		} else if(packet instanceof DrawLinePacket) {
			
		} else {
			System.out.println("Unknown packet");
		}
	}

	public ArrayList<Packet> getNewPackets() {
		ArrayList<Packet> returnList;
		synchronized(newPackets) {
			returnList = new ArrayList<Packet>(newPackets);
			newPackets.clear();
		}
		return returnList;
	}

}
