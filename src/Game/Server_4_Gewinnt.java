package Game;

import java.net.*;
import java.io.*;

public class Server_4_Gewinnt {
	public ServerSocket socket;
	private Socket hostClient;
	private PrintWriter host_out;
	private BufferedReader host_in;
	private Socket client;
	private PrintWriter client_out;
	private BufferedReader client_in;
	private char[][] board = {	 { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'}, // board[spalte][zeile]
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'}, 
								{ 'x', 'x', 'x', 'x', 'x', 'x' ,'x'} };

	public Server_4_Gewinnt(int port) throws Exception {
		starteServer(port);
		hostClient = new Socket("127.0.0.1", port);
		host_out = new PrintWriter(hostClient.getOutputStream(), true);
		host_in = new BufferedReader(new InputStreamReader(hostClient.getInputStream()));
	}

	public void starteServer(int port) throws Exception {
		// initalising a server on the port 52072
		socket = new ServerSocket(port);
		//client = socket.accept();
		//client_out = new PrintWriter(client.getOutputStream(), true);
		//client_in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		//sendeSpielfeld();
		//Spiel_4_Gewinnt();
	}
	
	public void print() {
		host_out.println("funktioniert");
	}

	private void Spiel_4_Gewinnt() throws Exception {
		boolean hostWins = false;
		boolean clientWins = false;
		boolean hostsTurn = true;
		while (hostWins == clientWins) {
			if (hostsTurn) {
				int pSpalte = Integer.parseInt(host_in.readLine());
				if (spalteIstFrei(pSpalte)) {
					for (int i = 0; i < board[pSpalte].length; i++) {
						if (board[pSpalte][i] != 'x') {
							board[pSpalte][i - 1] = 'r';
						}
						hostWins = prüfeSieger('r', pSpalte);
						sendeSpielfeld();
						hostsTurn = false;
					}
				}
			} else {
				int pSpalte = Integer.parseInt(client_in.readLine());
				if (spalteIstFrei(pSpalte)) {
					for (int i = 0; i < board[pSpalte].length; i++) {
						if (board[pSpalte][i] != 'x') {
							board[pSpalte][i - 1] = 'g';
						}
					}
					clientWins = prüfeSieger('g', pSpalte);
					sendeSpielfeld();
					hostsTurn = true;
				}

			}
		}
	}

	private boolean spalteIstFrei(int spalte) {
		for (int i = 0; i < board[spalte].length; i++) {
			if (board[spalte][i] == 'x') {
				return true;
			}
		}
		return false;
	}

	private boolean prüfeSieger(char spieler, int spalte) {
		String siegerString = "" + spieler + spieler + spieler + spieler;
		int zeile = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[spalte][i] != 'x') {
				zeile = i;
				break;
			}
		}
		String horizontal = "";
		String vertikal = "";
		String diagonalEins = "";
		String diagonalZwei = "";
		for (int i = -4; i <= 4; i++) {
			horizontal += board[spalte][zeile + i];
			vertikal += board[spalte + i][zeile];
			diagonalEins += board[spalte + i][zeile + i];
			diagonalZwei += board[spalte - i][zeile + i];
		}
		if (horizontal.indexOf(siegerString) >= 0 || vertikal.indexOf(siegerString) >= 0
				|| diagonalEins.indexOf(siegerString) >= 0 || diagonalZwei.indexOf(siegerString) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	private void sendeSpielfeld() {
		String sending = "";
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				sending += board[j][i];
			}
		}
		host_out.println(sending);
		client_out.println(sending);
	}

}
