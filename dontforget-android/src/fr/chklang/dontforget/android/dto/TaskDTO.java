/**
 * 
 */
package fr.chklang.dontforget.android.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;

/**
 * @author Chklang
 *
 */
public class TaskDTO {
	
	private final String text;
	
	private final TaskStatus status;
	
	private final Collection<String> tagUuids;
	
	private final Collection<String> placeUuids;
	
	private final String categoryUuid;
	
	private final String uuid;
	
	private final long lastUpdate;
	
	public TaskDTO(JSONObject pObject) {
		super();
		try {
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
			JSONArray lTagsJson = pObject.getJSONArray("tagUuids");
			Collection<String> lTags = new ArrayList<String>();
			for (int i = 0; i < lTagsJson.length(); i++) {
				lTags.add(lTagsJson.getString(i));
			}
			tagUuids = lTags;
			JSONArray lPlacesJson = pObject.getJSONArray("placeUuids");
			Collection<String> lPlaces = new ArrayList<String>();
			for (int i = 0; i < lPlacesJson.length(); i++) {
				lPlaces.add(lPlacesJson.getString(i));
			}
			placeUuids = lPlaces;
			categoryUuid = pObject.getString("categoryUuid");
			uuid = pObject.getString("uuid");
			lastUpdate = pObject.getLong("lastUpdate");
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	
	public TaskDTO(String pText, TaskStatus pStatus, String pCategoryUuid, String pUuid, long pLastUpdate) {
		super();
		this.text = pText;
		this.status = pStatus;
		this.tagUuids = new ArrayList<String>();
		this.placeUuids = new ArrayList<String>();
		this.categoryUuid = pCategoryUuid;
		this.uuid = pUuid;
		this.lastUpdate = pLastUpdate;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}


	/**
	 * @return the tagUuids
	 */
	public Collection<String> getTagUuids() {
		return tagUuids;
	}


	/**
	 * @return the placeUuids
	 */
	public Collection<String> getPlaceUuids() {
		return placeUuids;
	}


	/**
	 * @return the categoryUuid
	 */
	public String getCategoryUuid() {
		return categoryUuid;
	}


	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}


	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}


	public JSONObject toJson() {
		JSONObject lContent = new JSONObject();
		
		try {
			lContent.put("text", text);
			lContent.put("status", status);
			
			JSONArray lTags = new JSONArray();
			for (String lTag : tagUuids) {
				lTags.put(lTag);
			}
			lContent.put("tagUuids", lTags);
			
			JSONArray lPlaces = new JSONArray();
			for (String lPlace : placeUuids) {
				lPlaces.put(lPlace);
			}
			lContent.put("placeUuids", lPlaces);
			
			lContent.put("categoryUuid", categoryUuid);
			lContent.put("uuid", uuid);
			lContent.put("lastUpdate", lastUpdate);
		} catch (JSONException e) {
			throw new AbstractDontForgetException(e);
		}
			
		return lContent;
	}

}
