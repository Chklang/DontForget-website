/**
 * 
 */
package fr.chklang.dontforget.android.dto;

import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;


/**
 * @author Chklang
 *
 */
public class CategoryDTO {
	
	private int id;
	
	private String name;
	
	public CategoryDTO() {
		
	}
	
	public CategoryDTO(JSONObject pJson) {
		try {
			id = pJson.getInt("id");
			name = pJson.getString("name");
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
