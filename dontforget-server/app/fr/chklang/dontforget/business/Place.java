/**
 * 
 */
package fr.chklang.dontforget.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import play.db.ebean.Model;
import fr.chklang.dontforget.ConstantsHelper;
import fr.chklang.dontforget.dao.PlaceDAO;

/**
 * @author Chklang
 *
 */
@Entity
@Table(name="T_PLACE", uniqueConstraints={
		@UniqueConstraint(columnNames={"idUser", "name"})
})
public class Place extends Model {

	/** SVUID */
	private static final long serialVersionUID = 956798745364L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser")
	private User user;
	
	@ManyToOne(targetEntity=String.class)
	@Column(name="name")
	private String name;
	
	@Column(name="lastUpdate", nullable=false)
	private long lastUpdate;
	
	@Column(name="uuid", unique=true, nullable=true)
	private String uuid;
	
	public static final PlaceDAO dao = new PlaceDAO();

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

	@Override
	public void save() {
		super.save();
		if (StringUtils.isEmpty(this.getUuid())) {
			this.setUuid(ConstantsHelper.singleton().getDEVICE_ID() + "_" + getId());
			super.save();
		}
	}
}
