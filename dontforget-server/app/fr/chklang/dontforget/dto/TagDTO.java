/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Tag;

/**
 * @author Chklang
 *
 */
public class TagDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	private final String uuid;
	
	private final String name;
	
	private final long lastUpdate;
	
	public TagDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		uuid = pJson.get("uuid").asText();
		name = pJson.get("name").asText();
		lastUpdate = pJson.get("lastUpdate").asLong();
		
		build();
	}
	
	public TagDTO(Tag pTag) {
		super(mapper.getNodeFactory());
		
		uuid = pTag.getUuid();
		name = pTag.getName();
		lastUpdate = pTag.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("uuid", uuid);
		
		this.put("name", name);
		
		this.put("lastUpdate", lastUpdate);
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}
}
