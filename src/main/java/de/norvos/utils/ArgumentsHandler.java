package de.norvos.utils;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.account.SettingsService;
import de.norvos.contacts.ContactData;
import de.norvos.i18n.AvailableLanguage;
import de.norvos.persistence.tables.ContactsTable;

public class ArgumentsHandler {
	public static final ArgumentsHandler INSTANCE = new ArgumentsHandler();

	private static final Logger LOGGER = LoggerFactory.getLogger(ArgumentsHandler.class);

	private boolean skipRegistration;

	private ArgumentsHandler() {
	}

	private void enableDebugMode() {
		LOGGER.debug(
				"Attention! Debug mode enabled! The displayed data may be modified and behaviour might be altered!");
		ContactsTable.getInstance()
				.storeContactData(new ContactData("+491788174362", "Connor", "", ContactData.ContactState.KNOWN_USER));
		ContactsTable.getInstance()
				.storeContactData(new ContactData("1", "Léanne", "", ContactData.ContactState.KNOWN_USER));
		ContactsTable.getInstance()
				.storeContactData(new ContactData("2", "Björn", "", ContactData.ContactState.KNOWN_USER));

	}

	public boolean getSkipRegistration() {
		return skipRegistration;
	}

	public void init(final String[] args) {
		final List<String> arguments = Arrays.asList(args);

		if (arguments.contains("debug")) {
			enableDebugMode();
		}
		if (arguments.contains("skipRegistration")) {
			skipRegistration = true;
		}

		if (arguments.contains("testTranslation")) {
			SettingsService.setLanguage(AvailableLanguage.TEST);
		} else if (SettingsService.getLanguage() == AvailableLanguage.TEST) {
			SettingsService.setLanguage(AvailableLanguage.getDefaultLanguage());
		}

	}

}
