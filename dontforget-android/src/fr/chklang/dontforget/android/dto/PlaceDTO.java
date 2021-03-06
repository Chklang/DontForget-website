/**
 * 
 */
package fr.chklang.dontforget.android.dto;

import org.json.JSONException;
import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;


/**
 * @author Chklang
 *
 */
public class PlaceDTO {
	
	private final String name;
	
	private final String uuid;
	
	private final long lastUpdate;
	
	public PlaceDTO (JSONObject pObject) {
		super();
		try {
			name = pObject.getString("name");
			uuid = pObject.getString("uuid");
			lastUpdate = pObject.getLong("lastUpdate");
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);//TODO
		}
	}

	public PlaceDTO(String pName, String pUuid, long pLastUpdate) {
		super();
		this.name = pName;
		this.uuid = pUuid;
		this.lastUpdate = pLastUpdate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
			lContent.put("name", name);
			lContent.put("uuid", uuid);
			lContent.put("lastUpdate", lastUpdate);
		} catch (JSONException e) {
			throw new AbstractDontForgetException(e);
		}
			
		return lContent;
	}
}
