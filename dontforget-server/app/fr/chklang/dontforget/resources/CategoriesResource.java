/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.User;
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
			if (StringUtils.isEmpty(lText)) {
				return status(412);
			}
			Category lCategoryDB = new Category();
			lCategoryDB.setName(lText);
			lCategoryDB.setUser(getConnectedUser());
			lCategoryDB.save();
			
			return ok(new CategoryDTO(lCategoryDB));
		});
	}
	
	public static Result delete(String pTaskCategoryUuid) {
		return executeAndVerifyConnect(() -> {
			User lConnectedUser = getConnectedUser();
			
			Category lCategoryDB = Category.dao.getByUuid(pTaskCategoryUuid);
			if (lCategoryDB == null) {
				return notFound(pTaskCategoryUuid);
			}
			
			if (lCategoryDB.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return unauthorized();
			}

			Set<Task> lTasksToMove = Task.dao.findByCategoryAndUser(lCategoryDB, lConnectedUser);
			if (!lTasksToMove.isEmpty())  {
				return status(412);
			}
			
			
			lCategoryDB.delete();
			return ok();
		});
	}
	
	public static Result moveAndDelete(String pTaskCategoryUuid, String pCategoryUuidReceiver) {
		return executeAndVerifyConnect(() -> {
			User lConnectedUser = getConnectedUser();
			
			Category lCategoryDB = Category.dao.getByUuid(pTaskCategoryUuid);
			if (lCategoryDB == null) {
				return notFound(pTaskCategoryUuid);
			}
			
			Category lCategoryReceiver = Category.dao.getByUuid(pCategoryUuidReceiver);
			if (lCategoryReceiver == null) {
				return notFound(pCategoryUuidReceiver);
			}
			
			if (lCategoryDB.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return unauthorized();
			}
			
			if (lCategoryReceiver.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return unauthorized();
			}
			
			Set<Task> lTasksToMove = Task.dao.findByCategoryAndUser(lCategoryDB, lConnectedUser);
			for (Task lTaskToMove : lTasksToMove) {
				lTaskToMove.setCategory(lCategoryReceiver);
				lTaskToMove.save();
			}
			
			lCategoryDB.delete();
			return ok();
		});
	}
	
	public static Result update(String pTaskCategoryUuid) {
		return executeAndVerifyConnect(() -> {
			User lConnectedUser = getConnectedUser();
			Category lCategoryDB = Category.dao.getByUuid(pTaskCategoryUuid);
			if (lCategoryDB == null) {
				return notFound();
			}
			
			if (lCategoryDB.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return unauthorized();
			}

			String lName = request().body().asText();
			lCategoryDB.setName(lName);
			lCategoryDB.save();
			return ok(new CategoryDTO(lCategoryDB));
		});
	}
	
}
