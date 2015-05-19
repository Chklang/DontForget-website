package fr.chklang.dontforget.dao;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Configuration;

public class ConfigurationDAO extends Finder<String, Configuration> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public ConfigurationDAO() {
		super(String.class, Configuration.class);
	}
}
