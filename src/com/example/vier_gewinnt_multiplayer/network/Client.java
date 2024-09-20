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

    // connects to a server with an IP and port given by the user
    public void connectToServer(String ip, int port) {
        try {
            client = new Socket(ip, port);
            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String receiveMessage() {
        String message;
        try {
            message = in.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving server message:");
            throw new RuntimeException(e);
        }
        return message;
    }

    public void sendMove(int move) {
        out.println(move);
    }
}
