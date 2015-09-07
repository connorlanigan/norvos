package de.norvos.i18n;

import java.util.Locale;

public enum Language {
	CHINESE("Chinese (Simplified)", Locale.SIMPLIFIED_CHINESE),
	ENGLISH("English", Locale.ENGLISH);

	private Locale code;
	private String displayName;

	Language(String name, Locale locale) {
		this.code = locale;
	}

	public Locale getLocale(){
		return code;
	}

	@Override
	public String toString(){
		return displayName;
	}

	public static Language forLocale(Locale locale){
		for(Language language : Language.values()){
			if(language.getLocale().equals(locale)) return language;
		}
		return ENGLISH;
	}
}
