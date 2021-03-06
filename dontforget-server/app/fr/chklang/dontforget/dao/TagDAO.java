package fr.chklang.dontforget.dao;

import java.util.Collection;
import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.User;

public class TagDAO extends Finder<Integer, Tag> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public TagDAO() {
		super(Integer.class, Tag.class);
	}
	
	public List<Tag> findByUser(User pUser) {
		return this.where().eq("user", pUser).findList();
	}
	
	public List<Tag> findByTagAndUser(User pUser, String pTag) {
		return this.where().eq("user", pUser).eq("name", pTag).findList();
	}
	
	public Collection<Tag> findByLastUpdate(long pLastUpdate, User pUser) {
		return this.where().ge("lastUpdate", pLastUpdate).eq("user", pUser).findList();
	}
	
	public Tag getByUuid(String pUuid) {
		return this.where().eq("uuid", pUuid).findUnique();
	}
}
