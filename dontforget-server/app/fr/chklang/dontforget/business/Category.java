package fr.chklang.dontforget.business;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import play.db.ebean.Model;
import fr.chklang.dontforget.ConstantsHelper;
import fr.chklang.dontforget.dao.CategoryDAO;

@Entity
@Table(name="T_CATEGORY", uniqueConstraints={
		@UniqueConstraint(columnNames={"idUser", "name"})
})
public class Category extends Model {
	/** SVUID */
	private static final long serialVersionUID = 56789876434L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private int id;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser")
	private User user;

	@Column(name="name")
	private String name;
	
	@Column(name="lastUpdate", nullable=false)
	private long lastUpdate;
	
	@Column(name="uuid", unique=true, nullable=true)
	private String uuid;
	
	@OneToMany(targetEntity=Task.class, fetch=FetchType.LAZY, mappedBy="category")
	private List<Task> tasks;
	
	public static final CategoryDAO dao = new CategoryDAO();

	public Category() {
		super();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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

	/**
	 * @return the tasks
	 */
	public List<Task> getTasks() {
		return tasks;
	}

	/**
	 * @param tasks the tasks to set
	 */
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public void save() {
		super.save();
		if (StringUtils.isEmpty(this.getUuid())) {
			this.setUuid(ConstantsHelper.singleton().getDEVICE_ID() + "_" + getId());
			super.save();
		}
	}
}
