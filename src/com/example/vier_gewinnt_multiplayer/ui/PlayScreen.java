package com.example.vier_gewinnt_multiplayer.ui;

import com.example.vier_gewinnt_multiplayer.Main;

import javax.swing.*;
import java.awt.*;

public class PlayScreen extends JFrame {

    private static int zuege = 0;
    private final int roll;

    // contains a String which represents the board: x -> nothing, r -> Player 1, g-> Player 2
    private String board;
    private final JButton[] bt = new JButton[7];
    private final JLabel[] lb = new JLabel[42];


    // Constructor
    public PlayScreen(String s, int roll) {
        this.roll = roll;
        this.board = s;
        createScreen();
        this.setVisible(true);
    }


    // gets the new board and updates the labels
    public void updateGame(String s) {
        if (s.length() != 42) { // if the message isn't the board
            int result = JOptionPane.showConfirmDialog(null, s, "Game Over", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION || result == JOptionPane.CANCEL_OPTION) {
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

    private void makeMove(int i) {
        // Check if it's the current user's turn
        if (zuege % 2 == roll) {
            Main.client.sendMove(i);
        }
    }

    // do some UI Stuff
    private void createScreen() {
        this.setTitle("4 Gewinnt");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new GridLayout(7,7));
        this.setSize(700, 700);
        this.getContentPane().setBackground(Color.white);


        for (int i = 0; i < 7; i++) {
            JButton b = new JButton();
            int number = i;
            b.addActionListener(e -> makeMove(number));
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
