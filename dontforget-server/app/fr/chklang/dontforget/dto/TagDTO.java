/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Tag;

/**
 * @author Chklang
 *
 */
public class TagDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public TagDTO(Tag pTag) {
		super(mapper.getNodeFactory());
		
		this.put("id", pTag.getId());
		
		this.put("name", pTag.getName());
	}
}
