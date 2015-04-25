/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.dto.CategoryDTO;

/**
 * @author Chklang
 *
 */
public class CategoriesResource extends AbstractRest {

	public static Result findAll() {
		return executeAndVerifyConnect(() -> {
			Set<Category> lCategories = Category.dao.findByUser(getConnectedUser());
			ObjectNode lFactory = Json.newObject();
			ArrayNode lResults = lFactory.arrayNode();
			for (Category lCategory : lCategories) {
				lResults.add(new CategoryDTO(lCategory));
			}
			return ok(lResults);
		});
	}

	public static Result create() {
		return executeAndVerifyConnect(() -> {
			String lText = request().body().asText();
			Category lCategoryDB = Category.dao.findByNameAndUser(lText, getConnectedUser());
			if (lCategoryDB != null) {
				return status(409);
			}
			lCategoryDB = new Category();
			lCategoryDB.setName(lText);
			lCategoryDB.setUser(getConnectedUser());
			lCategoryDB.save();
			return ok(new CategoryDTO(lCategoryDB));
		});
	}
	
	public static Result delete(String pTaskCategoryName) {
		return executeAndVerifyConnect(() -> {
			Category lCategoryDB = Category.dao.findByNameAndUser(pTaskCategoryName, getConnectedUser());
			if (lCategoryDB == null) {
				return notFound();
			}
			lCategoryDB.delete();
			return ok();
		});
	}
	
	public static Result update(String pTaskCategoryName) {
		return executeAndVerifyConnect(() -> {
			Category lCategoryDB = Category.dao.findByNameAndUser(pTaskCategoryName, getConnectedUser());
			if (lCategoryDB == null) {
				return notFound();
			}

			String lName = request().body().asText();
			if (!StringUtils.isEmpty(lName) && !lCategoryDB.getName().equals(lName)) {
				Category lCategoryDB2 = Category.dao.findByNameAndUser(lName, getConnectedUser());
				if (lCategoryDB2 != null) {
					return conflict();
				}
				lCategoryDB.setName(lName);
			}
			lCategoryDB.save();
			return ok(new CategoryDTO(lCategoryDB));
		});
	}
	
}
