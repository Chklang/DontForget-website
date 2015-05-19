/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.dao.TagDAO;

/**
 * @author S0075724
 *
 */
public class Tag extends AbstractBusinessObject {

	private int idTag;
	
	private String name;
	
	private long lastUpdate;
	
	private String uuid;
	
	public static final TagDAO dao = new TagDAO();

	/**
	 * @return the idTag
	 */
	public int getIdTag() {
		return idTag;
	}

	/**
	 * @param idTag the idTag to set
	 */
	public void setIdTag(int idTag) {
		this.idTag = idTag;
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

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTag;
		result = prime * result + (int) (lastUpdate ^ (lastUpdate >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		Tag other = (Tag) obj;
		if (idTag != other.idTag)
			return false;
		if (lastUpdate != other.lastUpdate)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tag [idTag=" + idTag + ", name=" + name + ", lastUpdate=" + lastUpdate + ", uuid=" + uuid + "]";
	}
}
