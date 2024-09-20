package com.example.vier_gewinnt_multiplayer.network;

import com.example.vier_gewinnt_multiplayer.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {

    private final int port;

    public ServerSocket socket;

    private Socket hostClient;
    private PrintWriter host_out;
    private BufferedReader host_in;

    private Socket client;
    private PrintWriter client_out;
    private BufferedReader client_in;

    private final char[][] board = { // board[spalte][zeile]
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'}
    };

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            startServer(port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startServer(int port) throws Exception {
        // initialize a server on the given port
        socket = new ServerSocket(port);

        // accept hostClient
        System.out.print("Awaiting hostClient connection...");
        Main.startScreen.updateInfoString("Awaiting hostClient connection...");
        hostClient = socket.accept();
        host_out = new PrintWriter(hostClient.getOutputStream(), true);
        host_in = new BufferedReader(new InputStreamReader(hostClient.getInputStream()));
        System.out.println("hostClient connected!");
        Main.startScreen.updateInfoString("hostClient connected!", false);

        System.out.println("host IP address and port: " + InetAddress.getLocalHost().getHostAddress() + ":" + port);

        // accept client
        System.out.print("Awaiting client connection...");
        Main.startScreen.updateInfoString("Connection details: " + InetAddress.getLocalHost().getHostAddress() + ":" + port + "\nAwaiting client connection...");
        client = socket.accept();
        client_out = new PrintWriter(client.getOutputStream(), true);
        client_in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("client connected!");
        Main.startScreen.updateInfoString("client connected!");

        sendeSpielfeld();
        startGame();
    }

    private void startGame() throws Exception {
        boolean hostWins = false;
        boolean clientWins = false;
        boolean hostsTurn = true;

        Main.startScreen.updateInfoString("Game in progress...");

        // main game logic
        while (hostWins == clientWins) {
            if (hostsTurn) {
                int pSpalte = Integer.parseInt(host_in.readLine());
                System.out.println("host move");
                if (spalteIstFrei(pSpalte)) {
                    for (int i = board.length-1; i >= 0; i--) {
                        if (board[i][pSpalte] == 'x') {
                            board[i][pSpalte] = 'r';
                            hostWins = pruefeSieger('r', pSpalte);
                            sendeSpielfeld();
                            hostsTurn = false;
                            break;
                        }
                    }
                }
                // clear the InputStream for the client, in case they sent move messages during the host's turn
                //noinspection ResultOfMethodCallIgnored
                client.getInputStream().skip(client.getInputStream().available());
            } else {
                int pSpalte = Integer.parseInt(client_in.readLine());
                System.out.println("client move");
                if (spalteIstFrei(pSpalte)) {
                    for (int i = board.length-1; i >= 0; i--) {
                        if (board[i][pSpalte] == 'x') {
                            board[i][pSpalte] = 'g';
                            clientWins = pruefeSieger('g', pSpalte);
                            sendeSpielfeld();
                            hostsTurn = true;
                            break;
                        }
                    }
                }
                // clear the InputStream for the host, in case they sent move messages during the client's turn
                //noinspection ResultOfMethodCallIgnored
                hostClient.getInputStream().skip(hostClient.getInputStream().available());
            }
        }

        gameOver(hostWins);
    }

    private void gameOver(boolean hostWins) {
        String output = "Game Over. The winner is: " + (hostWins ? "host" : "client") + "!";
        System.out.println(output);
        client_out.println(output);
        host_out.println(output);
    }

    private boolean spalteIstFrei(int spalte) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][spalte] == 'x') {
                return true;
            }
        }
        return false;
    }

    private boolean pruefeSieger(char spieler, int spalte) {
        String siegerString = "" + spieler + spieler + spieler + spieler;
        int zeile = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i][spalte] != 'x') {
                zeile = i;
                break;
            }
        }
        StringBuilder horizontal = new StringBuilder();
        StringBuilder vertikal = new StringBuilder();
        StringBuilder diagonalEins = new StringBuilder();
        StringBuilder diagonalZwei = new StringBuilder();
        for (int i = -6; i <= 6; i++) {
            if (zeile + i >= 0 && zeile + i <= 5) horizontal.append(board[zeile + i][spalte]);
            if (spalte + i >= 0 && spalte + i <= 6) vertikal.append(board[zeile][spalte + i]);
            if (spalte + i >= 0 && spalte + i <= 6 && zeile + i >= 0 && zeile + i <= 5) diagonalEins.append(board[zeile + i][spalte + i]);
            if (spalte - i >= 0 && spalte - i <= 6 && zeile + i >= 0 && zeile + i <= 5)
                diagonalZwei.append(board[zeile + i][spalte - i]);
        }
        return horizontal.toString().contains(siegerString) || vertikal.toString().contains(siegerString)
                || diagonalEins.toString().contains(siegerString) || diagonalZwei.toString().contains(siegerString);
    }

    private void sendeSpielfeld() {
        StringBuilder sending = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sending.append(board[i][j]);
            }
        }
        host_out.println(sending);
        client_out.println(sending);
    }
}
