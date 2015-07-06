package fr.chklang.dontforget.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

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

public class SynchronizationHelper {
	
	public static void update(Token pToken, SynchronizationDTO pSynchronizationDTO) {
		long lCurrentTime = System.currentTimeMillis();
		User lUser = User.dao.findByPseudo(pToken.getUser().getPseudo());
		
		updateLocalWithSynchronizationDTOCategoriesTagsAndPlaces(lUser, pSynchronizationDTO);
		updateLocalWithSynchronizationDTOTasks(lUser, pSynchronizationDTO);
		updateLocalWithSynchronizationDTODeleteTasks(lUser, pToken, pSynchronizationDTO, lCurrentTime);
		updateLocalWithSynchronizationDTODeleteTagsPlacesAndCategories(lUser, pToken, pSynchronizationDTO, lCurrentTime);
		
		correctionDoubloons();
	}
	
	private static void correctionDoubloons() {

		//Categories
		correctionDoubloons(new ICorrectionDoubloons<Category>() {
			@Override
			public Collection<Category> getAll() {
				return Category.dao.findList();
			}
			@Override
			public String getName(Category pObject) {
				return pObject.getName();
			}

			@Override
			public String getUuid(Category pObject) {
				return pObject.getUuid();
			}

			@Override
			public void delete(Category pObject) {
				CategoryToDelete lCategoryToDelete = new CategoryToDelete();
				lCategoryToDelete.setDateDeletion(System.currentTimeMillis());
				lCategoryToDelete.setUuidCategory(pObject.getUuid());
				lCategoryToDelete.save();
				
				pObject.delete();
			}

			@Override
			public Collection<Task> getTasksOfType(Category pObject) {
				return pObject.getTasks();
			}

			@Override
			public void updateAndSaveTask(Task pTaskToUpdate, Category pObjectBefore, Category pObjectAfter) {
				pTaskToUpdate.setCategory(pObjectAfter);
				pTaskToUpdate.setLastUpdate(System.currentTimeMillis());
				pTaskToUpdate.save();
			}
		});
		
		//Places
		correctionDoubloons(new ICorrectionDoubloons<Place>() {
			@Override
			public Collection<Place> getAll() {
				return Place.dao.findList();
			}
			@Override
			public String getName(Place pObject) {
				return pObject.getName();
			}

			@Override
			public String getUuid(Place pObject) {
				return pObject.getUuid();
			}

			@Override
			public void delete(Place pObject) {
				PlaceToDelete lPlaceToDelete = new PlaceToDelete();
				lPlaceToDelete.setDateDeletion(System.currentTimeMillis());
				lPlaceToDelete.setUuidPlace(pObject.getUuid());
				lPlaceToDelete.save();
				
				pObject.delete();
			}

			@Override
			public Collection<Task> getTasksOfType(Place pObject) {
				return pObject.getTasks();
			}

			@Override
			public void updateAndSaveTask(Task pTaskToUpdate, Place pObjectBefore, Place pObjectAfter) {
				pTaskToUpdate.getPlaces().remove(pObjectBefore);
				pTaskToUpdate.getPlaces().add(pObjectAfter);
				pTaskToUpdate.setLastUpdate(System.currentTimeMillis());
				pTaskToUpdate.save();
			}
		});
		
		//Tags
		correctionDoubloons(new ICorrectionDoubloons<Tag>() {
			@Override
			public Collection<Tag> getAll() {
				return Tag.dao.findList();
			}
			@Override
			public String getName(Tag pObject) {
				return pObject.getName();
			}

			@Override
			public String getUuid(Tag pObject) {
				return pObject.getUuid();
			}

			@Override
			public void delete(Tag pObject) {
				TagToDelete lTagToDelete = new TagToDelete();
				lTagToDelete.setDateDeletion(System.currentTimeMillis());
				lTagToDelete.setUuidTag(pObject.getUuid());
				lTagToDelete.save();
				
				pObject.delete();
			}

			@Override
			public Collection<Task> getTasksOfType(Tag pObject) {
				return pObject.getTasks();
			}

			@Override
			public void updateAndSaveTask(Task pTaskToUpdate, Tag pObjectBefore, Tag pObjectAfter) {
				pTaskToUpdate.getTags().remove(pObjectBefore);
				pTaskToUpdate.getTags().add(pObjectAfter);
				pTaskToUpdate.setLastUpdate(System.currentTimeMillis());
				pTaskToUpdate.save();
			}
		});
	
	}

