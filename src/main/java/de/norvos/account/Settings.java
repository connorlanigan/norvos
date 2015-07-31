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
package de.norvos.account;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.logging.Logger;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

public class Settings implements Observable {

	private static Settings instance = null;

	final private static String SEPARATOR_TAG = "\n";

	public static Settings getCurrent() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}

	public static void load(final byte[] bytes) throws IOException {
		final String serialized = new String(bytes, StandardCharsets.UTF_8);
		final String[] strings = serialized.split(SEPARATOR_TAG);
		Logger.debug("Loading settings: [Locale:" + strings[0] + "], [User:" + strings[1] + "], [password hidden]");
		getCurrent().setLocale(Locale.forLanguageTag(strings[0]));
		getCurrent().setServerAccount(new ServerAccount(strings[1], strings[2], strings[3]));
		getCurrent().setAxolotlStore(new NorvosAxolotlStore(DiskPersistence.load("axolotlStore")));

		getCurrent().setSetupFinished(true);
	}

	private Locale locale = Locale.US;

	final private NotificatorMap notifiables = new NotificatorMap();

	private ServerAccount serverAccount = null;
	private boolean setupFinished = false;

	private NorvosAxolotlStore store;

	private Settings() {
		store = new NorvosAxolotlStore();
		register(new DiskPersistence(), "settingsChange");
	}

	public NorvosAxolotlStore getAxolotlStore() {
		return store;
	}

	public Locale getLocale() {
		return locale;
	}

	public ServerAccount getServerAccount() {
		return serverAccount;
	}

	public boolean isSetupFinished() {
		return setupFinished;
	}

	private void notifyIfSetupFinished(final String event, final Object data) {
		if (setupFinished) {
			notifiables.notify(event, data);
		}
	}

	@Override
	public void register(final Notifiable n, final String event) {
		notifiables.register(event, n);
	}

	public byte[] serialize() throws IOException {
		final String serialized =
				locale.toLanguageTag() + SEPARATOR_TAG + serverAccount.getUsername() + SEPARATOR_TAG
						+ serverAccount.getPassword() + SEPARATOR_TAG + serverAccount.getSignalingKey();
		DiskPersistence.save("axolotlStore", store.serialize());
		return serialized.getBytes(StandardCharsets.UTF_8);
	}

	public void setAxolotlStore(final NorvosAxolotlStore store) {
		this.store = store;
	}

	public void setLocale(final Locale newLocale) {
		locale = newLocale;
		notifyIfSetupFinished("localeChange", locale);
		notifyIfSetupFinished("settingsChange", this);
	}

	public void setServerAccount(final ServerAccount account) {
		serverAccount = account;
	}

	public void setSetupFinished(final boolean finished) {
		setupFinished = finished;
		notifiables.notify("settingsChange", this);
		notifiables.notify("axolotlStoreChange", store);
	}

	@Override
	public void unregister(final Notifiable n) {
		notifiables.unregister(n);
	}
}
