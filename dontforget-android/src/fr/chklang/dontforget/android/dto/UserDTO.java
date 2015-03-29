package fr.chklang.dontforget.android.dto;

import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;


public class UserDTO {
	
	private int id;
	
	private String pseudo;
	
	private long dateInsription;
	
	public UserDTO() {
		
	}
	
	public UserDTO(JSONObject pJson) {
		try {
			id = pJson.getInt("id");
			pseudo = pJson.getString("pseudo");
			dateInsription = pJson.getLong("dateInsription");
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);//TODO
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the dateInsription
	 */
	public long getDateInsription() {
		return dateInsription;
	}

	/**
	 * @param dateInsription the dateInsription to set
	 */
	public void setDateInsription(long dateInsription) {
		this.dateInsription = dateInsription;
	}
}
