package com.example.vier_gewinnt_multiplayer;

import com.example.vier_gewinnt_multiplayer.network.BenutzerOberflaeche;
import com.example.vier_gewinnt_multiplayer.network.Client;
import com.example.vier_gewinnt_multiplayer.network.Server;
import com.example.vier_gewinnt_multiplayer.network.StartScreen;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;

public class Main {

    public static Server server;
    public static Client client;

    public static void main(String[] args) throws Exception {
        StartScreen s = new StartScreen();
    }

    public static void hosting() throws Exception{
        int serverPort = Integer.parseInt(JOptionPane.showInputDialog("Port:"));
        new Thread(server = new Server(serverPort)).start();
        String hostIp = InetAddress.getLocalHost().getHostAddress();
        client = new Client(hostIp, serverPort);
        BenutzerOberflaeche n = new BenutzerOberflaeche(client.receiveBoard(), 0);
        new Thread(new GameUpdater(n)).start();
    }

    public static void joining() throws IOException {
        client = clientInitGUI();
        assert client != null;
        BenutzerOberflaeche n = new BenutzerOberflaeche(client.receiveBoard(), 1);
        new Thread(new GameUpdater(n)).start();
    }

    private static class GameUpdater implements Runnable {

        BenutzerOberflaeche ui;

        public GameUpdater(BenutzerOberflaeche ui) {
            this.ui = ui;
        }

        @Override
        public void run() {
            try {
                while(true) {
                    ui.updateGame(Main.client.receiveBoard());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Client clientInitGUI() {
        // creates elements for the JOptionPane to create a more or less reasonable Layout
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
            return new Client(ipField.getText(), port);
        }

        return null;
    }
}
