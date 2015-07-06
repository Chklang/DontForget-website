/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.dao.PlaceToDeleteDAO;

/**
 * @author S0075724
 *
 */
public class PlaceToDelete extends AbstractBusinessObject {
	
	private String uuidPlace;
	
	private long dateDeletion;
	
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (dateDeletion ^ (dateDeletion >>> 32));
		result = prime * result + ((uuidPlace == null) ? 0 : uuidPlace.hashCode());
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
		PlaceToDelete other = (PlaceToDelete) obj;
		if (dateDeletion != other.dateDeletion)
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
		return "PlaceToDelete [uuidPlace=" + uuidPlace + ", dateDeletion=" + dateDeletion + "]";
	}
}
