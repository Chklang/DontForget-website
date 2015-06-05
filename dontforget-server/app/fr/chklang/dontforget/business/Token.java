/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.TokenDAO;

/**
 * @author S0075724
 *
 */
@Entity
@Table(name="T_TOKEN")
public class Token extends Model {
	
	/** SVUID */
	private static final long serialVersionUID = -5504243752685313994L;

	@EmbeddedId
	private TokenKey key;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser", referencedColumnName="idUser", updatable=false, insertable=false)
	private User user;
	
	@Column(name="token", updatable=false, insertable=false)
	private String token;
	
	@Column(name="deviceId", length=36)
	private String deviceId;
	
	@Column(name="lastUpdate", nullable=false)
	private long lastUpdate;
	
	public static final TokenDAO dao = new TokenDAO();

	/**
	 * @return the key
	 */
	public TokenKey getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(TokenKey key) {
		this.key = key;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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

	/**
	 * @return the lastUpdate
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
