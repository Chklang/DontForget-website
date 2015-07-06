/**
 * 
 */
package fr.chklang.dontforget;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.i18n.Lang;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;
import fr.chklang.dontforget.business.Configuration;
import fr.chklang.dontforget.exceptions.WebException;
import fr.chklang.dontforget.managers.CategoriesManager;
import fr.chklang.dontforget.managers.LangsManager;

/**
 * @author Chklang
 *
 */
public class ApplicationGlobal extends GlobalSettings {
	
	public static final String VERSION = "1.0.0";
	
	public static final String CONFIGURATION_KEY_DEVICE_ID = "DEVICE_ID";
	
	@Override
    public void onStart(Application app) {
		String lDefaultLanguage = app.configuration().getString("application.defautlLang");
		LangsManager.getInstance().setDefaultLang(lDefaultLanguage);
		
		Object lTasksCategoriesObject = app.configuration().getObject("tasks.categorie");
		
		@SuppressWarnings("unchecked")
		Map<String, List<String>> lTasksCategories = (Map<String, List<String>>) lTasksCategoriesObject;
		
		CategoriesManager lTasksCategoriesManager = CategoriesManager.getInstance();
		lTasksCategoriesManager.reset();
		
		for (Entry<String, List<String>> lEntry : lTasksCategories.entrySet()) {
			String lLanguage = lEntry.getKey();
			int lIndex = 0;
			for (String lValue : lEntry.getValue()) {
				lTasksCategoriesManager.addValue(lLanguage, lIndex, lValue);
				lIndex++;
			}
		}
		
		for (Entry<String, List<String>> lEntry : lTasksCategories.entrySet()) {
			Lang lLang = Lang.forCode(lEntry.getKey());
			List<String> lTasksList = lTasksCategoriesManager.getTasks(lLang);
			for (String lTaskCategorie : lTasksList) {
				Logger.debug("For lang " + lLang + ", key : " + lTaskCategorie);
			}
		}
		
		checkConfiguration();
		
		//Check database datas
		DatabaseVersionManager.checkDatabase();
    }

	@Override
	public Promise<Result> onError(RequestHeader pRequest, Throwable pError) {
		if (pError != null && pError.getCause() instanceof WebException) {
			return Promise.<Result> pure(((WebException)pError.getCause()).getResult());
		}
		return Promise.<Result> pure(Results.internalServerError());
	}
	
	private void checkConfiguration() {
		Configuration lConfiguration = Configuration.dao.byId(CONFIGURATION_KEY_DEVICE_ID);
		if (lConfiguration == null) {
			lConfiguration = new Configuration();
			lConfiguration.setKey(CONFIGURATION_KEY_DEVICE_ID);
			lConfiguration.setValue(UUID.randomUUID().toString());
			lConfiguration.save();
		}
	}
}
