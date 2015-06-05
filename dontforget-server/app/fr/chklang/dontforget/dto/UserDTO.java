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
	
	private final String codelang;
	
	private final long dateInscription;

	public UserDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		id = pJson.get("id").asInt();
		pseudo = pJson.get("pseudo").asText();
		mail = pJson.get("mail").asText();
		codelang = pJson.get("codelang").asText();
		dateInscription = pJson.get("dateInscription").asLong();
	}
	
	public UserDTO(User pUser) {
		super(mapper.getNodeFactory());

		id = pUser.getIdUser();
		pseudo = pUser.getPseudo();
		mail = pUser.getMail();
		codelang = pUser.getCodelang();
		dateInscription = pUser.getDateInscription();
		
		build();
	}
	
	private void build() {
		this.put("id", id);
		this.put("pseudo", pseudo);
		this.put("mail", mail);
		this.put("codelang", codelang);
		this.put("dateInscription", dateInscription);
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

	public String getCodelang() {
		return codelang;
	}

	public long getDateInscription() {
		return dateInscription;
	}
}
