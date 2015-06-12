package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Update;

import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.User;

public class CategoryToDeleteDAO extends Finder<String, CategoryToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public CategoryToDeleteDAO() {
		super(String.class, CategoryToDelete.class);
	}
	
	public List<CategoryToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
	
	public void deleteOldObjects(User pUser) {
		Update<CategoryToDelete> lQuery = Ebean.createUpdate(CategoryToDelete.class, "DELETE FROM T_CATEGORY_TO_DELETE p WHERE p.idUser = :idUser AND p.dateDeletion < (SELECT MIN(t.lastUpdate) FROM T_TOKEN t WHERE t.idUser = :idUser)");
		lQuery.setParameter("idUser", pUser.getIdUser());
		lQuery.execute();
	}
}
