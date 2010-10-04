package whiteboard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import whiteboard.packet.Packet;

public class WhiteBoardClientListener extends Thread {
	
	private ObjectInputStream input;
	private ArrayList<Packet> newPackets;
	
	
	public WhiteBoardClientListener(ObjectInputStream input) {
		this.input = input;
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
						newPackets.add((Packet) o);
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
	
	public ArrayList<Packet> getNewPackets() {
		ArrayList<Packet> returnList;
		synchronized(newPackets) {
			returnList = new ArrayList<Packet>(newPackets);
			newPackets.clear();
		}
		return returnList;
	}

}