	private static void updateLocalWithSynchronizationDTOCategoriesTagsAndPlaces(User pUser, SynchronizationDTO pSynchronizationDTO) {
		// Update/create categories
		for (CategoryDTO lCategoryDTO : pSynchronizationDTO.getCategories()) {
			Category lCategory = Category.dao.getByUuid(lCategoryDTO.getUuid());
			if (lCategory == null) {
				// Create it
				lCategory = new Category();
				lCategory.setName(lCategoryDTO.getName().trim());
				lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
				lCategory.setUuid(lCategoryDTO.getUuid());
				lCategory.setUser(pUser);
				lCategory.save();
			} else {
				// Update it
				if (lCategory.getUser().getIdUser() != pUser.getIdUser()) {
					//Category owned by another user
					continue;
				}
				if (lCategory.getLastUpdate() < lCategoryDTO.getLastUpdate()) {
					// Update local storage
					lCategory.setName(lCategoryDTO.getName().trim());
					lCategory.setLastUpdate(lCategoryDTO.getLastUpdate());
					lCategory.save();
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
				lTag.setUser(pUser);
				lTag.save();
			} else {
				// Update it
				if (lTag.getUser().getIdUser() != pUser.getIdUser()) {
					//Tag owned by another user
					continue;
				}
				if (lTag.getLastUpdate() < lTagDTO.getLastUpdate()) {
					// Update local storage
					lTag.setName(lTagDTO.getName().trim());
					lTag.setLastUpdate(lTagDTO.getLastUpdate());
					lTag.save();
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
				lPlace.setUser(pUser);
				lPlace.save();
			} else {
				// Update it
				if (lPlace.getUser().getIdUser() != pUser.getIdUser()) {
					//Place owned by another user
					continue;
				}
				if (lPlace.getLastUpdate() < lPlaceDTO.getLastUpdate()) {
					// Update local storage
					lPlace.setName(lPlaceDTO.getName().trim());
					lPlace.setLastUpdate(lPlaceDTO.getLastUpdate());
					lPlace.save();
				}
			}
		}

	}

	private static void updateLocalWithSynchronizationDTOTasks(User pUser, SynchronizationDTO pSynchronizationDTO) {
		// Update/create tasks
		for (TaskDTO lTaskDTO : pSynchronizationDTO.getTasks()) {
			Task lTask = Task.dao.getByUuid(lTaskDTO.getUuid());
			if (lTask == null) {
				// Create it
				lTask = new Task();
				Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategory());
				if (lCategory == null) {
					// TODO Error, ignore task for the moment
					continue;
				}
				lTask.setText(lTaskDTO.getText().trim());
				lTask.setCategory(lCategory);
				lTask.setStatus(lTaskDTO.getStatus());
				lTask.setLastUpdate(lTaskDTO.getLastUpdate());
				lTask.setUuid(lTaskDTO.getUuid());
				lTask.setUser(pUser);
				lTask.save();
			} else {
				// Update it
				if (lTask.getUser().getIdUser() != pUser.getIdUser()) {
					//Task owned by another user
					continue;
				}
				if (lTask.getLastUpdate() < lTaskDTO.getLastUpdate()) {
					// Update local storage
					Category lCategory = Category.dao.getByUuid(lTaskDTO.getCategory());
					if (lCategory == null) {
						// TODO Error, ignore task for the moment
						continue;
					}
					lTask.setText(lTaskDTO.getText().trim());
					lTask.setCategory(lCategory);
					lTask.setStatus(lTaskDTO.getStatus());
					lTask.setLastUpdate(lTaskDTO.getLastUpdate());
					lTask.setUuid(lTaskDTO.getUuid());
					lTask.save();
				}
			}

			// Attach tags
			Collection<String> lTagsOfTask = new HashSet<String>();
			for (Tag lTag : lTask.getTags()) {
				lTagsOfTask.add(lTag.getUuid());
			}
			for (String lTagUUID : lTaskDTO.getTags()) {
				Tag lTag = Tag.dao.getByUuid(lTagUUID);
				if (lTag == null) {
					// TODO This tag was deleted here
					// This problem must never comes because task must be more
					// recent on server than local storage
					continue;
				}
				if (!lTagsOfTask.contains(lTagUUID)) {
					lTask.getTags().add(lTag);
				} else {
					lTagsOfTask.remove(lTagUUID);
				}
			}
			for (String lUuidToRemove : lTagsOfTask) {
				Tag lTag = Tag.dao.getByUuid(lUuidToRemove);
				lTask.getTags().remove(lTag);
			}

			// Attach places
			Collection<String> lPlacesOfTask = new HashSet<String>();
			for (Place lPlace : lTask.getPlaces()) {
				lPlacesOfTask.add(lPlace.getUuid());
			}
			for (String lPlaceUUID : lTaskDTO.getPlaces()) {
				Place lPlace = Place.dao.getByUuid(lPlaceUUID);
				if (lPlace == null) {
					// This place was deleted here
					// This problem must never comes because task must be more
					// recent on server than local storage
					continue;
				}
				if (!lPlacesOfTask.contains(lPlaceUUID)) {
					lTask.getPlaces().add(lPlace);
				} else {
					lPlacesOfTask.remove(lPlaceUUID);
				}
			}
			for (String lUuidToRemove : lPlacesOfTask) {
				Place lPlace = Place.dao.getByUuid(lUuidToRemove);
				lTask.getPlaces().remove(lPlace);
			}
			
			lTask.save();
		}
	}

	private static void updateLocalWithSynchronizationDTODeleteTasks(User pUser, Token pToken, SynchronizationDTO pSynchronizationDTO, long currentTime) {
		for (String lIdTaskToDelete : pSynchronizationDTO.getUuidTasksToDelete()) {
			Task lTask = Task.dao.getByUuid(lIdTaskToDelete);
			if (lTask.getUser().getIdUser() != pUser.getIdUser()) {
				//Task owned by another user
				continue;
			}
			if (lTask != null && lTask.getLastUpdate() < pToken.getLastUpdate()) {
				TaskToDelete lTaskToDelete = new TaskToDelete();
				lTaskToDelete.setUuidTask(lTask.getUuid());
				lTaskToDelete.setDateDeletion(currentTime);
				lTaskToDelete.save();
				lTask.delete();
			}
		}
	}

	private static void updateLocalWithSynchronizationDTODeleteTagsPlacesAndCategories(User pUser, Token pToken, SynchronizationDTO pSynchronizationDTO, long currentTime) {
		for (String lIdTagToDelete : pSynchronizationDTO.getUuidTagsToDelete()) {
			Tag lTag = Tag.dao.getByUuid(lIdTagToDelete);
			if (lTag != null && lTag.getLastUpdate() < pToken.getLastUpdate()) {
				if (Task.dao.findByTagAndUser(lTag, pUser).isEmpty()) {
					TagToDelete lTagToDelete = new TagToDelete();
					lTagToDelete.setUuidTag(lTag.getUuid());
					lTagToDelete.setDateDeletion(currentTime);
					lTagToDelete.save();
					lTag.delete();
				}
			}
		}
		for (String lIdPlaceToDelete : pSynchronizationDTO.getUuidPlacesToDelete()) {
			Place lPlace = Place.dao.getByUuid(lIdPlaceToDelete);
			if (lPlace != null && lPlace.getLastUpdate() < pToken.getLastUpdate()) {
				if (Task.dao.findByPlaceAndUser(lPlace, pUser).isEmpty()) {
					PlaceToDelete lPlaceToDelete = new PlaceToDelete();
					lPlaceToDelete.setUuidPlace(lPlace.getUuid());
					lPlaceToDelete.setDateDeletion(currentTime);
					lPlaceToDelete.save();
					lPlace.delete();
				}
			}
		}
		for (String lIdCategoryToDelete : pSynchronizationDTO.getUuidCategoriesToDelete()) {
			Category lCategory = Category.dao.getByUuid(lIdCategoryToDelete);
			if (lCategory != null && lCategory.getLastUpdate() < pToken.getLastUpdate()) {
				if (Task.dao.findByCategoryAndUser(lCategory, pUser).isEmpty()) {
					CategoryToDelete lCategoryToDelete = new CategoryToDelete();
					lCategoryToDelete.setUuidCategory(lCategory.getUuid());
					lCategoryToDelete.setDateDeletion(currentTime);
					lCategoryToDelete.save();
					lCategory.delete();
				}
			}
		}
	}
	
	private interface ICorrectionDoubloons<T> {
		Collection<T> getAll();
		
		String getName(T pObject);
		
		String getUuid(T pObject);
		
		void delete (T pObject);
		
		Collection<Task> getTasksOfType(T pObject);
		
		void updateAndSaveTask(Task pTaskToUpdate, T pObjectBefore, T pObjectAfter);
	}
	
	private static <T> void correctionDoubloons(ICorrectionDoubloons<T> pConfig) {
		Map<String, Collection<T>> lMapTypeNameTypes = new HashMap<String, Collection<T>>();
		for (T lType : pConfig.getAll()) {
			Collection<T> lTypeWithSameName = lMapTypeNameTypes.get(pConfig.getName(lType));
			if (lTypeWithSameName == null) {
				lTypeWithSameName = new ArrayList<T>();
				lMapTypeNameTypes.put(pConfig.getName(lType), lTypeWithSameName);
			}
			lTypeWithSameName.add(lType);
		}
		
		//Check if there is some doubloons
		for (Entry<String, Collection<T>> lEntry : lMapTypeNameTypes.entrySet()) {
			if (lEntry.getValue().size() > 1) {
				//doubloons
				String lUuidToKeep = null;
				T lTypeToKeep = null;
				for (T lType : lEntry.getValue()) {
					if (lUuidToKeep == null) {
						lUuidToKeep = pConfig.getUuid(lType);
						lTypeToKeep = lType;
					} else if (AbstractStringComparatorByAsciiOrder.compareStatic(lUuidToKeep, pConfig.getUuid(lType)) > 0) {
						lUuidToKeep = pConfig.getUuid(lType);
						lTypeToKeep = lType;
					}
				}
				for (T lType : lEntry.getValue()) {
					if (!lUuidToKeep.equals(pConfig.getUuid(lType))) {
						Collection<Task> lTasks = pConfig.getTasksOfType(lType);
						for (Task lTask : lTasks) {
							pConfig.updateAndSaveTask(lTask, lType, lTypeToKeep);
						}
						pConfig.delete(lType);
					}
				}
				
			}
		}
	}

}
