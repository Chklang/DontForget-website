/**
 * 
 */
package fr.chklang.dontforget.helpers;

import java.util.Set;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.PlaceDTO;
import fr.chklang.dontforget.helpers.SynchronizationHelper.AdapterForUpdate;

/**
 * @author Chklang
 *
 */
public class PlaceAdapterForUpdate implements AdapterForUpdate<Integer, Place, PlaceDTO> {
	
	@Override
	public Finder<Integer, Place> getDAO() {
		return Place.dao;
	}

	@Override
	public String getUuid(Place pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getUuid(PlaceDTO pObject) {
		return pObject.getUuid();
	}

	@Override
	public String getName(Place pObject) {
		return pObject.getName();
	}

	@Override
	public String getName(PlaceDTO pObject) {
		return pObject.getName();
	}

	@Override
	public User getUser(Place pObject) {
		return pObject.getUser();
	}

	@Override
	public Set<Task> findByTAndUser(Place pObject, User pUser) {
		return Task.dao.findByPlaceAndUser(pObject, pUser);
	}

	@Override
	public void removeTFromTask(Task pTask, Place pObject) {
		pTask.getPlaces().remove(pObject);
	}

	@Override
	public void addTToTask(Task pTask, Place pObject) {
		pTask.getPlaces().add(pObject);
	}

	@Override
	public void deleteT(String pUuid, int pIdUser) {
		PlaceToDelete lPlaceToDelete = new PlaceToDelete();
		lPlaceToDelete.setDateDeletion(System.currentTimeMillis());
		lPlaceToDelete.setUuidPlace(pUuid);
		lPlaceToDelete.setIdUser(pIdUser);
		lPlaceToDelete.save();
	}

	@Override
	public Place getFromDBByUuid(String pUuid) {
		return Place.dao.getByUuid(pUuid);
	}

	@Override
	public Place create(String pUuid, User pUser) {
		Place lPlace = new Place();
		lPlace.setUuid(pUuid);
		lPlace.setUser(pUser);
		return lPlace;
	}

	@Override
	public Place update(Place pObject, String pName) {
		pObject.setName(pName);
		return pObject;
	}
	


}
