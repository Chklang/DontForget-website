/**
 * 
 */
package fr.chklang.dontforget.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

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
	
	private final Stream<TagDTO> tags;
	
	private final Stream<PlaceDTO> places;
	
	private final CategoryDTO category;
	
	private final long lastUpdate;

	public TaskDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		uuid = pJson.get("uuid").asText();
		text = pJson.get("text").asText();
		status = TaskStatus.valueOf(pJson.get("status").asText());
		Collection<TagDTO> lTags = new ArrayList<>();
		pJson.get("tags").forEach((pNode) -> {
			lTags.add(new TagDTO(pNode));
		});
		tags = lTags.stream();
		Collection<PlaceDTO> lPlaces = new ArrayList<>();
		pJson.get("places").forEach((pNode) -> {
			lPlaces.add(new PlaceDTO(pNode));
		});
		places = lPlaces.stream();
		category = new CategoryDTO(pJson.get("category"));
		lastUpdate = pJson.get("lastUpdate").asLong();
		build();
	}
	public TaskDTO(Task pTask) {
		super(mapper.getNodeFactory());

		uuid = pTask.getUuid();
		text = pTask.getText();
		status = pTask.getStatus();
		
		tags = pTask.getTags().stream().map((pTag) -> {
			return new TagDTO(pTag);
		});
		
		places = pTask.getPlaces().stream().map((pPlace) -> {
			return new PlaceDTO(pPlace);
		});
		
		category = new CategoryDTO(pTask.getCategory());
		
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
	public Stream<TagDTO> getTags() {
		return tags;
	}
	/**
	 * @return the places
	 */
	public Stream<PlaceDTO> getPlaces() {
		return places;
	}
	/**
	 * @return the category
	 */
	public CategoryDTO getCategory() {
		return category;
	}
	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}
}
