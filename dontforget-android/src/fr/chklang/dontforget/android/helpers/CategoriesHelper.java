/**
 * 
 */
package fr.chklang.dontforget.android.helpers;

import fr.chklang.dontforget.android.business.Configuration;
import fr.chklang.dontforget.android.dao.ConfigurationDAO;

/**
 * @author S0075724
 *
 */
public class CategoriesHelper {

	public static String getDeviceId() {
		ConfigurationDAO lConfigurationDAO = new ConfigurationDAO();
		
		//Create device uuid if not already defined
		Configuration lDeviceId = lConfigurationDAO.get("DEVICE_ID");
		
		return lDeviceId.getValue();
	}
}
