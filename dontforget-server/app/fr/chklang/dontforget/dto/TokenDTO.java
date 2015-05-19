package fr.chklang.dontforget.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Token;

public class TokenDTO extends ObjectNode {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	private String pseudo;
	
	private String token;
	
	private String deviceId;

	public TokenDTO(JsonNode pJson) {
		super(mapper.getNodeFactory());
		
		pseudo = pJson.get("pseudo").asText();
		token = pJson.get("token").asText();
		deviceId = pJson.get("deviceId").asText();
	}
	
	public TokenDTO(Token pToken) {
		super(mapper.getNodeFactory());

		pseudo = pToken.getUser().getPseudo();
		token = pToken.getToken();
		deviceId = pToken.getDeviceId();
		
		build();
	}
	
	private void build() {
		this.put("pseudo", pseudo);
		this.put("token", token);
		this.put("deviceId", deviceId);
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @param pseudo the pseudo to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
