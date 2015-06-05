package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.CategoryToDelete;

public class CategoryToDeleteDAO extends Finder<String, CategoryToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public CategoryToDeleteDAO() {
		super(String.class, CategoryToDelete.class);
	}
	
	public List<CategoryToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
}
