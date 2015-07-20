package de.norvos.log;

import javax.swing.JOptionPane;

public class Errors {	
	public static void warning(String message){
		JOptionPane.showMessageDialog(null, message, "Norvos – Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void critical(String message){
		JOptionPane.showMessageDialog(null, message, "Norvos – Error", JOptionPane.ERROR_MESSAGE);
	}
}
