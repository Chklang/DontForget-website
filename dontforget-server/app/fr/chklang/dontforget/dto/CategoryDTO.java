/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Tag;

/**
 * @author Chklang
 *
 */
public class CategoryDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	private final int id;
	
	private final String name;
	
	private final long lastUpdate;
	
	public CategoryDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		id = pJson.get("id").asInt();
		name = pJson.get("name").asText();
		lastUpdate = pJson.get("lastUpdate").asLong();
		
		build();
	}
	
	public CategoryDTO(Category pTag) {
		super(mapper.getNodeFactory());
		
		id = pTag.getId();
		name = pTag.getName();
		lastUpdate = pTag.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("id", id);
		
		this.put("name", name);
		
		this.put("lastUpdate", lastUpdate);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
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
