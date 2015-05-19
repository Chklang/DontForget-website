package fr.chklang.dontforget.android.dto;

public enum TaskStatus {
	OPENED(1),
	FINISHED(2),
	DELETED(3);
	
	private final int idStatus;
	TaskStatus(int pIdStatus) {
		idStatus = pIdStatus;
	}
	/**
	 * @return the idStatus
	 */
	public int getIdStatus() {
		return idStatus;
	}
	
	public static TaskStatus getById(int pIdStatus) {
		for (TaskStatus lStatus : values()) {
			if (pIdStatus == lStatus.idStatus) {
				return lStatus;
			}
		}
		return null;
	}
}
