package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.User;

public class UserDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final int id;
	
	private final String pseudo;
	
	private final String mail;
	
	private final long dateInscription;
	
	private final long lastUpdate;

	public UserDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		id = pJson.get("id").asInt();
		pseudo = pJson.get("pseudo").asText();
		mail = pJson.get("mail").asText();
		dateInscription = pJson.get("dateInscription").asLong();
		lastUpdate = pJson.get("lastUpdate").asLong();
	}
	
	public UserDTO(User pUser) {
		super(mapper.getNodeFactory());

		id = pUser.getIdUser();
		pseudo = pUser.getPseudo();
		mail = pUser.getMail();
		dateInscription = pUser.getDateInscription();
		lastUpdate = pUser.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("id", id);
		this.put("pseudo", pseudo);
		this.put("mail", mail);
		this.put("dateInscription", dateInscription);
		
		this.put("lastUpdate", lastUpdate);
	}

	public int getId() {
		return id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public String getMail() {
		return mail;
	}

	public long getDateInscription() {
		return dateInscription;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}
}
