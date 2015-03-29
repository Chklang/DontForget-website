/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;

/**
 * @author Chklang
 *
 */
public class TaskDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public TaskDTO(Task pTask) {
		super(mapper.getNodeFactory());

		this.put("id", pTask.getIdTask());
		this.put("text", pTask.getText());
		this.put("status", pTask.getStatus().name());
		
		ArrayNode lTags = this.arrayNode();
		for (Tag lTag : pTask.getTags()) {
			lTags.add(new TagDTO(lTag));
		}
		this.put("tags", lTags);
		
		ArrayNode lPlaces = this.arrayNode();
		for (Place lPlace : pTask.getPlaces()) {
			lPlaces.add(new PlaceDTO(lPlace));
		}
		this.put("places", lPlaces);
		
		this.put("category", pTask.getCategory().getName());
	}
}
