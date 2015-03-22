/**
 * 
 */
package fr.chklang.dontforget.managers;

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
public class CategoriesManager {

	private static final Map<Lang, List<String>> values = new HashMap<>();
	
	private static final CategoriesManager SINGLETON = new CategoriesManager();
	
	private CategoriesManager() {
		
	}
	
	public void addValue(String pLang, int pId, String pValue) {
		Lang lLang = Lang.forCode(pLang);
		List<String> lValues = values.get(lLang);
		if (lValues == null) {
			lValues = new ArrayList<>();
			values.put(lLang, lValues);
		}
		for (String lTaskCategorieFromValues : lValues) {
			if (lTaskCategorieFromValues.equalsIgnoreCase(pValue)) {
				throw new DontForgetException("Value " + pValue + " is already added for lang " + pLang + ".");
			}
		}

		lValues.add(pValue);
	}
	
	public List<String> getTasks(List<Lang> lLangs) {
		List<String> lTasksCategories = null;
		
		for (Lang lLang : lLangs) {
			lTasksCategories = this.getTasks(lLang);
			if (lTasksCategories != null) {
				break;
			}
		}
		if (lTasksCategories == null) {
			lTasksCategories = this.getTasks(LangsManager.getInstance().getDefaultLang());
		}
		
		return lTasksCategories;
	}
	public List<String> getTasks(Lang pLang) {
		List<String> lResults = values.get(pLang);
		if (lResults == null) {
			return null;
		}
		return Collections.unmodifiableList(lResults);
	}
	
	public static CategoriesManager getInstance() {
		return SINGLETON;
	}
	
}
