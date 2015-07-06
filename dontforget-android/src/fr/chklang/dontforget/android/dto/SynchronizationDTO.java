package fr.chklang.dontforget.android.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;

public class SynchronizationDTO {
	
	private final Collection<TaskDTO> tasks;
	private final Collection<TagDTO> tags;
	private final Collection<PlaceDTO> places;
	private final Collection<CategoryDTO> categories;
	
	private final Collection<String> uuidTasksToDelete;
	private final Collection<String> uuidTagsToDelete;
	private final Collection<String> uuidPlacesToDelete;
	private final Collection<String> uuidCategoriesToDelete;
	
	public SynchronizationDTO(JSONObject pObject) {
		this();
		
		try {
			JSONArray lTasksJSON = pObject.getJSONArray("tasks");
			int lNbTasks = lTasksJSON.length();
			for (int i=0; i<lNbTasks; i++) {
				TaskDTO lTaskDTO = new TaskDTO(lTasksJSON.getJSONObject(i));
				tasks.add(lTaskDTO);
			}
			
			JSONArray lTagsJSON = pObject.getJSONArray("tags");
			int lNbTags = lTagsJSON.length();
			for (int i=0; i<lNbTags; i++) {
				TagDTO lTagDTO = new TagDTO(lTagsJSON.getJSONObject(i));
				tags.add(lTagDTO);
			}
			
			JSONArray lPlacesJSON = pObject.getJSONArray("places");
			int lNbPlaces = lPlacesJSON.length();
			for (int i=0; i<lNbPlaces; i++) {
				PlaceDTO lPlaceDTO = new PlaceDTO(lPlacesJSON.getJSONObject(i));
				places.add(lPlaceDTO);
			}
			
			JSONArray lCategoriesJSON = pObject.getJSONArray("categories");
			int lNbCategories = lCategoriesJSON.length();
			for (int i=0; i<lNbCategories; i++) {
				CategoryDTO lCategoryDTO = new CategoryDTO(lCategoriesJSON.getJSONObject(i));
				categories.add(lCategoryDTO);
			}
			
			JSONArray lUuidTasksToDeleteJson = pObject.getJSONArray("uuidTasksToDelete");
			int lNbUuidTasksToDelete = lUuidTasksToDeleteJson.length();
			for (int i=0; i<lNbUuidTasksToDelete; i++) {
				uuidTasksToDelete.add(lUuidTasksToDeleteJson.getString(i));
			}
			
			JSONArray lIdTagsToDeleteJson = pObject.getJSONArray("uuidTagsToDelete");
			int lNbIdTagsToDelete = lIdTagsToDeleteJson.length();
			for (int i=0; i<lNbIdTagsToDelete; i++) {
				uuidTagsToDelete.add(lIdTagsToDeleteJson.getString(i));
			}
			
			JSONArray lIdPlacesToDeleteJson = pObject.getJSONArray("uuidPlacesToDelete");
			int lNbIdPlacesToDelete = lIdPlacesToDeleteJson.length();
			for (int i=0; i<lNbIdPlacesToDelete; i++) {
				uuidPlacesToDelete.add(lIdPlacesToDeleteJson.getString(i));
			}
			
			JSONArray lIdCategoriesToDeleteJson = pObject.getJSONArray("uuidCategoriesToDelete");
			int lNbIdCategoriesToDelete = lIdCategoriesToDeleteJson.length();
			for (int i=0; i<lNbIdCategoriesToDelete; i++) {
				uuidCategoriesToDelete.add(lIdCategoriesToDeleteJson.getString(i));
			}
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	public SynchronizationDTO() {
		super();
		tasks = new ArrayList<TaskDTO>();
		tags = new ArrayList<TagDTO>();
		places = new ArrayList<PlaceDTO>();
		categories = new ArrayList<CategoryDTO>();
		
		uuidTasksToDelete = new ArrayList<String>();
		uuidTagsToDelete = new ArrayList<String>();
		uuidPlacesToDelete = new ArrayList<String>();
		uuidCategoriesToDelete = new ArrayList<String>();
	}

	/**
	 * @return the tasks
	 */
	public Collection<TaskDTO> getTasks() {
		return tasks;
	}

	/**
	 * @return the tags
	 */
	public Collection<TagDTO> getTags() {
		return tags;
	}

	/**
	 * @return the places
	 */
	public Collection<PlaceDTO> getPlaces() {
		return places;
	}

	/**
	 * @return the categories
	 */
	public Collection<CategoryDTO> getCategories() {
		return categories;
	}

	/**
	 * @return the idTasksToDelete
	 */
	public Collection<String> getUuidTasksToDelete() {
		return uuidTasksToDelete;
	}

	/**
	 * @return the idTagsToDelete
	 */
	public Collection<String> getUuidTagsToDelete() {
		return uuidTagsToDelete;
	}

	/**
	 * @return the idPlacesToDelete
	 */
	public Collection<String> getUuidPlacesToDelete() {
		return uuidPlacesToDelete;
	}

	/**
	 * @return the idCategoriesToDelete
	 */
	public Collection<String> getUuidCategoriesToDelete() {
		return uuidCategoriesToDelete;
	}
	
	public JSONObject toJson() {
		JSONObject lContent = new JSONObject();

		try {
			JSONArray lCategories = new JSONArray();
			for (CategoryDTO lCategory : categories) {
				lCategories.put(lCategory.toJson());
			}
			lContent.put("categories", lCategories);
			
			JSONArray lTags = new JSONArray();
			for (TagDTO lTag : tags) {
				lTags.put(lTag.toJson());
			}
			lContent.put("tags", lTags);
			
			JSONArray lPlaces = new JSONArray();
			for (PlaceDTO lPlace : places) {
				lPlaces.put(lPlace.toJson());
			}
			lContent.put("places", lPlaces);
			
			JSONArray lTasks = new JSONArray();
			for (TaskDTO lTask : tasks) {
				lTasks.put(lTask.toJson());
			}
			lContent.put("tasks", lTasks);
			
			JSONArray lUuidTasksToDelete = new JSONArray();
			for (String lUuidTaskToDelete : uuidTasksToDelete) {
				lUuidTasksToDelete.put(lUuidTaskToDelete);
			}
			lContent.put("uuidTasksToDelete", lUuidTasksToDelete);
			
			JSONArray lUuidTagsToDelete = new JSONArray();
			for (String lUuidTagToDelete : uuidTagsToDelete) {
				lUuidTagsToDelete.put(lUuidTagToDelete);
			}
			lContent.put("uuidTagsToDelete", lUuidTagsToDelete);
			
			JSONArray lUuidPlacesToDelete = new JSONArray();
			for (String lUuidPlaceToDelete : uuidPlacesToDelete) {
				lUuidPlacesToDelete.put(lUuidPlaceToDelete);
			}
			lContent.put("uuidPlacesToDelete", lUuidPlacesToDelete);
			
			JSONArray lUuidCategoriesToDelete = new JSONArray();
			for (String lUuidCategoryToDelete : uuidCategoriesToDelete) {
				lUuidCategoriesToDelete.put(lUuidCategoryToDelete);
			}
			lContent.put("uuidCategoriesToDelete", lUuidCategoriesToDelete);
		} catch (JSONException e) {
			throw new AbstractDontForgetException(e);
		}
		
		return lContent;
	}
}
