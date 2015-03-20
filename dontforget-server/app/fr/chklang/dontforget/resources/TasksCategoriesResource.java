/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.List;

import play.Logger;
import play.i18n.Lang;
import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.TaskCategorie;
import fr.chklang.dontforget.business.DefaultTasksCategoriesManager;
import fr.chklang.dontforget.dto.TaskCategorieDTO;
import fr.chklang.dontforget.managers.LangsManager;

/**
 * @author Chklang
 *
 */
public class TasksCategoriesResource extends AbstractRest {

	public static Result findAll() {
		List<Lang> lLangs = request().acceptLanguages();
		
		DefaultTasksCategoriesManager lTasksCategoriesManager = DefaultTasksCategoriesManager.getInstance();
		List<TaskCategorie> lTaskCategories = null;
		Logger.debug("Prefered langs : " + lLangs);
		for (Lang lLang : lLangs) {
			lTaskCategories = lTasksCategoriesManager.getTasks(lLang);
			if (lTaskCategories != null) {
				break;
			}
		}
		if (lTaskCategories == null) {
			lTaskCategories = lTasksCategoriesManager.getTasks(LangsManager.getInstance().getDefaultLang());
		}
		
		ObjectNode lFactory = Json.newObject();
		ArrayNode lResults = lFactory.arrayNode();
		for (TaskCategorie lTaskCategorie : lTaskCategories) {
			lResults.add(new TaskCategorieDTO(lTaskCategorie));
		}
		return ok(lResults);
	}
	
}
