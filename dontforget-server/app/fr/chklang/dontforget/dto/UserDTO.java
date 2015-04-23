package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.User;

public class UserDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final int id;
	
	private final String pseudo;
	
	private final long dateInscription;
	
	private final long lastUpdate;

	public UserDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		id = pJson.get("id").asInt();
		pseudo = pJson.get("pseudo").asText();
		dateInscription = pJson.get("dateInscription").asLong();
		lastUpdate = pJson.get("lastUpdate").asLong();
	}
	
	public UserDTO(User pUser) {
		super(mapper.getNodeFactory());

		id = pUser.getIdUser();
		pseudo = pUser.getPseudo();
		dateInscription = pUser.getDateInscription();
		lastUpdate = pUser.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("id", id);
		this.put("pseudo", pseudo);
		this.put("dateInscription", dateInscription);
		
		this.put("lastUpdate", lastUpdate);
	}
}
