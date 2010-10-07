package whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Vector;

import whiteboard.packet.ClearScreenPacket;
import whiteboard.packet.Packet;

public class WhiteBoardClient {
	
	public static final String DEFAULT_HOSTNAME = "127.0.0.1";
	public static final int DEFAULT_PORT = WhiteBoardServer.DEFAULT_PORT;	

	private Socket socket = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private WhiteBoardClientListener listener;
	private int clientId;
	
	public WhiteBoardClient() {
		this(DEFAULT_HOSTNAME, DEFAULT_PORT);
	}
	
	public WhiteBoardClient(String hostName, int port) {
		try {
			socket = new Socket(hostName, port);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			this.clientId = input.readInt();
			System.out.println("Client id: " + clientId);
		} catch (UnknownHostException e) {
			System.out.println("Unable to find host.");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Exception with I/O.");
			System.exit(-1);
		}
	}
	
	public int getClientId() {
		return clientId;
	}
	
	public void sendCommandPacket(Packet p) {
		try {
			p.setClientId(clientId);
			output.writeObject(p);
			System.out.println("Sent packet: " + p);
		} catch (IOException e) {
			System.out.println("Failed to send packet");
		}
	}
	
	public void initListener(WhiteBoard board, DrawingPanel panel) {
		listener = new WhiteBoardClientListener(input, panel, clientId);
		listener.start();
	}
	
	public Vector<Packet> getPacketHistory() {
		return listener.getPacketHistory();
	}
	
};
