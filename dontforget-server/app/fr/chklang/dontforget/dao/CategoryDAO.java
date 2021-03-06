package fr.chklang.dontforget.dao;

import java.util.Collection;
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
	
	public Collection<Category> findByLastUpdate(long pLastUpdate, User pUser) {
		return this.where().ge("lastUpdate", pLastUpdate).eq("user", pUser).findList();
	}
	
	public Category getByUuid(String pUuid) {
		return this.where().eq("uuid", pUuid).findUnique();
	}
}
