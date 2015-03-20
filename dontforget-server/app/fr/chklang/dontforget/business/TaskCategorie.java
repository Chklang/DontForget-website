package fr.chklang.dontforget.business;

public class TaskCategorie {

	private final int id;

	private final String name;

	public TaskCategorie(int pId, String pName) {
		super();
		this.id = pId;
		this.name = pName;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
