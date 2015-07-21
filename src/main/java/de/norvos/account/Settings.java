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
import java.security.SecureRandom;
import java.util.Locale;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.log.Logger;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;
import de.norvos.persistence.DiskPersistence;

public class Settings implements Observable{

	final private NotificatorMap notifiables = new NotificatorMap();
	
	private Locale locale = Locale.US;
	private NorvosAxolotlStore store;
	private ServerAccount serverAccount = null;
	private boolean setupFinished = false;
	
	final private static String SEPARATOR_TAG = "\n";
	
	
	private static Settings instance = null;
	private Settings(){
		store = new NorvosAxolotlStore();
		register(new DiskPersistence(), "settingsChange");
	}
	
	public static Settings getCurrent() {
		if(instance == null){
			instance = new Settings();
		}
		return instance;
	}
	
	public Locale getLocale(){
		return locale;
	}

	public ServerAccount getServerAccount() {
		return serverAccount;
	}

	public NorvosAxolotlStore getAxolotlStore() {
		return store;
	}
	
	public void setSetupFinished(boolean finished){
		setupFinished = finished;
		notifiables.notify("settingsChange", this);
	}
	public boolean isSetupFinished(){
		return setupFinished;
	}
	
	public void setLocale(Locale newLocale){
		locale = newLocale;
		notifyIfSetupFinished("localeChange", locale);
		notifyIfSetupFinished("settingsChange", this);
	}

	public byte[] serialize() throws IOException{
		String serialized =  locale.toLanguageTag() + SEPARATOR_TAG + serverAccount.getUsername() + SEPARATOR_TAG + serverAccount.getPassword() + SEPARATOR_TAG + serverAccount.getSignalingKey();
		DiskPersistence.save("axolotlStore", store.serialize());
		return serialized.getBytes(StandardCharsets.UTF_8);
	}
	
	public static void load(byte[] bytes) throws IOException{
		String serialized = new String(bytes, StandardCharsets.UTF_8);
		String[] strings = serialized.split(SEPARATOR_TAG);
		Logger.debug("Loading settings: [Locale:"+strings[0]+"], [User:"+strings[1]+"], [password hidden]");
		getCurrent().setLocale(Locale.forLanguageTag(strings[0]));
		getCurrent().setServerAccount(new ServerAccount(strings[1], strings[2], strings[3]));
		getCurrent().setAxolotlStore(new NorvosAxolotlStore(DiskPersistence.load("axolotlStore")));
		
		getCurrent().setSetupFinished(true);
	}
	
	
	
	
	public void setAxolotlStore(NorvosAxolotlStore store){
		this.store = store;
	}
	
	public void setServerAccount(ServerAccount account){
		this.serverAccount = account;
	}
	
	
	private void notifyIfSetupFinished(String event, Object data){
		if(setupFinished){
			notifiables.notify(event, data);
		}
	}
	
	@Override
	public void register(Notifiable n, String event) {
		notifiables.register(event, n);
	}

	@Override
	public void unregister(Notifiable n) {
		notifiables.unregister(n);
	}
}
