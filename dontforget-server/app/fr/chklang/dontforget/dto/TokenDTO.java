package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Token;

public class TokenDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private final String pseudo;
	
	private final String token;
	
	private final String deviceId;
	
	private final long lastUpdate;

	public TokenDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		pseudo = pJson.get("pseudo").asText();
		token = pJson.get("token").asText();
		deviceId = pJson.get("deviceId").asText();
		lastUpdate = pJson.get("lastUpdate").asLong();
	}
	
	public TokenDTO(Token pToken) {
		super(mapper.getNodeFactory());

		pseudo = pToken.getUser().getPseudo();
		token = pToken.getToken();
		deviceId = pToken.getDeviceId();
		lastUpdate = pToken.getLastUpdate();
		
		build();
	}
	
	private void build() {
		this.put("pseudo", pseudo);
		this.put("token", token);
		this.put("deviceId", deviceId);
		this.put("lastUpdate", lastUpdate);
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

}
