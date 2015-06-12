/**
 * 
 */
package fr.chklang.dontforget.android.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.util.Pair;
import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.CategoryToDelete;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.PlaceToDelete;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.TagToDelete;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.business.TaskToDelete;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.dto.CategoryDTO;
import fr.chklang.dontforget.android.dto.PlaceDTO;
import fr.chklang.dontforget.android.dto.SynchronizationDTO;
import fr.chklang.dontforget.android.dto.TagDTO;
import fr.chklang.dontforget.android.dto.TaskDTO;
import fr.chklang.dontforget.android.rest.AbstractRest.Result;
import fr.chklang.dontforget.android.rest.SynchronizationRest;
import fr.chklang.dontforget.android.rest.TokensRest;

/**
 * @author S0075724
 *
 */
public class SynchronizationHelper {

	public static void startSynchronizations(Context pContext, final Runnable pCallback) {
		DatabaseManager.transaction(pContext, new DatabaseManager.Transaction() {
			@Override
			public void execute() {
				runStartSynchronizations();
			}
		});
		pCallback.run();
	}

	private static void runStartSynchronizations() {
		final long currentTime = System.currentTimeMillis();
		
		Collection<Token> lTokens = Token.dao.getAll();
		Collection<Pair<Token, Result<SynchronizationDTO>>> lSynchronizationsPromesses = new ArrayList<Pair<Token, Result<SynchronizationDTO>>>();
		Collection<Pair<Token, SynchronizationDTO>> lSynchronizations = new ArrayList<Pair<Token, SynchronizationDTO>>();
		Map<Token, ServerConfiguration> lTokensWithServersConfiguration = new HashMap<Token, ServerConfiguration>();

		// Connect to servers
		for (Token lToken : lTokens) {
			ServerConfiguration lServerConfiguration = lToken.toServerConfiguration();
			Result<Boolean> lResult = TokensRest.connexion(lServerConfiguration, lToken, ConfigurationHelper.getDeviceId());
			if (lResult.get().booleanValue()) {
				lTokensWithServersConfiguration.put(lToken, lServerConfiguration);
			}
		}
		
		// First, update local storage with each declared servers
		for (Entry<Token, ServerConfiguration> lEntry : lTokensWithServersConfiguration.entrySet()) {
			Token lToken = lEntry.getKey();
			Result<SynchronizationDTO> lSynchronizationPromesse = getSynchronizationDTO(lToken, lEntry.getValue());
			lSynchronizationsPromesses.add(Pair.create(lToken, lSynchronizationPromesse));
		}

		for (Pair<Token, Result<SynchronizationDTO>> lEntry : lSynchronizationsPromesses) {
			SynchronizationDTO lSynchronizationDTO = lEntry.second.get();
			if (lSynchronizationDTO == null) {
				// Problem connection, ignore token
				continue;
			}
			lSynchronizations.add(Pair.create(lEntry.first, lSynchronizationDTO));
		}
		
		//Update categories, tags and places
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			updateLocalWithSynchronizationDTOCategoriesTagsAndPlaces(lEntry.second);
		}
		
