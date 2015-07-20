package de.norvos.gui;

public interface RegistrationHandler {

	/**
	 * Returns the received verification code that the user enters.
	 * 
	 * @return the verification code
	 */
	public String getCode();

	/**
	 * Notifies the user that the registration failed. The reason is found in
	 * the parameter.
	 * 
	 * @param message
	 *            the reason for the failure
	 */
	public void registrationFailed(String message);

}
