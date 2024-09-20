package com.example.vier_gewinnt_multiplayer.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server implements Runnable {

    int port;

    public ServerSocket socket;

    private Socket hostClient;
    private PrintWriter host_out;
    private BufferedReader host_in;

    private Socket client;
    private PrintWriter client_out;
    private BufferedReader client_in;

    private char[][] board = { // board[spalte][zeile]
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'},
            { 'x', 'x', 'x', 'x', 'x', 'x' ,'x'}
    };

    public Server(int port) throws Exception {
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
        hostClient = socket.accept();
        host_out = new PrintWriter(hostClient.getOutputStream(), true);
        host_in = new BufferedReader(new InputStreamReader(hostClient.getInputStream()));
        System.out.println("hostClient connected!");

        System.out.println("host IP address: " + InetAddress.getLocalHost().getHostAddress().toString());

        // accept client
        if (true) {
            System.out.print("Awaiting client connection...");
            client = socket.accept();
            client_out = new PrintWriter(client.getOutputStream(), true);
            client_in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("client connected!");
        }

        sendeSpielfeld();
        startGame();
    }

    private void startGame() throws Exception {
        boolean hostWins = false;
        boolean clientWins = false;
        boolean hostsTurn = true;
        while (hostWins == clientWins) {
            if (hostsTurn) {
                int pSpalte = Integer.parseInt(host_in.readLine());
                System.out.println("host move");
                if (spalteIstFrei(pSpalte)) {
                    for (int i = board.length-1; i >= 0; i--) {
                        if (board[i][pSpalte] == 'x') {
                            board[i][pSpalte] = 'r';
                            hostWins = prüfeSieger('r', pSpalte);
                            sendeSpielfeld();
                            hostsTurn = false;
                            break;
                        }
                    }
                }
                client.getInputStream().skip(client.getInputStream().available());
                client_in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            } else {
                int pSpalte = Integer.parseInt(client_in.readLine());
                System.out.println("client move");
                if (spalteIstFrei(pSpalte)) {
                    for (int i = board.length-1; i >= 0; i--) {
                        if (board[i][pSpalte] == 'x') {
                            board[i][pSpalte] = 'g';
                            clientWins = prüfeSieger('g', pSpalte);
                            sendeSpielfeld();
                            hostsTurn = true;
                            break;
                        }
                    }
                }
                hostClient.getInputStream().skip(hostClient.getInputStream().available());
                host_in = new BufferedReader(new InputStreamReader(hostClient.getInputStream()));

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

    private boolean prüfeSieger(char spieler, int spalte) {
        String siegerString = "" + spieler + spieler + spieler + spieler;
        int zeile = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i][spalte] != 'x') {
                zeile = i;
                break;
            }
        }
        String horizontal = "";
        String vertikal = "";
        String diagonalEins = "";
        String diagonalZwei = "";
        for (int i = -6; i <= 6; i++) {
            if (zeile + i >= 0 && zeile + i <= 5) horizontal += board[zeile + i][spalte];
            if (spalte + i >= 0 && spalte + i <= 6) vertikal += board[zeile][spalte + i];
            if (spalte + i >= 0 && spalte + i <= 6 && zeile + i >= 0 && zeile + i <= 5) diagonalEins += board[zeile + i][spalte + i];
            if (spalte - i >= 0 && spalte - i <= 6 && zeile + i >= 0 && zeile + i <= 5)diagonalZwei += board[zeile + i][spalte - i];
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
                sending += board[i][j];
            }
        }
        host_out.println(sending);
        if (true) client_out.println(sending);
    }

}
