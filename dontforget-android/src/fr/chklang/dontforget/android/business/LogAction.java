package fr.chklang.dontforget.android.business;

public enum LogAction {
	DELETE(0);
	
	private final int dbValue;
	
	LogAction(int pValue) {
		dbValue = pValue;
	}

	/**
	 * @return the dbValue
	 */
	public int getDbValue() {
		return dbValue;
	}
}
