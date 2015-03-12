/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.UserDAO;

/**
 * @author Chklang
 *
 */
@Entity
@Table(name="T_USER")
public class User extends Model {

	/** SVUID */
	private static final long serialVersionUID = 9868479689553434L;

	@Id
	@Column(name="idUser")
	private int idUser;
	
	@Column(name="pseudo", length=32, nullable=false)
	private String pseudo;
	
	@Column(name="password", length=40, nullable=false)
	private String password;
	
	@Column(name="dateInscription", nullable=false)
	private long dateInscription;
	
	public static final UserDAO dao = new UserDAO();

	/**
	 * @return the idUser
	 */
	public int getIdUser() {
		return idUser;
	}

	/**
	 * @param idUser the idUser to set
	 */
	public void setIdUser(int idUser) {
		this.idUser = idUser;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the dateInscription
	 */
	public long getDateInscription() {
		return dateInscription;
	}

	/**
	 * @param dateInscription the dateInscription to set
	 */
	public void setDateInscription(long dateInscription) {
		this.dateInscription = dateInscription;
	}
}
