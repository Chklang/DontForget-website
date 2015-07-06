package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Update;

import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.User;

public class PlaceToDeleteDAO extends Finder<String, PlaceToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public PlaceToDeleteDAO() {
		super(String.class, PlaceToDelete.class);
	}
	
	public List<PlaceToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
	
	public void deleteOldObjects(User pUser) {
		Update<PlaceToDelete> lQuery = Ebean.createUpdate(PlaceToDelete.class, "DELETE FROM T_PLACE_TO_DELETE p WHERE p.idUser = :idUser AND p.dateDeletion < (SELECT MIN(t.lastUpdate) FROM T_TOKEN t WHERE t.idUser = :idUser)");
		lQuery.setParameter("idUser", pUser.getIdUser());
		lQuery.execute();
	}
}
