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
public class TagDTO {
	
	private int id;
	
	private String name;
	
	public TagDTO () {
		
	}
	public TagDTO (JSONObject pObject) {
		super();
		try {
			id = pObject.getInt("id");
			name = pObject.getString("name");
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
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
