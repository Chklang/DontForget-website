/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.TaskToDeleteDAO;

/**
 * @author S0075724
 *
 */
@Entity
@Table(name="T_TASK_TO_DELETE")
public class TaskToDelete extends Model {

	/** SVUID */
	private static final long serialVersionUID = 1189141668070057944L;

	@Id
	@Column(name="uuidTask")
	private String uuidTask;
	
	@Column(name="dateDeletion")
	private long dateDeletion;
	
	public static final TaskToDeleteDAO dao = new TaskToDeleteDAO();

	/**
	 * @return the uuidTask
	 */
	public String getUuidTask() {
		return uuidTask;
	}

	/**
	 * @param uuidTask the uuidTask to set
	 */
	public void setUuidTask(String uuidTask) {
		this.uuidTask = uuidTask;
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
		int result = super.hashCode();
		result = prime * result + (int) (dateDeletion ^ (dateDeletion >>> 32));
		result = prime * result + ((uuidTask == null) ? 0 : uuidTask.hashCode());
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
		TaskToDelete other = (TaskToDelete) obj;
		if (dateDeletion != other.dateDeletion)
			return false;
		if (uuidTask == null) {
			if (other.uuidTask != null)
				return false;
		} else if (!uuidTask.equals(other.uuidTask))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskToDelete [uuidTask=" + uuidTask + ", dateDeletion=" + dateDeletion + "]";
	}
}
