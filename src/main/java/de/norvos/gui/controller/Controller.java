package de.norvos.gui.controller;

public abstract class Controller {

	private static Controller instance;

	public static Controller getInstance() {
		return instance;
	}

	public Controller() {
		instance = this;
	}

}
