/**
 * 
 */
package fr.chklang.dontforget.business;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
}
