/**
 * 
 */
package fr.chklang.dontforget.android.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.util.Pair;
import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.dto.CategoryDTO;
import fr.chklang.dontforget.android.dto.PlaceDTO;
import fr.chklang.dontforget.android.dto.SynchronizationDTO;
import fr.chklang.dontforget.android.dto.TagDTO;
import fr.chklang.dontforget.android.dto.TaskDTO;
import fr.chklang.dontforget.android.rest.AbstractRest.Result;
import fr.chklang.dontforget.android.rest.SynchronizationRest;

/**
 * @author S0075724
 *
 */
public class SynchronizationHelper {

	public static void startSynchronizations(Context pContext) {
		DatabaseManager.transaction(pContext, new DatabaseManager.Transaction() {
			@Override
			public void execute() {
				runStartSynchronizations();
			}
		});
	}
	
	private static void runStartSynchronizations() {
		Collection<Token> lTokens = Token.dao.getAll();
		Collection<Pair<Token, Result<SynchronizationDTO>>> lSynchronizationsPromesses = new ArrayList<Pair<Token, Result<SynchronizationDTO>>>();
		Collection<Pair<Token, SynchronizationDTO>> lSynchronizations = new ArrayList<Pair<Token, SynchronizationDTO>>();
		
		//First, update local storage with each declared servers
		for (Token lToken : lTokens) {
			Result<SynchronizationDTO> lSynchronizationPromesse = getSynchronizationDTO(lToken);
			lSynchronizationsPromesses.add(Pair.create(lToken, lSynchronizationPromesse));
		}
		
		for (Pair<Token, Result<SynchronizationDTO>> lEntry : lSynchronizationsPromesses) {
			SynchronizationDTO lSynchronizationDTO = lEntry.second.get();
			if (lSynchronizationDTO == null) {
				//Problem connection, ignore token
				continue;
			}
			lSynchronizations.add(Pair.create(lEntry.first, lSynchronizationDTO));
			updateLocalWithSynchronizationDTO(lSynchronizationDTO);
		}
		
		Collection<Pair<Token, Result<Boolean>>> lReUpdateServer = new ArrayList<Pair<Token, Result<Boolean>>>();
		//Now, update each servers with the updated data
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			SynchronizationDTO lSynchronizationDTOToSave = getUpdateServerWithSynchronizationDTO(lEntry.first.getLastSynchro(), lEntry.second);
			Result<Boolean> lPromesse = updateServer(lEntry.first, lSynchronizationDTOToSave);
			lReUpdateServer.add(Pair.create(lEntry.first, lPromesse));
		}

