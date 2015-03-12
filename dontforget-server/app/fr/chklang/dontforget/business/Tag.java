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
import fr.chklang.dontforget.dao.TagDAO;

/**
 * @author Chklang
 *
 */
@Entity
@Table(name="T_TAG", uniqueConstraints={
		@UniqueConstraint(columnNames={"idUser", "tag"})
})
public class Tag extends Model {

	/** SVUID */
	private static final long serialVersionUID = 956798745364L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int idTag;
	
	@ManyToOne(targetEntity=User.class)
	@JoinColumn(name="idUser")
	private User user;
	
	@ManyToOne(targetEntity=String.class)
	@Column(name="tag")
	private String tag;
	
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
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
