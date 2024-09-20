package com.example.vier_gewinnt_multiplayer.network;

import com.example.vier_gewinnt_multiplayer.Main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class BenutzerOberflaeche extends JFrame {

    private static int zuege = 0;
    private final int roll;

    //contains a String which represents the board: x -> nothing, r -> Player 1, g-> Player 2
    private String board;
    JButton[] bt = new JButton[7];
    JLabel[] lb = new JLabel[42];


    //Constructor
    public BenutzerOberflaeche (String s, int roll) {
        this.roll = roll;
        this.board = s;
        createTheScreen();
        this.setVisible(true);
    }


    //gets the new board and updates the lables
    public void updateGame(String s) {
        if (s.length() != 42) {
            int result = JOptionPane.showConfirmDialog(null, s, JOptionPane.OK_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
            return;
        }
        zuege++;
        this.board = s;
        for (int i = 0; i < board.length(); i++) {
            if (board.charAt(i) == 'g') {
                lb[i].setBackground(new Color(0xFFFF00));
            } else if(board.charAt(i) == 'r') {
                lb[i].setBackground(new Color(0xFF0000));
            }
        }
    }

    private void actionB(int i) throws IOException, InterruptedException {
        //do something
        if (true || zuege % 2 == roll) {
            System.out.println(i);
            Main.client.sendMove(i);
        }
    }

    //do some UI Stuff
    private void createTheScreen() {
        this.setTitle("4 Gewinnt");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new GridLayout(7,7));
        this.setSize(700, 700);
        this.getContentPane().setBackground(Color.white);


        for (int i = 0; i < 7; i++) {
            JButton b = new JButton();
            int number = i;
            b.addActionListener(e -> {
                try {
                    actionB(number);
                } catch (IOException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            });
            b.setBackground(new Color(0xFFFFFF));
            b.setText("↓");
            b.setFont(new Font("", Font.BOLD, 50));
            bt[i] = b;
            this.add(bt[i]);
        }

        for (int i = 0; i < board.length(); i++) {
            JLabel f = new JLabel();
            f.setOpaque(true);
            if (board.charAt(i) == 'g') {
                f.setBackground(new Color(0xFFFF00));
            } else if(board.charAt(i) == 'r') {
                f.setBackground(new Color(0xFF0000));
            }
            f.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            lb[i] = f;
            this.add(lb[i]);
        }
    }
}
