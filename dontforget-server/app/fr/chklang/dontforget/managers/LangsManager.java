package fr.chklang.dontforget.managers;

import play.i18n.Lang;

public class LangsManager {

	private static final LangsManager INSTANCE = new LangsManager();
	
	private Lang defaultLang;
	
	private LangsManager() {
	}
	
	public Lang getDefaultLang() {
		return defaultLang;
	}

	public void setDefaultLang(Lang defaultLang) {
		this.defaultLang = defaultLang;
	}
	
	public void setDefaultLang(String pDefaultLang) {
		this.defaultLang = Lang.forCode(pDefaultLang);
	}

	public static LangsManager getInstance() {
		return INSTANCE;
	}
}
