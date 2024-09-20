//XXX
package com.example.vier_gewinnt_multiplayer.network;

import com.example.vier_gewinnt_multiplayer.Main;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JFrame {

    public StartScreen () {
        this.setTitle("4 Gewinnt nicht");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new FlowLayout());
        this.setSize(600, 600);

        JLabel text = new JLabel();
        text.setText("Willkommen!");
        text.setHorizontalAlignment(JLabel.CENTER);
        text.setVerticalAlignment(JLabel.CENTER);
        text.setFont(new Font("", Font.BOLD, 70));
        text.setForeground(Color.black);

        JLabel text3 = new JLabel();
        text3.setText("                                      ");
        text3.setHorizontalAlignment(JLabel.CENTER);
        text3.setVerticalAlignment(JLabel.CENTER);
        text3.setFont(new Font("", Font.BOLD, 30));

        JLabel text4 = new JLabel();
        text4.setText("                                      ");
        text4.setHorizontalAlignment(JLabel.CENTER);
        text4.setVerticalAlignment(JLabel.CENTER);
        text4.setFont(new Font("", Font.BOLD, 30));

        JButton nSp2 = new JButton();
        nSp2.setText("Starte neues offenes Spiel");
        nSp2.setFont(new Font("", Font.BOLD, 30));
        nSp2.addActionListener(a -> {
            try {
                startG(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        JButton nSp3 = new JButton();
        nSp3.setText("Tritt  einem Spiel bei");
        nSp3.setFont(new Font("", Font.BOLD, 30));
        nSp3.addActionListener(a -> {
            try {
                startG(false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        this.add(text);
        this.add(text3);
        this.add(nSp2);
        this.add(text4);
        this.add(nSp3);
        this.setVisible(true);
    }

    private static void startG(boolean host) throws Exception {
        if (host) {
            Main.hosting();
        } else {
            Main.joining();
        }
    }

}
