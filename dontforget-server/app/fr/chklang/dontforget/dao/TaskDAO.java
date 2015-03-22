package fr.chklang.dontforget.dao;

import java.util.Set;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;

public class TaskDAO extends Finder<Integer, Task> {

	/** SVUID */
	private static final long serialVersionUID = 8206620710705377587L;

	public TaskDAO() {
		super(Integer.class, Task.class);
	}
	
	public Set<Task> findByUser(User pUser) {
		return this.where().eq("user", pUser).findSet();
	}
	
	public Set<Task> findByCategoryAndUser(Category pCategory, User pUser) {
		return this.where().eq("category", pCategory).eq("user", pUser).findSet();
	}
}
