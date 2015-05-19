/**
 * 
 */
package fr.chklang.dontforget.android.business;

/**
 * @author S0075724
 *
 */
public class AbstractBusinessObject {

	private boolean alreadyIntoDB;
	
	public AbstractBusinessObject() {
		alreadyIntoDB = false;
	}

	/**
	 * @return the alreadyIntoDB
	 */
	public boolean isAlreadyIntoDB() {
		return alreadyIntoDB;
	}

	/**
	 * @param alreadyIntoDB the alreadyIntoDB to set
	 */
	public void setAlreadyIntoDB(boolean alreadyIntoDB) {
		this.alreadyIntoDB = alreadyIntoDB;
	}
}
