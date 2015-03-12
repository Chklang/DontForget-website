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

		this.put("text", pTask.getText());
		
		ArrayNode lTags = this.arrayNode();
		for (Tag lTag : pTask.getTags()) {
			lTags.add(lTag.getTag());
		}
		this.put("tags", lTags);
		
		ArrayNode lPlaces = this.arrayNode();
		for (Place lPlace : pTask.getPlaces()) {
			lPlaces.add(lPlace.getPlace());
		}
		this.put("places", lPlaces);
	}
}
