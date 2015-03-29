package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.User;

public class UserDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();

	public UserDTO(User pUser) {
		super(mapper.getNodeFactory());

		this.put("id", pUser.getIdUser());
		this.put("pseudo", pUser.getPseudo());
		this.put("dateInscription", pUser.getDateInscription());
	}
}
