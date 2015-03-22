/**
 * 
 */
package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Place;

/**
 * @author Chklang
 *
 */
public class PlaceDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public PlaceDTO(Place pPlace) {
		super(mapper.getNodeFactory());
		
		this.put("id", pPlace.getId());
		
		this.put("name", pPlace.getName());
	}
}
