package com.example.vier_gewinnt_multiplayer;

import com.example.vier_gewinnt_multiplayer.ui.PlayScreen;
import com.example.vier_gewinnt_multiplayer.network.Client;
import com.example.vier_gewinnt_multiplayer.network.Server;
import com.example.vier_gewinnt_multiplayer.ui.StartScreen;

import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static Server server;
    public static Client client;
    public static StartScreen startScreen;

    public static void main(String[] args) {
        startScreen = new StartScreen();
    }

    /**
     * Starts the game in host mode
     * (this method is called from the startScreen)
     */
    public static void hosting() {
        int serverPort = Integer.parseInt(JOptionPane.showInputDialog(startScreen, "Port:"));

        // Start the server with the given port
        try {
            new Thread(server = new Server(serverPort)).start();
        } catch (Exception e) {
            System.err.printf("Error starting server on port %d:%n", serverPort);
            throw new RuntimeException(e);
        }

        // Get the host IP, so the hostClient can connect to the server seamlessly
        String hostIp = null;
        try {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("Error getting Host IP Address:");
            throw new RuntimeException(e);
        }

        // initialize the client and PlayScreen in host mode
        client = new Client(hostIp, serverPort);
        PlayScreen n = new PlayScreen(client.receiveMessage(), 0);

        // "Listener" for server messages
        new Thread(new GameUpdater(n)).start();
    }

    /**
     * Starts the game in client mode
     * (this method is called from the startScreen)
     */
    public static void joining() {
        client = clientInitGUI();
        assert client != null;
        PlayScreen n = new PlayScreen(client.receiveMessage(), 1);
        new Thread(new GameUpdater(n)).start();
    }

    /**
     * Initializes the client interactively using a GUI.
     * The user is queried for server IP and port.
     * @return The initialized client.
     */
    private static Client clientInitGUI() {
        // creates elements for the JOptionPane to create a more or less reasonable Layout
        JTextField ipField = new JTextField(5);
        JTextField portField = new JTextField(5);
        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port:");

        // opens a JOptionPane with the Elements from above and checks if an input has
        // been made or if the JOptionPane was closed
        int result = JOptionPane.showConfirmDialog(startScreen, new Object[] { ipLabel, ipField, portLabel, portField },
                "Please Enter IP and Port of Server", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int port = Integer.parseInt(portField.getText());
            return new Client(ipField.getText(), port);
        } else {
            System.out.println("Client initialization cancelled by user, aborting...");
            System.exit(0);
            return null;
        }
    }

    // private inner class, to handle incoming messages from the server
    private static class GameUpdater implements Runnable {

        PlayScreen ui;

        public GameUpdater(PlayScreen ui) {
            this.ui = ui;
        }

        @Override
        public void run() {
            while (true) {
                ui.updateGame(Main.client.receiveMessage());
            }
        }
    }
}
