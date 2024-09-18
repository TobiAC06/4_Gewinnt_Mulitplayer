package Game;

import java.net.*;
import java.io.*;

public class Server_4_Gewinnt {
	private ServerSocket socket;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;

	public static void main(String[] args) {

	}

	public void starteServer() {
		try {
			socket = new ServerSocket(52072);
			client = socket.accept();
			out = new PrintWriter(client.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("hat funktioniert");
	}
}
