package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.TagToDelete;

public class TagToDeleteDAO extends Finder<String, TagToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public TagToDeleteDAO() {
		super(String.class, TagToDelete.class);
	}
	
	public List<TagToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
}
