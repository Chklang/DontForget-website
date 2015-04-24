/**
 * 
 */
package fr.chklang.dontforget.business;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.TaskDAO;

/**
 * @author Chklang
 *
 */
@Entity
@Table(name="T_TASK")
public class Task extends Model {

	/** SVUID */
	private static final long serialVersionUID = 9868479689553434L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idTask")
	private int idTask;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser")
	private User user;
	
	@Column(name="text", length=5000, nullable=false)
	private String text;
	
	@ManyToMany(targetEntity=Tag.class)
	private Set<Tag> tags;
	
	@ManyToMany(targetEntity=Place.class)
	private Set<Place> places;

	@ManyToOne(targetEntity=Category.class)
	@Column(name="idCategory")
	private Category category;
	
	@Enumerated(EnumType.STRING)
	@Column(name="status", length=8, nullable=false)
	private TaskStatus status;
	
	@Column(name="lastUpdate", nullable=false)
	private long lastUpdate;
	
	public static final TaskDAO dao = new TaskDAO();

	/**
	 * @return the idTask
	 */
	public int getIdTask() {
		return idTask;
	}

	/**
	 * @param idTask the idTask to set
	 */
	public void setIdTask(int idTask) {
		this.idTask = idTask;
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
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the tags
	 */
	public Set<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * @return the places
	 */
	public Set<Place> getPlaces() {
		return places;
	}

	/**
	 * @param places the places to set
	 */
	public void setPlaces(Set<Place> places) {
		this.places = places;
	}

	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
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