		//Update tasks
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			updateLocalWithSynchronizationDTOTasks(lEntry.second);
		}

		// Delete old tasks
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			updateLocalWithSynchronizationDTODeleteTasks(lEntry.first, lEntry.second, currentTime);
		}

		// Delete old tags, places and categories
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			updateLocalWithSynchronizationDTODeleteTagsPlacesAndCategories(lEntry.first, lEntry.second, currentTime);
		}

		Collection<Pair<Token, Result<Boolean>>> lReUpdateServer = new ArrayList<Pair<Token, Result<Boolean>>>();
		// Now, update each servers with the updated data
		for (Pair<Token, SynchronizationDTO> lEntry : lSynchronizations) {
			SynchronizationDTO lSynchronizationDTOToSave = getUpdateServerWithSynchronizationDTO(lEntry.first, lEntry.second);
			Result<Boolean> lPromesse = updateServer(lEntry.first, lSynchronizationDTOToSave);
			lReUpdateServer.add(Pair.create(lEntry.first, lPromesse));
		}

		for (Pair<Token, Result<Boolean>> lEntry : lReUpdateServer) {
			Boolean lResult = lEntry.second.get();
			if (lResult.booleanValue()) {
				//Update the token
				lEntry.first.setLastSynchro(currentTime);
				Token.dao.save(lEntry.first);
				//Send update date
				ServerConfiguration lServerConfiguration = lEntry.first.toServerConfiguration();
				TokensRest.sendUpdateToken(lServerConfiguration, currentTime);
			}
		}
		
		
		//Delete old entries into logs
		cleanObjectsDeleted();
	}

	private static Result<SynchronizationDTO> getSynchronizationDTO(Token pToken, ServerConfiguration pServerConfiguration) {
		return SynchronizationRest.connexion(pServerConfiguration, pToken.getLastSynchro());
	}

	private static Result<Boolean> updateServer(Token pToken, SynchronizationDTO pSynchronizationDTO) {
		String lProtocol = pToken.getProtocol();
		String lHost = pToken.getHost();
		int lPort = pToken.getPort();
		String lContext = pToken.getContext();
		ServerConfiguration lServerConfiguration = new ServerConfiguration(lProtocol, lHost, lPort, lContext);
		return SynchronizationRest.update(lServerConfiguration, pSynchronizationDTO);
	}

	private static void updateLocalWithSynchronizationDTOCategoriesTagsAndPlaces(SynchronizationDTO pSynchronizationDTO) {
		// Update/create categories
		for (CategoryDTO lCategoryDTO : pSynchronizationDTO.getCategories()) {
			Category lCategory = Category.dao.getByUuid(lCategoryDTO.getUuid());
			if (lCategory == null) {
				// Create it
				lCategory = new Category();
				lCategory.setName(lCategoryDTO.getName().trim());
				lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
				lCategory.setUuid(lCategoryDTO.getUuid());
				Category.dao.save(lCategory);
			} else {
				// Update it
				if (lCategory.getLastUpdate() < lCategoryDTO.getLastUpdate()) {
					// Update local storage
					lCategory.setName(lCategoryDTO.getName().trim());
					lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
					Category.dao.save(lCategory);
				}
			}
		}

		// Update/create tags
		for (TagDTO lTagDTO : pSynchronizationDTO.getTags()) {
			Tag lTag = Tag.dao.getByUuid(lTagDTO.getUuid());
			if (lTag == null) {
				// Create it
				lTag = new Tag();
				lTag.setName(lTagDTO.getName().trim());
				lTag.setLastUpdate(lTagDTO.getLastUpdate());
				lTag.setUuid(lTagDTO.getUuid());
				Tag.dao.save(lTag);
			} else {
				// Update it
				if (lTag.getLastUpdate() < lTagDTO.getLastUpdate()) {
					// Update local storage
					lTag.setName(lTagDTO.getName().trim());
					lTag.setLastUpdate(lTagDTO.getLastUpdate());
					Tag.dao.save(lTag);
				}
			}
		}

		// Update/create places
		for (PlaceDTO lPlaceDTO : pSynchronizationDTO.getPlaces()) {
			Place lPlace = Place.dao.getByUuid(lPlaceDTO.getUuid());
			if (lPlace == null) {
				// Create it
				lPlace = new Place();
				lPlace.setName(lPlaceDTO.getName().trim());
				lPlace.setLastUpdate(lPlaceDTO.getLastUpdate());
				lPlace.setUuid(lPlaceDTO.getUuid());
				Place.dao.save(lPlace);
			} else {
				// Update it
				if (lPlace.getLastUpdate() < lPlaceDTO.getLastUpdate()) {
					// Update local storage
					lPlace.setName(lPlaceDTO.getName().trim());
					lPlace.setLastUpdate(lPlaceDTO.getLastUpdate());
					Place.dao.save(lPlace);
				}
			}
		}

	}

	private static void updateLocalWithSynchronizationDTOTasks(SynchronizationDTO pSynchronizationDTO) {
		// Update/create tasks
		for (TaskDTO lTaskDTO : pSynchronizationDTO.getTasks()) {
			boolean lIsUpdateLinks = false;
			Task lTask = Task.dao.getByUuid(lTaskDTO.getUuid());
			if (lTask == null) {
				// Create it
				lTask = new Task();
				Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategoryUuid());
				if (lCategory == null) {
					// TODO Error, ignore task for the moment
					continue;
				}
				lTask.setName(lTaskDTO.getText().trim());
				lTask.setIdCategory(lCategory.getIdCategory());
				lTask.setStatus(lTaskDTO.getStatus());
				lTask.setLastUpdate(lTaskDTO.getLastUpdate());
				lTask.setUuid(lTaskDTO.getUuid());
				Task.dao.save(lTask);
				lIsUpdateLinks = true;
			} else {
				// Update it
				if (lTask.getLastUpdate() < lTaskDTO.getLastUpdate()) {
					// Update local storage
					Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategoryUuid());
					if (lCategory == null) {
						// TODO Error, ignore task for the moment
						continue;
					}
					lTask.setName(lTaskDTO.getText().trim());
					lTask.setIdCategory(lCategory.getIdCategory());
					lTask.setStatus(lTaskDTO.getStatus());
					lTask.setLastUpdate(lTaskDTO.getLastUpdate());
					lTask.setUuid(lTaskDTO.getUuid());
					Task.dao.save(lTask);
					lIsUpdateLinks = true;
				}
			}

			if (lIsUpdateLinks) {
				// So we mustn't update it
				continue;
			}

			// Attach tags
			Collection<String> lTagsOfTask = new HashSet<String>();
			for (Tag lTag : Tag.dao.getTagsOfTask(lTask)) {
				lTagsOfTask.add(lTag.getUuid());
			}
			for (String lTagUUID : lTaskDTO.getTagUuids()) {
				Tag lTag = Tag.dao.getByUuid(lTagUUID);
				if (lTag == null) {
					// TODO This tag was deleted here
					// This problem must never comes because task must be more
					// recent on server than local storage
					continue;
				}
				if (!lTagsOfTask.contains(lTagUUID)) {
					Task.dao.addTagToTask(lTask, lTag);
				}
			}

			// Attach places
			Collection<String> lPlacesOfTask = new HashSet<String>();
			for (Place lPlace : Place.dao.getPlacesOfTask(lTask)) {
				lPlacesOfTask.add(lPlace.getUuid());
			}
			for (String lPlaceUUID : lTaskDTO.getPlaceUuids()) {
				Place lPlace = Place.dao.getByUuid(lPlaceUUID);
				if (lPlace == null) {
					// This place was deleted here
					// This problem must never comes because task must be more
					// recent on server than local storage
					continue;
				}
				if (!lPlacesOfTask.contains(lPlaceUUID)) {
					Task.dao.addPlaceToTask(lTask, lPlace);
				}
			}
		}
	}

	private static void updateLocalWithSynchronizationDTODeleteTasks(Token pToken, SynchronizationDTO pSynchronizationDTO, long currentTime) {
		for (String lIdTaskToDelete : pSynchronizationDTO.getUuidTasksToDelete()) {
			Task lTask = Task.dao.getByUuid(lIdTaskToDelete);
			if (lTask != null && lTask.getLastUpdate() < pToken.getLastSynchro()) {
				TaskToDelete lTaskToDelete = new TaskToDelete();
				lTaskToDelete.setUuidTask(lTask.getUuid());
				lTaskToDelete.setDateDeletion(currentTime);
				TaskToDelete.dao.create(lTaskToDelete);
				Task.dao.delete(lTask.getIdTask());
			}
		}
	}

	private static void updateLocalWithSynchronizationDTODeleteTagsPlacesAndCategories(Token pToken, SynchronizationDTO pSynchronizationDTO, long currentTime) {
		for (String lIdTagToDelete : pSynchronizationDTO.getUuidTagsToDelete()) {
			Tag lTag = Tag.dao.getByUuid(lIdTagToDelete);
			if (lTag != null && lTag.getLastUpdate() < pToken.getLastSynchro()) {
				if (Task.dao.findByTag(lTag).isEmpty()) {
					TagToDelete lTagToDelete = new TagToDelete();
					lTagToDelete.setUuidTag(lTag.getUuid());
					lTagToDelete.setDateDeletion(currentTime);
					TagToDelete.dao.create(lTagToDelete);
					Tag.dao.delete(lTag.getIdTag());
				}
			}
		}
		for (String lIdPlaceToDelete : pSynchronizationDTO.getUuidPlacesToDelete()) {
			Place lPlace = Place.dao.getByUuid(lIdPlaceToDelete);
			if (lPlace != null && lPlace.getLastUpdate() < pToken.getLastSynchro()) {
				if (Task.dao.findByPlace(lPlace).isEmpty()) {
					PlaceToDelete lPlaceToDelete = new PlaceToDelete();
					lPlaceToDelete.setUuidPlace(lPlace.getUuid());
					lPlaceToDelete.setDateDeletion(currentTime);
					PlaceToDelete.dao.create(lPlaceToDelete);
					Place.dao.delete(lPlace.getIdPlace());
				}
			}
		}
		for (String lIdCategoryToDelete : pSynchronizationDTO.getUuidCategoriesToDelete()) {
			Category lCategory = Category.dao.getByUuid(lIdCategoryToDelete);
			if (lCategory != null && lCategory.getLastUpdate() < pToken.getLastSynchro()) {
				if (Task.dao.findByCategory(lCategory).isEmpty()) {
					CategoryToDelete lCategoryToDelete = new CategoryToDelete();
					lCategoryToDelete.setUuidCategory(lCategory.getUuid());
					lCategoryToDelete.setDateDeletion(currentTime);
					CategoryToDelete.dao.create(lCategoryToDelete);
					Category.dao.delete(lCategory.getIdCategory());
				}
			}
		}
	}

	private static SynchronizationDTO getUpdateServerWithSynchronizationDTO(Token pToken, SynchronizationDTO pOriginalSynchronizationDTO) {
		long lDateRef = pToken.getLastSynchro();
		SynchronizationDTO lSynchronizationDTO = new SynchronizationDTO();

		for (Category lCategoryLocal : Category.dao.findAfterLastUpdate(lDateRef)) {
			boolean lMustAdd = true;
			for (CategoryDTO lCategoryDTO : pOriginalSynchronizationDTO.getCategories()) {
				if (lCategoryLocal.getUuid().equals(lCategoryDTO.getUuid())) {
					if (lCategoryLocal.getLastUpdate() < lCategoryDTO.getLastUpdate()) {
						lMustAdd = false;
					}
					break;
				}
			}
			if (lMustAdd) {
				CategoryDTO lCategoryDTO = new CategoryDTO(lCategoryLocal.getName(), lCategoryLocal.getUuid(), lCategoryLocal.getLastUpdate());
				lSynchronizationDTO.getCategories().add(lCategoryDTO);
			}
		}

		for (Tag lTagLocal : Tag.dao.findAfterLastUpdate(lDateRef)) {
			boolean lMustAdd = true;
			for (TagDTO lTagDTO : pOriginalSynchronizationDTO.getTags()) {
				if (lTagLocal.getUuid().equals(lTagDTO.getUuid())) {
					if (lTagLocal.getLastUpdate() < lTagDTO.getLastUpdate()) {
						lMustAdd = false;
					}
					break;
				}
			}
			if (lMustAdd) {
				TagDTO lTagDTO = new TagDTO(lTagLocal.getName(), lTagLocal.getUuid(), lTagLocal.getLastUpdate());
				lSynchronizationDTO.getTags().add(lTagDTO);
			}
		}

		for (Place lPlaceLocal : Place.dao.findAfterLastUpdate(lDateRef)) {
			boolean lMustAdd = true;
			for (PlaceDTO lPlaceDTO : pOriginalSynchronizationDTO.getPlaces()) {
				if (lPlaceLocal.getUuid().equals(lPlaceDTO.getUuid())) {
					if (lPlaceLocal.getLastUpdate() < lPlaceDTO.getLastUpdate()) {
						lMustAdd = true;
					}
					break;
				}
			}
			if (lMustAdd) {
				PlaceDTO lPlaceDTO = new PlaceDTO(lPlaceLocal.getName(), lPlaceLocal.getUuid(), lPlaceLocal.getLastUpdate());
				lSynchronizationDTO.getPlaces().add(lPlaceDTO);
			}
		}

		for (Task lTaskLocal : Task.dao.findAfterLastUpdate(lDateRef)) {
			boolean lMustAdd = true;
			for (TaskDTO lTaskDTO : pOriginalSynchronizationDTO.getTasks()) {
				if (lTaskLocal.getUuid().equals(lTaskDTO.getUuid())) {
					if (lTaskLocal.getLastUpdate() < lTaskDTO.getLastUpdate()) {
						lMustAdd = true;
					}
					break;
				}
			}
			if (lMustAdd) {
				Category lCategory = Category.dao.get(lTaskLocal.getIdCategory());
				if (lCategory == null) {
					//TODO case very strange!
					continue;
				}
				TaskDTO lTaskDTO = new TaskDTO(lTaskLocal.getName(), lTaskLocal.getStatus(), lCategory.getUuid(), lTaskLocal.getUuid(), lTaskLocal.getLastUpdate());
				for (Tag lTagLinked : Tag.dao.getTagsOfTask(lTaskLocal)) {
					lTaskDTO.getTagUuids().add(lTagLinked.getUuid());
				}
				for (Place lPlaceLinked : Place.dao.getPlacesOfTask(lTaskLocal)) {
					lTaskDTO.getPlaceUuids().add(lPlaceLinked.getUuid());
				}
				lSynchronizationDTO.getTasks().add(lTaskDTO);
			}
		}

		for (TaskToDelete lTaskToDelete : TaskToDelete.dao.findAfterDate(pToken.getLastSynchro())) {
			lSynchronizationDTO.getUuidTasksToDelete().add(lTaskToDelete.getUuidTask());
		}

		for (TagToDelete lTagToDelete : TagToDelete.dao.findAfterDate(pToken.getLastSynchro())) {
			lSynchronizationDTO.getUuidTagsToDelete().add(lTagToDelete.getUuidTag());
		}

		for (PlaceToDelete lPlaceToDelete : PlaceToDelete.dao.findAfterDate(pToken.getLastSynchro())) {
			lSynchronizationDTO.getUuidPlacesToDelete().add(lPlaceToDelete.getUuidPlace());
		}

		for (CategoryToDelete lCategoryToDelete : CategoryToDelete.dao.findAfterDate(pToken.getLastSynchro())) {
			lSynchronizationDTO.getUuidCategoriesToDelete().add(lCategoryToDelete.getUuidCategory());
		}
		return lSynchronizationDTO;
	}

	private static void cleanObjectsDeleted() {
		TaskToDelete.dao.deleteBeforeLastToken();
		TagToDelete.dao.deleteBeforeLastToken();
		PlaceToDelete.dao.deleteBeforeLastToken();
		CategoryToDelete.dao.deleteBeforeLastToken();

	}
}
