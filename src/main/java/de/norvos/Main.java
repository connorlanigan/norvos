package de.norvos;


public class Main {
	public static void main(String[] args) {
		boolean loadedExistingStore = PlainAxolotlStoreContainer.loadStore();
		if(!loadedExistingStore){
			// TODO first use, show message/tutorial
		}
		
		
	}

	public static void handleCriticalError(String message) {
		// TODO inform the user (e.g. via messagebox)
	}

}
