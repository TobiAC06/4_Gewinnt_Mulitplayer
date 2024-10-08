package Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class Client_4_Gewinnt {
	static private Socket client;
	static private PrintWriter out;
	static private BufferedReader in;
	static private Server_4_Gewinnt server;

	public static void main(String args[]) throws Exception {
		
	}
	// creats a server over on a port
	public static void createServer() throws Exception {
		int port = Integer.parseInt( new JOptionPane().showInputDialog("Port:"));
		server = new Server_4_Gewinnt(port);
		client = new Socket("127.0.0.1", port);
		out = new PrintWriter(client.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	}
	
	// connects to a Server with a IP and Port given by the User
	public static void connectToServer() throws Exception {

		// creates elements for the JOptionPane to create a more or less reasonable
		// Layout
		JTextField ipField = new JTextField(5);
		JTextField portField = new JTextField(5);
		JLabel ipLabel = new JLabel("IP:");
		JLabel portLabel = new JLabel("Port:");

		// opens a JOptionPane with the Elements from above and checks if a input has
		// been made or if the JOptionPane was closed
		int result = JOptionPane.showConfirmDialog(null, new Object[] { ipLabel, ipField, portLabel, portField },
				"Please Enter IP and Port of Server", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			int port = Integer.parseInt(portField.getText());
			client = new Socket(ipField.getText(), port);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.println(in.readLine());
		}

	}
}
