package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class BenutzerOberflaeche extends JFrame{
	
	private static int zuege = 0;
	private final int roll;
	
	/*
	 * Test
	public static void main(String[] args) {
		BenutzerOberflaeche  n = new BenutzerOberflaeche("xxxxxxx"
													   + "xxxxxxx"
													   + "xxxxxxx"
													   + "xxxxxxr"
													   + "xxxxgrr"
													   + "rxxgggr");
	}
	*/
	
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
	
	private void actionB(int i) {
		//do something
		if (zuege % 2 == roll) {
			System.out.println(i);
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
			b.addActionListener(e -> actionB(number));
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
