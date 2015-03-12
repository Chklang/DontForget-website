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

import play.db.ebean.Model;
import fr.chklang.dontforget.dao.PlaceDAO;

/**
 * @author Chklang
 *
 */
@Entity
@Table(name="T_PLACE", uniqueConstraints={
		@UniqueConstraint(columnNames={"idUser", "place"})
})
public class Place extends Model {

	/** SVUID */
	private static final long serialVersionUID = 956798745364L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idPlace;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser")
	private User user;
	
	@ManyToOne(targetEntity=String.class)
	@Column(name="place")
	private String place;
	
	public static final PlaceDAO dao = new PlaceDAO();

	/**
	 * @return the idPlace
	 */
	public int getIdPlace() {
		return idPlace;
	}

	/**
	 * @param idPlace the idPlace to set
	 */
	public void setIdPlace(int idPlace) {
		this.idPlace = idPlace;
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
	 * @return the place
	 */
	public String getPlace() {
		return place;
	}

	/**
	 * @param place the place to set
	 */
	public void setPlace(String place) {
		this.place = place;
	}
}
