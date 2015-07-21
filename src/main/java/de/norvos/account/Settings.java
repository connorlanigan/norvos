package de.norvos.account;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.log.Logger;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;
import de.norvos.persistence.DiskPersistence;

public class Settings implements Observable{

	private NotificatorMap notifiables = new NotificatorMap();
	private Locale locale = Locale.US;
	private NorvosAxolotlStore store = null;
	private ServerAccount serverAccount = null;
	private boolean setupFinished = false;
	
	private static String SEPARATOR_TAG = "\n";
	
	
	private static Settings instance = null;
	private Settings(){
		register(new DiskPersistence(), "settingsChange");
	}
	
	public static Settings getCurrent() {
		if(instance == null){
			instance = new Settings();
			instance.store = new NorvosAxolotlStore();
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

	public byte[] serialize() {
		String serialized =  locale.toLanguageTag() + SEPARATOR_TAG + serverAccount.getUsername() + SEPARATOR_TAG + serverAccount.getPassword();
		return serialized.getBytes(StandardCharsets.UTF_8);
	}	

	public static void load(byte[] bytes) throws IOException{
		String serialized = new String(bytes, StandardCharsets.UTF_8);
		String[] strings = serialized.split(SEPARATOR_TAG);
		Logger.debug("Loading settings: [Locale:"+strings[0]+"], [User:"+strings[1]+"], [password hidden]");
		getCurrent().setLocale(Locale.forLanguageTag(strings[0]));
		getCurrent().setServerAccount(new ServerAccount(strings[1], strings[2]));
		getCurrent().setSetupFinished(true);
		getCurrent().setAxolotlStore(new NorvosAxolotlStore(DiskPersistence.load("axolotlStore")));
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
