/**
 * 
 */
package fr.chklang.dontforget.android.helpers;

import fr.chklang.dontforget.android.business.Configuration;

/**
 * @author S0075724
 *
 */
public class ConfigurationHelper {

	public static String getDeviceId() {
		//Create device uuid if not already defined
		Configuration lDeviceId = Configuration.dao.get("DEVICE_ID");
		
		return lDeviceId.getValue();
	}
}
