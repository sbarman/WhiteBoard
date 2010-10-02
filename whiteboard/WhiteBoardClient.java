package whiteboard;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import whiteboard.packet.Packet;

public class WhiteBoardClient {
	
	public static final String DEFAULT_HOSTNAME = "127.0.0.1";
	public static final int DEFAULT_PORT = WhiteBoardServer.DEFAULT_PORT;	
	
	public static void main(String args[]) {
		WhiteBoardClient client = new WhiteBoardClient();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			try {
				client.sendMessage(in.readLine());
				System.out.println(client.getMessage());
			} catch (IOException e) {
			}
			
		}
	}
	
	private Socket socket = null;
	private BufferedReader input = null;
	private PrintWriter output = null;
	
	public WhiteBoardClient() {
		this(DEFAULT_HOSTNAME, DEFAULT_PORT);
	}
	
	public WhiteBoardClient(String hostName, int port) {
		try {
			socket = new Socket(hostName, port);
			input = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.out.println("Unable to find host.");
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Exception with I/O.");
			System.exit(-1);
		}
	}
	
	public void sendCommandPacket(Packet p) {
		
	}
	
	public ArrayList<Packet> getCommandPackets() {
		return new ArrayList<Packet>();
	}
	
	public void sendMessage(String message) {
		output.println(message);
	}
	
	public String getMessage() {
		try {
			return input.readLine();
		} catch (IOException e) {
			System.out.println("Problem reading message off socket.");
		}
		return "";
	}
};
