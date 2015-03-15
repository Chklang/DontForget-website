package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;

public class TaskDAO extends Finder<Integer, Task> {

	public TaskDAO() {
		super(Integer.class, Task.class);
	}
	
	public List<Task> findByUser(User pUser) {
		return this.where().eq("user", pUser).findList();
	}
}