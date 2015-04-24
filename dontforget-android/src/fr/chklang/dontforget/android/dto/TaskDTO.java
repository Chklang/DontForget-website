/**
 * 
 */
package fr.chklang.dontforget.android.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;

/**
 * @author Chklang
 *
 */
public class TaskDTO {
	
	private int id;
	
	private String text;
	
	private TaskStatus status;
	
	private Collection<TagDTO> tags;
	
	private Collection<PlaceDTO> places;
	
	private String categoryName;

	public TaskDTO() {
		super();
	}
	
	public TaskDTO(JSONObject pObject) {
		super();
		try {
			id = pObject.getInt("id");
			text = pObject.getString("text");
			String lTempStatus = pObject.getString("status");
			if (TaskStatus.OPENED.name().equalsIgnoreCase(lTempStatus)) {
				status = TaskStatus.OPENED;
			} else if (TaskStatus.FINISHED.name().equalsIgnoreCase(lTempStatus)) {
				status = TaskStatus.FINISHED;
			} else if (TaskStatus.DELETED.name().equalsIgnoreCase(lTempStatus)) {
				status = TaskStatus.DELETED;
			} else {
				throw new AbstractDontForgetException("TaskStatus." + lTempStatus + " don't exists!");
			}
			JSONArray lTagsJson = pObject.getJSONArray("tags");
			Collection<TagDTO> lTags = new ArrayList<TagDTO>();
			for (int i = 0; i < lTagsJson.length(); i++) {
				JSONObject lTag = lTagsJson.getJSONObject(i);
				lTags.add(new TagDTO(lTag));
			}
			tags = lTags;
			JSONArray lPlacesJson = pObject.getJSONArray("tags");
			Collection<PlaceDTO> lPlaces = new ArrayList<PlaceDTO>();
			for (int i = 0; i < lPlacesJson.length(); i++) {
				JSONObject lPlace = lPlacesJson.getJSONObject(i);
				lPlaces.add(new PlaceDTO(lPlace));
			}
			places = lPlaces;
			categoryName = pObject.getString("category");
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	/**
	 * @return the tags
	 */
	public Collection<TagDTO> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Collection<TagDTO> tags) {
		this.tags = tags;
	}

	/**
	 * @return the places
	 */
	public Collection<PlaceDTO> getPlaces() {
		return places;
	}

	/**
	 * @param places the places to set
	 */
	public void setPlaces(Collection<PlaceDTO> places) {
		this.places = places;
	}

	/**
	 * @return the categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}

	/**
	 * @param categoryName the categoryName to set
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
