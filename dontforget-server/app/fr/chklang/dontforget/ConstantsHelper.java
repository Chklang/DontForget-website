/**
 * 
 */
package fr.chklang.dontforget;

import fr.chklang.dontforget.business.Configuration;

/**
 * @author S0075724
 *
 */
public class ConstantsHelper {
	
	private static ConstantsHelper singleton;
	
	private final String DEVICE_ID;
	
	private ConstantsHelper () {
		Configuration lConfiguration = Configuration.dao.byId(ApplicationGlobal.CONFIGURATION_KEY_DEVICE_ID);
		DEVICE_ID = lConfiguration.getValue();
	}

	/**
	 * @return the dEVICE_ID
	 */
	public String getDEVICE_ID() {
		return DEVICE_ID;
	}

	public static ConstantsHelper singleton() {
		if (singleton == null) {
			synchronized (ConstantsHelper.class) {
				if (singleton == null) {
					singleton = new ConstantsHelper();
				}
			}
		}
		return singleton;
	}
}
