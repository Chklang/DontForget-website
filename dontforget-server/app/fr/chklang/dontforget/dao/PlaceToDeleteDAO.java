package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.PlaceToDelete;

public class PlaceToDeleteDAO extends Finder<String, PlaceToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public PlaceToDeleteDAO() {
		super(String.class, PlaceToDelete.class);
	}
	
	public List<PlaceToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
	
	public void deleteOldObjects() {
//		this.query()
	}
}
