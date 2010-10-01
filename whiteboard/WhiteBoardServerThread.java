package whiteboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WhiteBoardServerThread extends Thread {
	private Socket socket = null;
	private BufferedReader input = null;
	private PrintWriter output = null;
	private WhiteBoardServer server;

	public WhiteBoardServerThread(WhiteBoardServer server, Socket socket) {
		super("WhiteBoardServerThread");
		this.socket = socket;
		this.server = server;
		try {
			input = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			output = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("Unable to start server thread for client");
			input = null;
			output = null;
			this.socket = null;
		}

	}

	public void run() {
		while (socket != null && !socket.isClosed()) {
			try {
				String message = input.readLine();
				System.out.println("Recieved(" + socket.getPort() + "): " + message);
				server.broadcast(message);
			} catch (IOException e) {
				e.printStackTrace();
				// continue as if nothing happened
			}
		}
		System.out.println("Thread finished");
	}

	public void send(String message) {
		if (socket != null && !socket.isClosed()) {
			System.out.println("Sent(" + socket.getPort() + "): " + message);
			output.println(message);
		}
	}
}