		for (Pair<Token, Result<Boolean>> lEntry : lReUpdateServer) {
			Boolean lResult = lEntry.second.get();
			if (lResult.booleanValue()) {
				//Update ok, save token
				lEntry.first.setLastSynchro(System.currentTimeMillis());
				Token.dao.save(lEntry.first);
			}
		}
	}
	
	private static Result<SynchronizationDTO> getSynchronizationDTO(Token pToken) {
		String lProtocol = pToken.getTokenKey().getProtocol();
		String lHost = pToken.getTokenKey().getHost();
		int lPort = pToken.getTokenKey().getPort();
		String lContext = pToken.getTokenKey().getContext();
		ServerConfiguration lServerConfiguration = new ServerConfiguration(lProtocol, lHost, lPort, lContext);
		return SynchronizationRest.connexion(lServerConfiguration, pToken.getLastSynchro());
	}
	
	private static Result<Boolean> updateServer(Token pToken, SynchronizationDTO pSynchronizationDTO) {
		String lProtocol = pToken.getTokenKey().getProtocol();
		String lHost = pToken.getTokenKey().getHost();
		int lPort = pToken.getTokenKey().getPort();
		String lContext = pToken.getTokenKey().getContext();
		ServerConfiguration lServerConfiguration = new ServerConfiguration(lProtocol, lHost, lPort, lContext);
		return SynchronizationRest.update(lServerConfiguration, pSynchronizationDTO);
	}
	
	private static void updateLocalWithSynchronizationDTO(SynchronizationDTO pSynchronizationDTO) {
		Collection<Category> lCategoriesIntoDBBeforeUpdate = Category.dao.getAll();
		
		SynchronizationDTO lUpdateServer = new SynchronizationDTO();
		
		for (CategoryDTO lCategoryDTO : pSynchronizationDTO.getCategories()) {
			boolean lIsFound = false;
			for (Category lCategory : lCategoriesIntoDBBeforeUpdate) {
				if (lCategory.getUuid().equals(lCategoryDTO.getUuid())) {
					//Update it
					lIsFound = true;
					lCategoriesIntoDBBeforeUpdate.remove(lCategory);
					
					//Update category
					if (lCategory.getLastUpdate() < lCategoryDTO.getLastUpdate()) {
						//Update local storage
						lCategory.setName(lCategoryDTO.getName().trim());
						lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
						Category.dao.save(lCategory);
					} else if (lCategory.getLastUpdate() > lCategoryDTO.getLastUpdate()) {
						//Update server storage
						lCategoryDTO = new CategoryDTO(lCategory.getName(), lCategory.getUuid(), lCategory.getLastUpdate());
						lUpdateServer.getCategories().add(lCategoryDTO);
					}
					
					break;
				}
			}
			if (!lIsFound) {
				//Create it
				Category lCategory = new Category();
				lCategory.setName(lCategoryDTO.getName().trim());
				lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
				lCategory.setUuid(lCategoryDTO.getUuid());
				Category.dao.save(lCategory);
			}
		}
		//TODO Delete old categories
		
		
		Collection<Tag> lTagsIntoDBBeforeUpdate = Tag.dao.getAll();
		
		for (TagDTO lTagDTO : pSynchronizationDTO.getTags()) {
			boolean lIsFound = false;
			for (Tag lTag : lTagsIntoDBBeforeUpdate) {
				if (lTag.getUuid().equals(lTagDTO.getUuid())) {
					//Update it
					lIsFound = true;
					lTagsIntoDBBeforeUpdate.remove(lTag);
					
					//Update Tag
					if (lTag.getLastUpdate() < lTagDTO.getLastUpdate()) {
						//Update local storage
						lTag.setName(lTagDTO.getName().trim());
						lTag.setLastUpdate(lTagDTO.getLastUpdate());
						Tag.dao.save(lTag);
					} else if (lTag.getLastUpdate() > lTagDTO.getLastUpdate()) {
						//Update server storage
						lTagDTO = new TagDTO(lTag.getName(), lTag.getUuid(), lTag.getLastUpdate());
						lUpdateServer.getTags().add(lTagDTO);
					}
					
					break;
				}
			}
			if (!lIsFound) {
				//Create it
				Tag lTag = new Tag();
				lTag.setName(lTagDTO.getName().trim());
				lTag.setLastUpdate(lTagDTO.getLastUpdate());
				lTag.setUuid(lTagDTO.getUuid());
				Tag.dao.save(lTag);
			}
		}
		//TODO Delete old tags
		
		
		Collection<Place> lPlacesIntoDBBeforeUpdate = Place.dao.getAll();
		
		for (PlaceDTO lPlaceDTO : pSynchronizationDTO.getPlaces()) {
			boolean lIsFound = false;
			for (Place lPlace : lPlacesIntoDBBeforeUpdate) {
				if (lPlace.getUuid().equals(lPlaceDTO.getUuid())) {
					//Update it
					lIsFound = true;
					lPlacesIntoDBBeforeUpdate.remove(lPlace);
					
					//Update Place
					if (lPlace.getLastUpdate() < lPlaceDTO.getLastUpdate()) {
						//Update local storage
						lPlace.setName(lPlaceDTO.getName().trim());
						lPlace.setLastUpdate(lPlaceDTO.getLastUpdate());
						Place.dao.save(lPlace);
					} else if (lPlace.getLastUpdate() > lPlaceDTO.getLastUpdate()) {
						//Update server storage
						lPlaceDTO = new PlaceDTO(lPlace.getName(), lPlace.getUuid(), lPlace.getLastUpdate());
						lUpdateServer.getPlaces().add(lPlaceDTO);
					}
					
					break;
				}
			}
			if (!lIsFound) {
				//Create it
				Place lPlace = new Place();
				lPlace.setName(lPlaceDTO.getName().trim());
				lPlace.setLastUpdate(lPlaceDTO.getLastUpdate());
				lPlace.setUuid(lPlaceDTO.getUuid());
				Place.dao.save(lPlace);
			}
		}
		//TODO Delete old places
		
		
		Collection<Task> lTasksIntoDBBeforeUpdate = Task.dao.getAll();
		
		for (TaskDTO lTaskDTO : pSynchronizationDTO.getTasks()) {
			boolean lIsFound = false;
			Task lTask = null;
			for (Task lTaskDB : lTasksIntoDBBeforeUpdate) {
				if (lTaskDB.getUuid().equals(lTaskDTO.getUuid())) {
					//Update it
					lIsFound = true;
					lTasksIntoDBBeforeUpdate.remove(lTaskDB);
					
					//Update Task
					if (lTaskDB.getLastUpdate() < lTaskDTO.getLastUpdate()) {
						lTask = lTaskDB;
						//Update local storage
						Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategoryUuid());
						if (lCategory == null) {
							//TODO Error, ignore task for the moment
							continue;
						}
						lTaskDB.setName(lTaskDTO.getText().trim());
						lTaskDB.setIdCategory(lCategory.getIdCategory());
						lTaskDB.setStatus(lTaskDTO.getStatus());
						lTaskDB.setLastUpdate(lTaskDTO.getLastUpdate());
						lTaskDB.setUuid(lTaskDTO.getUuid());
						Task.dao.save(lTaskDB);
					} else if (lTaskDB.getLastUpdate() > lTaskDTO.getLastUpdate()) {
						//Update server storage
						Category lCategory = Category.dao.get(lTaskDB.getIdCategory());
						if (lCategory == null) {
							//TODO Error, ignore task for the moment
							continue;
						}
						lTaskDTO = new TaskDTO(lTaskDB.getName(), lTaskDB.getStatus(), lCategory.getUuid(), lTaskDB.getUuid(), lTaskDB.getLastUpdate());
						lUpdateServer.getTasks().add(lTaskDTO);
					}
					
					break;
				}
			}
			if (!lIsFound) {
				//Create it
				lTask = new Task();
				Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategoryUuid());
				if (lCategory == null) {
					//TODO Error, ignore task for the moment
					continue;
				}
				lTask.setName(lTaskDTO.getText().trim());
				lTask.setIdCategory(lCategory.getIdCategory());
				lTask.setStatus(lTaskDTO.getStatus());
				lTask.setLastUpdate(lTaskDTO.getLastUpdate());
				lTask.setUuid(lTaskDTO.getUuid());
				Task.dao.save(lTask);
			}
			
			if (lTask == null) {
				//So we mustn't update it
				continue;
			}
			
			//Attach tags
			Collection<String> lTagsOfTask = new HashSet<String>();
			for (Tag lTag : Tag.dao.getTagsOfTask(lTask)) {
				lTagsOfTask.add(lTag.getUuid());
			}
			for (String lTagUUID : lTaskDTO.getTagUuids()) {
				Tag lTag = Tag.dao.getByUuid(lTagUUID);
				if (lTag == null) {
					//TODO This tag was deleted here
					continue;
				}
				if (!lTagsOfTask.contains(lTagUUID)) {
					Task.dao.addTagToTask(lTask, lTag);
				}
			}
			
			//Attach places
			Collection<String> lPlacesOfTask = new HashSet<String>();
			for (Place lPlace : Place.dao.getPlacesOfTask(lTask)) {
				lPlacesOfTask.add(lPlace.getUuid());
			}
			for (String lPlaceUUID : lTaskDTO.getPlaceUuids()) {
				Place lPlace = Place.dao.getByUuid(lPlaceUUID);
				if (lPlace == null) {
					//TODO This place was deleted here
					continue;
				}
				if (!lPlacesOfTask.contains(lPlaceUUID)) {
					Task.dao.addPlaceToTask(lTask, lPlace);
				}
			}
		}
		//TODO Delete old tasks
	}
	
	private static SynchronizationDTO getUpdateServerWithSynchronizationDTO(long pDateRef, SynchronizationDTO pOriginalSynchronizationDTO) {
		SynchronizationDTO lSynchronizationDTO = new SynchronizationDTO();
		
		Collection<Category> lCategoriesDB = Category.dao.findAfterLastUpdate(pDateRef);
		for (Category lCategory : lCategoriesDB) {
			boolean lMustAdd = true;
			for (CategoryDTO lCategoryDTO : pOriginalSynchronizationDTO.getCategories()) {
				if (lCategory.getUuid().equals(lCategoryDTO.getUuid())) {
					if (lCategory.getLastUpdate() > lCategoryDTO.getLastUpdate()) {
						lMustAdd = true;
					}
					break;
				}
			}
			if (lMustAdd) {
				CategoryDTO lCategoryDTO = new CategoryDTO(lCategory.getName(), lCategory.getUuid(), lCategory.getLastUpdate());
				lSynchronizationDTO.getCategories().add(lCategoryDTO);
			}
		}
		
		Collection<Tag> lTagsDB = Tag.dao.findAfterLastUpdate(pDateRef);
		for (Tag lTag : lTagsDB) {
			boolean lMustAdd = true;
			for (TagDTO lTagDTO : pOriginalSynchronizationDTO.getTags()) {
				if (lTag.getUuid().equals(lTagDTO.getUuid())) {
					if (lTag.getLastUpdate() > lTagDTO.getLastUpdate()) {
						lMustAdd = true;
					}
					break;
				}
			}
			if (lMustAdd) {
				TagDTO lTagDTO = new TagDTO(lTag.getName(), lTag.getUuid(), lTag.getLastUpdate());
				lSynchronizationDTO.getTags().add(lTagDTO);
			}
		}
		
		Collection<Place> lPlacesDB = Place.dao.findAfterLastUpdate(pDateRef);
		for (Place lPlace : lPlacesDB) {
			boolean lMustAdd = true;
			for (PlaceDTO lPlaceDTO : pOriginalSynchronizationDTO.getPlaces()) {
				if (lPlace.getUuid().equals(lPlaceDTO.getUuid())) {
					if (lPlace.getLastUpdate() > lPlaceDTO.getLastUpdate()) {
						lMustAdd = true;
					}
					break;
				}
			}
			if (lMustAdd) {
				PlaceDTO lPlaceDTO = new PlaceDTO(lPlace.getName(), lPlace.getUuid(), lPlace.getLastUpdate());
				lSynchronizationDTO.getPlaces().add(lPlaceDTO);
			}
		}
		return lSynchronizationDTO;
	}
}
