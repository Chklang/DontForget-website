/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.ConfigurationDAO;

/**
 * @author S0075724
 *
 */
@Entity
@Table(name="T_CONFIGURATION")
public class Configuration extends Model {

	/** SVUID */
	private static final long serialVersionUID = 8078597571683214564L;

	@Id
	@Column(name="confkey", unique=true, nullable=false)
	private String key;
	
	@Column(name="value", nullable=true)
	private String value;
	
	public static final ConfigurationDAO dao = new ConfigurationDAO();

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
