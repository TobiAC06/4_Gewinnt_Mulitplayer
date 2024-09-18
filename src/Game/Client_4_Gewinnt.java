package Game;

import javax.swing.*;

public class Client_4_Gewinnt {

	public static void main(String args[]) {
		connectToServer();
	}

	//connects to a Server with a IP and Port given by the User
	public static void connectToServer() {
		
		// creates elements for the JOptionPane to create a more or less reasonable Layout
		JTextField ipField = new JTextField(5);
		JTextField portField= new JTextField(5);
		JLabel ipLabel = new JLabel("IP:");
		JLabel portLabel = new JLabel("Port:");
		
		//opens a JOptionPane with the Elements from above and checks if a input has been made or if the JOptionPane was closed
		int result = JOptionPane.showConfirmDialog(null, new Object[] {ipLabel, ipField, portLabel,portField}, 
	               "Please Enter IP and Port of Server", JOptionPane.OK_CANCEL_OPTION);
	      if (result == JOptionPane.OK_OPTION) {
	         System.out.println("IP-Adresse: " + ipField.getText());
	         System.out.println("Port: " + portField.getText());
	      }
	      
	}
}
