package fr.chklang.dontforget.resources;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import play.mvc.Result;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskToDelete;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.CategoryDTO;
import fr.chklang.dontforget.dto.PlaceDTO;
import fr.chklang.dontforget.dto.SynchronizationDTO;
import fr.chklang.dontforget.dto.TagDTO;
import fr.chklang.dontforget.dto.TaskDTO;
import fr.chklang.dontforget.exceptions.WebException;
import fr.chklang.dontforget.helpers.CategoryAdapterForUpdate;
import fr.chklang.dontforget.helpers.PlaceAdapterForUpdate;
import fr.chklang.dontforget.helpers.SynchronizationHelper;
import fr.chklang.dontforget.helpers.TagAdapterForUpdate;


public class SynchronizationResource extends AbstractRest {

	public static Result getUpdates(long pLastUpdate) {
		return executeAndVerifyToken(() -> {
			Token lToken = getConnectedToken();
			if (lToken == null) {
				return unauthorized();
			}
			
			final User lConnectedUser = getConnectedUser();

			Collection<Task> lTasks = Task.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Tag> lTags = Tag.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Place> lPlaces = Place.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Category> lCategories = Category.dao.findByLastUpdate(pLastUpdate, lConnectedUser);

			Collection<CategoryToDelete> lCategoriesToDelete = CategoryToDelete.dao.findAfterDate(pLastUpdate);
			Collection<PlaceToDelete> lPlacesToDelete = PlaceToDelete.dao.findAfterDate(pLastUpdate);
			Collection<TagToDelete> lTagsToDelete = TagToDelete.dao.findAfterDate(pLastUpdate);
			Collection<TaskToDelete> lTasksToDelete = TaskToDelete.dao.findAfterDate(pLastUpdate);
			return ok(new SynchronizationDTO(lTasks.stream(), lTags.stream(), lPlaces.stream(), lCategories.stream(), lToken.getUser(), lCategoriesToDelete.stream(),
					lPlacesToDelete.stream(), lTagsToDelete.stream(), lTasksToDelete.stream()));
		});
	}

	public static Result setUpdates() {
		return executeAndVerifyToken(() -> {
			SynchronizationDTO lDTO = new SynchronizationDTO(request().body().asJson());
			
			final User lConnectedUser = getConnectedUser();
			
			//Insert or update elements
			final Map<String, String> lUpdatesTags = SynchronizationHelper.updateDTOs(lConnectedUser, Arrays.asList(lDTO.getTags().toArray(TagDTO[]::new)), new TagAdapterForUpdate());
			final Map<String, String> lUpdatesPlaces = SynchronizationHelper.updateDTOs(lConnectedUser, Arrays.asList(lDTO.getPlaces().toArray(PlaceDTO[]::new)), new PlaceAdapterForUpdate());
			final Map<String, String> lUpdatesCategories = SynchronizationHelper.updateDTOs(lConnectedUser, Arrays.asList(lDTO.getCategories().toArray(CategoryDTO[]::new)), new CategoryAdapterForUpdate());

			lDTO.getTasks().forEach((pTaskDTO) -> insertOrUpdate(lConnectedUser, pTaskDTO, lUpdatesTags, lUpdatesPlaces, lUpdatesCategories));
	
			final Token lToken = getConnectedToken();
			//Delete old elements
			lDTO.getUuidTasksToDelete().forEach((pUuidTask) -> removeOldTasks(lConnectedUser, lToken, pUuidTask));
			
			lDTO.getUuidTagsToDelete().forEach((pUuidTag) -> removeOldTags(lConnectedUser, lToken, pUuidTag));
			
			lDTO.getUuidPlacesToDelete().forEach((pUuidPlace) -> removeOldPlaces(lConnectedUser, lToken, pUuidPlace));
			
			lDTO.getUuidCategoriesToDelete().forEach((pUuidCategory) -> removeOldCategories(lConnectedUser, lToken, pUuidCategory));
			
			return ok();
		});
	}

	private static void removeOldCategories(final User pConnectedUser, final Token pToken, String pUuidCategory) {
		Category lCategoryDB = Category.dao.getByUuid(pUuidCategory);
		if (lCategoryDB == null || lCategoryDB.getUser().getIdUser() != pConnectedUser.getIdUser()) {
			//Category isn't assigned to user, ignore it
			return;
		}
		if (lCategoryDB.getLastUpdate() > pToken.getLastUpdate()) {
			//Some modifications was done here, not delete it (strategy on conflict)
			return;
		}
		CategoryToDelete lCategoryToDelete = new CategoryToDelete();
		lCategoryToDelete.setUuidCategory(pUuidCategory);
		lCategoryToDelete.setDateDeletion(System.currentTimeMillis());
		lCategoryToDelete.save();
		lCategoryDB.delete();
	}

	private static void removeOldPlaces(final User pConnectedUser, final Token pToken, String pUuidPlace) {
		Place lPlaceDB = Place.dao.getByUuid(pUuidPlace);
		if (lPlaceDB == null || lPlaceDB.getUser().getIdUser() != pConnectedUser.getIdUser()) {
			//Place isn't assigned to user, ignore it
			return;
		}
		if (lPlaceDB.getLastUpdate() > pToken.getLastUpdate()) {
			//Some modifications was done here, not delete it (strategy on conflict)
			return;
		}
		PlaceToDelete lPlaceToDelete = new PlaceToDelete();
		lPlaceToDelete.setUuidPlace(pUuidPlace);
		lPlaceToDelete.setDateDeletion(System.currentTimeMillis());
		lPlaceToDelete.save();
		lPlaceDB.delete();
	}

