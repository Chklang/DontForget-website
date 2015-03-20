/**
 * 
 */
package fr.chklang.dontforget.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.i18n.Lang;
import fr.chklang.dontforget.exceptions.DontForgetException;

/**
 * @author S0075724
 *
 */
public class DefaultTasksCategoriesManager {

	private static final Map<Lang, List<TaskCategorie>> values = new HashMap<>();
	
	private static final DefaultTasksCategoriesManager SINGLETON = new DefaultTasksCategoriesManager();
	
	private DefaultTasksCategoriesManager() {
		
	}
	
	public void addValue(String pLang, int pId, String pValue) {
		Lang lLang = Lang.forCode(pLang);
		List<TaskCategorie> lValues = values.get(lLang);
		if (lValues == null) {
			lValues = new ArrayList<>();
			values.put(lLang, lValues);
		}
		for (TaskCategorie lTaskCategorieFromValues : lValues) {
			if (lTaskCategorieFromValues.getName().equalsIgnoreCase(pValue)) {
				throw new DontForgetException("Value " + pValue + " is already added for lang " + pLang + ". It was inserted with index " + lTaskCategorieFromValues.getId());
			}
		}

		TaskCategorie lTaskCategorie = new TaskCategorie(pId, pValue);
		lValues.add(lTaskCategorie);
	}
	
	public List<TaskCategorie> getTasks(Lang pLang) {
		List<TaskCategorie> lResults = values.get(pLang);
		if (lResults == null) {
			return null;
		}
		return Collections.unmodifiableList(lResults);
	}
	
	public static DefaultTasksCategoriesManager getInstance() {
		return SINGLETON;
	}
	
}
