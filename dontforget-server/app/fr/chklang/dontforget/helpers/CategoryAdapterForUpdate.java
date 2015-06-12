/**
 * 
 */
package fr.chklang.dontforget.helpers;

import java.util.Set;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.CategoryDTO;
import fr.chklang.dontforget.helpers.SynchronizationHelper.AdapterForUpdate;

/**
 * @author Chklang
 *
 */
public class CategoryAdapterForUpdate implements AdapterForUpdate<Integer, Category, CategoryDTO> {
	
	@Override
	public Finder<Integer, Category> getDAO() {
		return Category.dao;
	}

	@Override
	public String getUuid(Category pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getUuid(CategoryDTO pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getName(Category pObject) {
		return pObject.getName();
	}

	@Override
	public String getName(CategoryDTO pObject) {
		return pObject.getName();
	}

	@Override
	public User getUser(Category pObject) {
		return pObject.getUser();
	}

	@Override
	public Set<Task> findByTAndUser(Category pObject, User pUser) {
		return Task.dao.findByCategoryAndUser(pObject, pUser);
	}

	@Override
	public void removeTFromTask(Task pTask, Category pObject) {
		//Nothing to do
	}

	@Override
	public void addTToTask(Task pTask, Category pObject) {
		pTask.setCategory(pObject);
	}

	@Override
	public void deleteT(String pUuid, int pIdUser) {
		CategoryToDelete lCategoryToDelete = new CategoryToDelete();
		lCategoryToDelete.setDateDeletion(System.currentTimeMillis());
		lCategoryToDelete.setUuidCategory(pUuid);
		lCategoryToDelete.setIdUser(pIdUser);
		lCategoryToDelete.save();
	}

	@Override
	public Category getFromDBByUuid(String pUuid) {
		return Category.dao.getByUuid(pUuid);
	}

	@Override
	public Category create(String pUuid, User pUser) {
		Category lCategory = new Category();
		lCategory.setUuid(pUuid);
		lCategory.setUser(pUser);
		return lCategory;
	}

	@Override
	public Category update(Category pObject, String pName) {
		pObject.setName(pName);
		return pObject;
	}
	


}
