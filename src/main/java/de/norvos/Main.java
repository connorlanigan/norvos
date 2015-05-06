package de.norvos;

import javax.swing.JOptionPane;


public class Main {
	public static void main(String[] args) {
		boolean loadedExistingStore = PlainAxolotlStoreManager.loadStore();
		if(!loadedExistingStore){
			PlainAxolotlStoreManager.saveStore();
			JOptionPane.showMessageDialog(null, "Hello! This is the first time youre using Norvos.\nWe've already prepared everything needed for secure communication.");
		}
		
		
	}

	public static void handleCriticalError(String message) {
		// TODO inform the user (e.g. via messagebox)
	}


}
