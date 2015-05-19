package fr.chklang.dontforget.resources;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import play.mvc.Result;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.CategoryDTO;
import fr.chklang.dontforget.dto.PlaceDTO;
import fr.chklang.dontforget.dto.SynchronizationDTO;
import fr.chklang.dontforget.dto.TagDTO;

public class SynchronizationResource extends AbstractRest {

	public static Result getUpdates(long pLastUpdate) {
		return execute(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				Collection<Task> lTasks = Task.dao.findByLastUpdate(pLastUpdate, getConnectedUser());
				Collection<Tag> lTags = Tag.dao.findByLastUpdate(pLastUpdate, getConnectedUser());
				Collection<Place> lPlaces = Place.dao.findByLastUpdate(pLastUpdate, getConnectedUser());
				Collection<Category> lCategories = Category.dao.findByLastUpdate(pLastUpdate, getConnectedUser());

				User lUser = getConnectedUser();
				if (lUser.getLastUpdate() <= pLastUpdate) {
					lUser = null;
				}
				return ok(new SynchronizationDTO(lTasks.stream(), lTags.stream(), lPlaces.stream(), lCategories.stream(), lUser));
			}
		});
	}

	public static Result setUpdates() {
		SynchronizationDTO lDTO = new SynchronizationDTO(request().body().asJson());

		lDTO.getTags().forEach((pTagDTO) -> {
			insertOrUpdate(pTagDTO);
		});

		lDTO.getPlaces().forEach((pPlaceDTO) -> {
			insertOrUpdate(pPlaceDTO);
		});

		lDTO.getCategories().forEach((pCategoryDTO) -> {
			insertOrUpdate(pCategoryDTO);
		});

		lDTO.getTasks().forEach((pTaskDTO) -> {
			Task lTaskDB = Task.dao.getByUuid(pTaskDTO.getUuid());
			Category lCategory = insertOrUpdate(pTaskDTO.getCategory());
			if (lCategory == null) {
				// Category isn't assigned to current user
				return;
			}
			Set<Tag> lTags = new HashSet<>();
			pTaskDTO.getTags().forEach((pTagDTO) -> {
				Tag lTag = insertOrUpdate(pTagDTO);
				if (lTag != null) {
					lTags.add(lTag);
				}
			});
			Set<Place> lPlaces = new HashSet<>();
			pTaskDTO.getPlaces().forEach((pPlaceDTO) -> {
				Place lPlace = insertOrUpdate(pPlaceDTO);
				if (lPlace != null) {
					lPlaces.add(lPlace);
				}
			});
			if (lTaskDB != null) {
				if (lTaskDB.getUser().getIdUser() != getConnectedUser().getIdUser()) {
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
				lTaskDB.setUser(getConnectedUser());
				lTaskDB.setLastUpdate(pTaskDTO.getLastUpdate());
				lTaskDB.save();
			}
		});
		return ok();
	}

	private static Tag insertOrUpdate(TagDTO pTagDTO) {
		Tag lTagDB = Tag.dao.getByUuid(pTagDTO.getUuid());
		if (lTagDB != null) {
			if (lTagDB.getUser().getIdUser() != getConnectedUser().getIdUser()) {
				return null;
			}
			// Check if update is necessary
			if (lTagDB.getLastUpdate() < pTagDTO.getLastUpdate()) {
				lTagDB.setName(pTagDTO.getName());
				lTagDB.setLastUpdate(pTagDTO.getLastUpdate());
				lTagDB.save();
			}
		} else {
			// Create it
			lTagDB = new Tag();
			lTagDB.setName(pTagDTO.getName());
			lTagDB.setUser(getConnectedUser());
			lTagDB.setLastUpdate(pTagDTO.getLastUpdate());
			lTagDB.save();
		}
		return lTagDB;
	}

	private static Place insertOrUpdate(PlaceDTO pPlaceDTO) {
		Place lPlaceDB = Place.dao.getByUuid(pPlaceDTO.getUuid());
		if (lPlaceDB != null) {
			if (lPlaceDB.getUser().getIdUser() != getConnectedUser().getIdUser()) {
				return null;
			}
			// Check if update is necessary
			if (lPlaceDB.getLastUpdate() < pPlaceDTO.getLastUpdate()) {
				lPlaceDB.setName(pPlaceDTO.getName());
				lPlaceDB.setLastUpdate(pPlaceDTO.getLastUpdate());
				lPlaceDB.save();
			}
		} else {
			// Create it
			lPlaceDB = new Place();
			lPlaceDB.setName(pPlaceDTO.getName());
			lPlaceDB.setUser(getConnectedUser());
			lPlaceDB.setLastUpdate(pPlaceDTO.getLastUpdate());
			lPlaceDB.save();
		}
		return lPlaceDB;
	}

	private static Category insertOrUpdate(CategoryDTO pCategoryDTO) {
		Category lCategoryDB = Category.dao.getByUuid(pCategoryDTO.getUuid());
		if (lCategoryDB != null) {
			if (lCategoryDB.getUser().getIdUser() != getConnectedUser().getIdUser()) {
				return null;
			}
			// Check if update is necessary
			if (lCategoryDB.getLastUpdate() < pCategoryDTO.getLastUpdate()) {
				lCategoryDB.setName(pCategoryDTO.getName());
				lCategoryDB.setLastUpdate(pCategoryDTO.getLastUpdate());
				lCategoryDB.save();
			}
		} else {
			// Create it
			lCategoryDB = new Category();
			lCategoryDB.setName(pCategoryDTO.getName());
			lCategoryDB.setUser(getConnectedUser());
			lCategoryDB.setLastUpdate(pCategoryDTO.getLastUpdate());
			lCategoryDB.save();
		}
		return lCategoryDB;
	}
}
