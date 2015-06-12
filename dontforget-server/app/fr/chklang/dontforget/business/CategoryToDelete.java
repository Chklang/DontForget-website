/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.CategoryToDeleteDAO;

/**
 * @author S0075724
 *
 */
@Entity
@Table(name="T_CATEGORY_TO_DELETE")
public class CategoryToDelete extends Model {

	/** SVUID */
	private static final long serialVersionUID = 1189141668070057944L;

	@Id
	@Column(name="uuidCategory")
	private String uuidCategory;
	
	@Column(name="dateDeletion")
	private long dateDeletion;
	
	@Column(name="idUser")
	private int idUser;
	
	public static final CategoryToDeleteDAO dao = new CategoryToDeleteDAO();

	/**
	 * @return the uuidCategory
	 */
	public String getUuidCategory() {
		return uuidCategory;
	}

	/**
	 * @param uuidCategory the uuidCategory to set
	 */
	public void setUuidCategory(String uuidCategory) {
		this.uuidCategory = uuidCategory;
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
		result = prime * result + ((uuidCategory == null) ? 0 : uuidCategory.hashCode());
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
		CategoryToDelete other = (CategoryToDelete) obj;
		if (dateDeletion != other.dateDeletion)
			return false;
		if (idUser != other.idUser)
			return false;
		if (uuidCategory == null) {
			if (other.uuidCategory != null)
				return false;
		} else if (!uuidCategory.equals(other.uuidCategory))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CategoryToDelete [uuidCategory=" + uuidCategory + ", dateDeletion=" + dateDeletion + ", idUser=" + idUser + "]";
	}
}
