/**
 * 
 */
package fr.chklang.dontforget.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskStatus;

/**
 * @author Chklang
 *
 */
public class TaskDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final String uuid;
	
	private final String text;
	
	private final TaskStatus status;
	
	private final List<String> tags;
	
	private final List<String> places;
	
	private final String category;
	
	private final long lastUpdate;

	public TaskDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		uuid = pJson.get("uuid").asText();
		text = pJson.get("text").asText();
		status = TaskStatus.valueOf(pJson.get("status").asText());
		tags = new ArrayList<>();
		pJson.get("tagUuids").forEach((pNode) -> {
			tags.add(pNode.asText());
		});
		places = new ArrayList<>();
		pJson.get("placeUuids").forEach((pNode) -> {
			places.add(pNode.asText());
		});
		category = pJson.get("categoryUuid").asText();
		lastUpdate = pJson.get("lastUpdate").asLong();
		build();
	}
	public TaskDTO(Task pTask) {
		super(mapper.getNodeFactory());

		uuid = pTask.getUuid();
		text = pTask.getText();
		status = pTask.getStatus();
		
		tags = Arrays.asList(pTask.getTags().stream().map((pTag) -> {
			return pTag.getUuid();
		}).toArray(String[]::new));
		
		places = Arrays.asList(pTask.getPlaces().stream().map((pPlace) -> {
			return pPlace.getUuid();
		}).toArray(String[]::new));
		
		category = pTask.getCategory().getUuid();
		
		lastUpdate = pTask.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("uuid", uuid);
		this.put("text", text);
		this.put("status", status.name());
		
		ArrayNode lTags = this.arrayNode();
		tags.forEach((pTag) -> {
			lTags.add(pTag);
		});
		this.put("tags", lTags);
		
		ArrayNode lPlaces = this.arrayNode();
		places.forEach((pPlace) -> {
			lPlaces.add(pPlace);
		});
		this.put("places", lPlaces);
		
		this.put("category", category);
		
		this.put("lastUpdate", lastUpdate);
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
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
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}
	/**
	 * @return the places
	 */
	public List<String> getPlaces() {
		return places;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}
}
