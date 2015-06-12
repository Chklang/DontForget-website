/**
 * 
 */
package fr.chklang.dontforget.helpers;

import java.util.Set;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.TagDTO;
import fr.chklang.dontforget.helpers.SynchronizationHelper.AdapterForUpdate;

/**
 * @author Chklang
 *
 */
public class TagAdapterForUpdate implements AdapterForUpdate<Integer, Tag, TagDTO> {
	
	@Override
	public Finder<Integer, Tag> getDAO() {
		return Tag.dao;
	}

	@Override
	public String getUuid(Tag pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getUuid(TagDTO pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getName(Tag pObject) {
		return pObject.getName();
	}

	@Override
	public String getName(TagDTO pObject) {
		return pObject.getName();
	}

	@Override
	public User getUser(Tag pObject) {
		return pObject.getUser();
	}

	@Override
	public Set<Task> findByTAndUser(Tag pObject, User pUser) {
		return Task.dao.findByTagAndUser(pObject, pUser);
	}

	@Override
	public void removeTFromTask(Task pTask, Tag pObject) {
		pTask.getTags().remove(pObject);
	}

	@Override
	public void addTToTask(Task pTask, Tag pObject) {
		pTask.getTags().add(pObject);
	}

	@Override
	public void deleteT(String pUuid, int pIdUser) {
		TagToDelete lTagToDelete = new TagToDelete();
		lTagToDelete.setDateDeletion(System.currentTimeMillis());
		lTagToDelete.setUuidTag(pUuid);
		lTagToDelete.setIdUser(pIdUser);
		lTagToDelete.save();
	}

	@Override
	public Tag getFromDBByUuid(String pUuid) {
		return Tag.dao.getByUuid(pUuid);
	}

	@Override
	public Tag create(String pUuid, User pUser) {
		Tag lTag = new Tag();
		lTag.setUuid(pUuid);
		lTag.setUser(pUser);
		return lTag;
	}

	@Override
	public Tag update(Tag pObject, String pName) {
		pObject.setName(pName);
		return pObject;
	}
	


}
