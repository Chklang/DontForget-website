package fr.chklang.dontforget.dao;

import java.util.Set;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.User;

public class CategoryDAO extends Finder<Integer, Category> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public CategoryDAO() {
		super(Integer.class, Category.class);
	}
	
	public Set<Category> findByUser(User pUser) {
		return this.where().eq("user", pUser).findSet();
	}
	
	public Category findByNameAndUser(String pName, User pUser) {
		return this.where().eq("name", pName).eq("user", pUser).findUnique();
	}
}
