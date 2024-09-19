package com.example.vier_gewinnt_multiplayer.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    Socket client;
    PrintWriter out;
    BufferedReader in;

    public Client(String ip, int port) {
        connectToServer(ip, port);
    }

    // connects to a Server with a IP and Port given by the User
    public void connectToServer(String ip, int port) {
        try {
            client = new Socket(ip, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveBoard() throws IOException {
        String spielFeld = in.readLine();
        System.out.println(spielFeld);
        return spielFeld;
    }

    public void sendMove(int move) {
        out.println(move);
    }
}
