package whiteboard;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class WhiteBoardServer {

	public static final int DEFAULT_PORT = 1500;

	public static void main(String args[]) {
		WhiteBoardServer server = new WhiteBoardServer(DEFAULT_PORT);
		server.start();
	}

	private int port;
	private Vector<WhiteBoardServerThread> openSockets;

	public WhiteBoardServer(int port) {
		this.port = port;
		openSockets = new Vector<WhiteBoardServerThread>();
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
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New client: " + clientSocket.getPort());
				WhiteBoardServerThread thread = new WhiteBoardServerThread(
						this, clientSocket);
				openSockets.add(thread);
				thread.start();
			} catch (IOException e) {
				System.out.println("Accept failed: " + port);
				System.exit(-1);
			}
		}
	}
	
	public void broadcast(String newInput) {
		for(WhiteBoardServerThread openSocket : openSockets) {
			openSocket.send(newInput);
		}
	}
}
