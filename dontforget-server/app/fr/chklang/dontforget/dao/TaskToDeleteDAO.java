package fr.chklang.dontforget.dao;

import java.util.List;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.TaskToDelete;

public class TaskToDeleteDAO extends Finder<String, TaskToDelete> {

	/** SVUID */
	private static final long serialVersionUID = 31535730751549435L;

	public TaskToDeleteDAO() {
		super(String.class, TaskToDelete.class);
	}
	
	public List<TaskToDelete> findAfterDate(long pDate) {
		return this.where().ge("dateDeletion", pDate).findList();
	}
}
