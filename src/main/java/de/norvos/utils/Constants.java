/*******************************************************************************
 * Copyright (C) 2015 Connor Lanigan (email: dev@connorlanigan.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.norvos.utils;

import de.norvos.i18n.AvailableLanguage;

public class Constants {
	/**
	 * The default language to use, when the system language is not yet
	 * available in the application.
	 */
	public final static AvailableLanguage DEFAULT_LANGUAGE = AvailableLanguage.ENGLISH;
	/**
	 * The location where FXML files are stored.
	 */
	public final static String FXML_LOCATION = "/de/norvos/gui/view/";
	/**
	 * The URL displayed to end users where they can view the manual and get
	 * help.
	 */
	public static final String HELP_URL = "https://starfishinteractive.org/norvos/usermanual";
	/**
	 * The placeholder for untranslatable texts.
	 */
	public final static String I18N_ERROR = "{i18n-ERR}";
	public final static int SINGLETON_LOCK_PORT = 64308;

	public final static String USER_AGENT = "Norvos";

	/**
	 * The title for the main application window.
	 */
	public final static String WINDOW_TITLE = "Norvos";

	private Constants() {
	}

}
