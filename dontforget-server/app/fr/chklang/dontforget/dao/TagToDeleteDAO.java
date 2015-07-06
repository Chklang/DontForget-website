package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Update;

import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.User;

public class TagToDeleteDAO extends Finder<String, TagToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public TagToDeleteDAO() {
		super(String.class, TagToDelete.class);
	}
	
	public List<TagToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
	
	public void deleteOldObjects(User pUser) {
		Update<TagToDelete> lQuery = Ebean.createUpdate(TagToDelete.class, "DELETE FROM T_TAG_TO_DELETE p WHERE p.idUser = :idUser AND p.dateDeletion < (SELECT MIN(t.lastUpdate) FROM T_TOKEN t WHERE t.idUser = :idUser)");
		lQuery.setParameter("idUser", pUser.getIdUser());
		lQuery.execute();
	}
}
