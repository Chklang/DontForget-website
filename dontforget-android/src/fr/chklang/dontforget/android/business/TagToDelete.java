/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.dao.TagToDeleteDAO;

/**
 * @author S0075724
 *
 */
public class TagToDelete extends AbstractBusinessObject {
	
	private String uuidTag;
	
	private long dateDeletion;
	
	public static final TagToDeleteDAO dao = new TagToDeleteDAO();

	/**
	 * @return the uuidTag
	 */
	public String getUuidTag() {
		return uuidTag;
	}

	/**
	 * @param uuidTag the uuidTag to set
	 */
	public void setUuidTag(String uuidTag) {
		this.uuidTag = uuidTag;
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
		result = prime * result + ((uuidTag == null) ? 0 : uuidTag.hashCode());
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
		TagToDelete other = (TagToDelete) obj;
		if (dateDeletion != other.dateDeletion)
			return false;
		if (uuidTag == null) {
			if (other.uuidTag != null)
				return false;
		} else if (!uuidTag.equals(other.uuidTag))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TagToDelete [uuidTag=" + uuidTag + ", dateDeletion=" + dateDeletion + "]";
	}
}
