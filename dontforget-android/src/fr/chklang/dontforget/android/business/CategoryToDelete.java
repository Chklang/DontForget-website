/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.dao.CategoryToDeleteDAO;

/**
 * @author S0075724
 *
 */
public class CategoryToDelete extends AbstractBusinessObject {
	
	private String uuidCategory;
	
	private long dateDeletion;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (dateDeletion ^ (dateDeletion >>> 32));
		result = prime * result + ((uuidCategory == null) ? 0 : uuidCategory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryToDelete other = (CategoryToDelete) obj;
		if (dateDeletion != other.dateDeletion)
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
		return "CategoryToDelete [uuidCategory=" + uuidCategory + ", dateDeletion=" + dateDeletion + "]";
	}
}
