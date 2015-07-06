/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.PlaceToDeleteDAO;

/**
 * @author S0075724
 *
 */
@Entity
@Table(name="T_PLACE_TO_DELETE")
public class PlaceToDelete extends Model {

	/** SVUID */
	private static final long serialVersionUID = 1189141668070057944L;

	@Id
	@Column(name="uuidPlace")
	private String uuidPlace;
	
	@Column(name="dateDeletion")
	private long dateDeletion;
	
	@Column(name="idUser")
	private int idUser;
	
	public static final PlaceToDeleteDAO dao = new PlaceToDeleteDAO();

	/**
	 * @return the uuidPlace
	 */
	public String getUuidPlace() {
		return uuidPlace;
	}

	/**
	 * @param uuidPlace the uuidPlace to set
	 */
	public void setUuidPlace(String uuidPlace) {
		this.uuidPlace = uuidPlace;
	}

	/**
	 * @return the dateDeletion
	 */
	public long getDateDeletion() {
		return dateDeletion;
	}

	/**
	 * @param dateDeletion the dateDeletion to set
	 */
	public void setDateDeletion(long dateDeletion) {
		this.dateDeletion = dateDeletion;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (dateDeletion ^ (dateDeletion >>> 32));
		result = prime * result + idUser;
		result = prime * result + ((uuidPlace == null) ? 0 : uuidPlace.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlaceToDelete other = (PlaceToDelete) obj;
		if (dateDeletion != other.dateDeletion)
			return false;
		if (idUser != other.idUser)
			return false;
		if (uuidPlace == null) {
			if (other.uuidPlace != null)
				return false;
		} else if (!uuidPlace.equals(other.uuidPlace))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlaceToDelete [uuidPlace=" + uuidPlace + ", dateDeletion=" + dateDeletion + ", idUser=" + idUser + "]";
	}

}
