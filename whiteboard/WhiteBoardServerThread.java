package whiteboard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import whiteboard.packet.Packet;

public class WhiteBoardServerThread extends Thread {
	
	private int clientId;
	private Socket socket = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private WhiteBoardServer server;

	public WhiteBoardServerThread(WhiteBoardServer server, Socket socket, int clientId) {
		super("WhiteBoardServerThread");
		this.socket = socket;
		this.server = server;
		this.clientId = clientId;
		try {
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
			output.writeInt(clientId);
			output.flush();
		} catch (IOException e) {
			System.out.println("Unable to start server thread for client");
			input = null;
			output = null;
			this.socket = null;
		}
	}

	public void run() {
		System.out.println("Starting server " + clientId);
		while (socket != null && !socket.isClosed()) {
			try {
				Object o = input.readObject();
				if (o instanceof Packet) {
					Packet packet = (Packet) o;
					System.out.println("Recieved(" + clientId + "): "
							+ packet);
					server.broadcast(packet);
				}
			} catch (IOException e) {
				System.out.println("Exception when listening for packets.");
			} catch (ClassNotFoundException e) {
				System.out.println("Exception when listening for packets.");
			}
		}
		System.out.println("Thread finished");
	}

	public void send(Packet packet) {
		if (socket != null && !socket.isClosed()) {
			System.out.println("Sent(" + clientId + "): " + packet);
			try {
				output.writeObject(packet);
			} catch (IOException e) {
				System.out.println("Exception when sending packets.");
				e.printStackTrace();
			}
		}
	}
}
