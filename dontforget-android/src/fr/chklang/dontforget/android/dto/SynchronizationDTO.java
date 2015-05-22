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
		} catch (JSONException e) {
			throw new AbstractDontForgetException(e);
		}
		
		return lContent;
	}
}
