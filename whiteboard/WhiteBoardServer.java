package whiteboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

import whiteboard.packet.Packet;

public class WhiteBoardServer {

	public static final int DEFAULT_PORT = 1500;

	public static void main(String args[]) {
		WhiteBoardServer server = new WhiteBoardServer(DEFAULT_PORT);
		server.start();
	}

	private int port;
	private Vector<WhiteBoardServerThread> openConnections;
	private ArrayList<Packet> packets;

	public WhiteBoardServer(int port) {
		this.port = port;
		openConnections = new Vector<WhiteBoardServerThread>();
		packets = new ArrayList<Packet>();
	}

	private void start() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			System.exit(-1);
		}

		while (true) {
			Socket clientSocket = null;
			int numConnections = 0;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New client: " + clientSocket.getPort());
				WhiteBoardServerThread thread = new WhiteBoardServerThread(
						this, clientSocket, numConnections);
				numConnections++;
				synchronized(openConnections) {
					openConnections.add(thread);
				}
				thread.start();
			} catch (IOException e) {
				System.out.println("Accept failed: " + port);
				System.exit(-1);
			}
		}
	}
	
	public void broadcast(Packet newPacket) {
		synchronized(packets) {
			newPacket.setSeqNum(packets.size());
			packets.add(newPacket);
		}
		
		synchronized(openConnections) {
			for(WhiteBoardServerThread openConnection : openConnections) {
				openConnection.send(newPacket);
			}
		}
	}

	public void removeConnection(WhiteBoardServerThread thread) {
		synchronized(openConnections) {
			openConnections.remove(thread);
		}
	}
}
