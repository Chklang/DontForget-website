/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.TaskCategorie;

/**
 * @author Chklang
 *
 */
public class TaskCategorieDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public TaskCategorieDTO(TaskCategorie pTaskCategorie) {
		super(mapper.getNodeFactory());
		
		this.put("id", pTaskCategorie.getId());
		
		this.put("name", pTaskCategorie.getName());
	}
}
