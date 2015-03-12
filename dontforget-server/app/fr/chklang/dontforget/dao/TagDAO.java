package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.User;

public class TagDAO extends Finder<Integer, Tag> {

	public TagDAO() {
		super(Integer.class, Tag.class);
	}
	
	public List<Tag> findByUser(User pUser) {
		return this.where().eq("user", pUser).findList();
	}
	
	public Tag findByTagAndUser(User pUser, String pTag) {
		return this.where().eq("user", pUser).eq("tag", pTag).findUnique();
	}
}
