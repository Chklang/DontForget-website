package fr.chklang.dontforget.dao;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.User;

public class UserDAO extends Finder<Integer, User> {

	public UserDAO() {
		super(Integer.class, User.class);
	}
	
	public User findByPseudo(String pPseudo) {
		return this.where().eq("pseudo", pPseudo).findUnique();
	}
}