	private static void removeOldTags(final User pConnectedUser, final Token pToken, String pUuidTag) {
		Tag lTagDB = Tag.dao.getByUuid(pUuidTag);
		if (lTagDB == null || lTagDB.getUser().getIdUser() != pConnectedUser.getIdUser()) {
			//Tag isn't assigned to user, ignore it
			return;
		}
		if (lTagDB.getLastUpdate() > pToken.getLastUpdate()) {
			//Some modifications was done here, not delete it (strategy on conflict)
			return;
		}
		TagToDelete lTagToDelete = new TagToDelete();
		lTagToDelete.setUuidTag(pUuidTag);
		lTagToDelete.setDateDeletion(System.currentTimeMillis());
		lTagToDelete.save();
		lTagDB.delete();
	}

	private static void removeOldTasks(final User pConnectedUser, final Token pToken, String pUuidTask) {
		Task lTaskDB = Task.dao.getByUuid(pUuidTask);
		if (lTaskDB == null || lTaskDB.getUser().getIdUser() != pConnectedUser.getIdUser()) {
			//Task isn't assigned to user, ignore it
			return;
		}
		if (lTaskDB.getLastUpdate() > pToken.getLastUpdate()) {
			//Some modifications was done here, not delete it (strategy on conflict)
			return;
		}
		TaskToDelete lTaskToDelete = new TaskToDelete();
		lTaskToDelete.setUuidTask(pUuidTask);
		lTaskToDelete.setDateDeletion(System.currentTimeMillis());
		lTaskToDelete.save();
		lTaskDB.delete();
	}

	private static void insertOrUpdate(User lConnectedUser, TaskDTO pTaskDTO, Map<String, String> pUpdatesTags, Map<String, String> pUpdatesPlaces, Map<String, String> pUpdatesCategories) {
		Task lTaskDB = Task.dao.getByUuid(pTaskDTO.getUuid());

		String lUuidCategory = pTaskDTO.getCategory();
		Set<String> lCategoriesUuidAlreadyPassed = new HashSet<>();
		while (pUpdatesCategories.containsKey(lUuidCategory)) {
			if (lCategoriesUuidAlreadyPassed.contains(lUuidCategory)) {
				//Infinite loop detected!
				throw new WebException(status(500), "Infinite loop to replace uuid category " + pTaskDTO.getCategory());
			}
			lCategoriesUuidAlreadyPassed.add(lUuidCategory);
			lUuidCategory = pUpdatesCategories.get(lUuidCategory);
		}
		Category lCategory = Category.dao.getByUuid(lUuidCategory);
		
		if (lCategory == null || lCategory.getUser().getIdUser() != lConnectedUser.getIdUser()) {
			// Category isn't assigned to current user
			return;
		}
		Set<Tag> lTags = new HashSet<>();
		pTaskDTO.getTags().forEach((pTagDTO) -> {
			String lUuidTag = pTagDTO;
			Set<String> lUuidAlreadyPassed = new HashSet<>();
			while (pUpdatesTags.containsKey(lUuidTag)) {
				if (lUuidAlreadyPassed.contains(lUuidTag)) {
					//Infinite loop detected!
					throw new WebException(status(500), "Infinite loop to replace uuid tag " + pTagDTO);
				}
				lUuidAlreadyPassed.add(lUuidTag);
				lUuidTag = pUpdatesTags.get(lUuidTag);
			}
			Tag lTag = Tag.dao.getByUuid(lUuidTag);
			if (lTag != null && lTag.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				lTags.add(lTag);
			}
		});
		Set<Place> lPlaces = new HashSet<>();
		pTaskDTO.getPlaces().forEach((pPlaceDTO) -> {
			String lUuidPlace = pPlaceDTO;
			Set<String> lUuidAlreadyPassed = new HashSet<>();
			while (pUpdatesPlaces.containsKey(lUuidPlace)) {
				if (lUuidAlreadyPassed.contains(lUuidPlace)) {
					//Infinite loop detected!
					throw new WebException(status(500), "Infinite loop to replace uuid tag " + pPlaceDTO);
				}
				lUuidAlreadyPassed.add(lUuidPlace);
				lUuidPlace = pUpdatesPlaces.get(lUuidPlace);
			}
			Place lPlace = Place.dao.getByUuid(lUuidPlace);
			if (lPlace != null && lPlace.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				lPlaces.add(lPlace);
			}
		});
		if (lTaskDB != null) {
			if (lTaskDB.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return;
			}
			// Check if update is necessary
			if (lTaskDB.getLastUpdate() < pTaskDTO.getLastUpdate()) {
				lTaskDB.setText(pTaskDTO.getText());
				lTaskDB.setStatus(pTaskDTO.getStatus());
				lTaskDB.setPlaces(lPlaces);
				lTaskDB.setTags(lTags);
				lTaskDB.setCategory(lCategory);
				lTaskDB.setLastUpdate(pTaskDTO.getLastUpdate());
				lTaskDB.save();
			}
		} else {
			// Create it
			lTaskDB = new Task();
			lTaskDB.setText(pTaskDTO.getText());
			lTaskDB.setPlaces(lPlaces);
			lTaskDB.setTags(lTags);
			lTaskDB.setCategory(lCategory);
			lTaskDB.setUser(lConnectedUser);
			lTaskDB.setLastUpdate(pTaskDTO.getLastUpdate());
			lTaskDB.save();
		}
	}
}
