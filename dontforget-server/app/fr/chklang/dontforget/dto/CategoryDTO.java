/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Category;

/**
 * @author Chklang
 *
 */
public class CategoryDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public CategoryDTO(Category pTaskCategory) {
		super(mapper.getNodeFactory());
		
		this.put("id", pTaskCategory.getId());
		
		this.put("name", pTaskCategory.getName());
	}
}
