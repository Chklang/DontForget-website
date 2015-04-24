/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.codec.digest.DigestUtils;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.UserDTO;
import fr.chklang.dontforget.helpers.SessionHelper;
import fr.chklang.dontforget.managers.CategoriesManager;

/**
 * @author Chklang
 *
 */
public class UsersResource extends AbstractRest {

	public static Result create() {
		return execute(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				JsonNode lJson = request().body().asJson();
				String lPseudo = getMandatoryValueAsString(lJson, "pseudo");
				String lPassword = getMandatoryValueAsString(lJson, "password");
				lPassword = DigestUtils.sha1Hex(lPassword);
				
				User lUser = User.dao.findByPseudo(lPseudo);
				if (lUser != null) {
					return status(409);
				}
				
				lUser = new User();
				lUser.setPseudo(lPseudo);
				lUser.setPassword(lPassword);
				lUser.setDateInscription(System.currentTimeMillis());
				lUser.save();
				
				SessionHelper.setPlayerId(session(), lUser);
				
				//Create categories
				CategoriesManager lCategoriesManager = CategoriesManager.getInstance();
				List<String> lDefaultCategories = lCategoriesManager.getTasks(request().acceptLanguages());
				for (String lDefaultCategory : lDefaultCategories) {
					Category lCategory = new Category();
					lCategory.setName(lDefaultCategory);
					lCategory.setUser(lUser);
					lCategory.save();
				}
				
				return ok();
			}
		});
	}

	public static Result login() {
		return execute(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				JsonNode lJson = request().body().asJson();
				String lPseudo = getMandatoryValueAsString(lJson, "pseudo");
				String lPassword = getMandatoryValueAsString(lJson, "password");
				lPassword = DigestUtils.sha1Hex(lPassword);
				
				User lUser = User.dao.findByPseudo(lPseudo);
				if (lUser == null) {
					return unauthorized();
				}
				if (!lUser.getPassword().equals(lPassword)) {
					return unauthorized();
				}
				
				SessionHelper.setPlayerId(session(), lUser);
				return ok();
			}
		});
	}

	public static Result disconnect() {
		SessionHelper.clearPlayerId(session());
		return ok();
	}
	
	public static Result me() {
		return executeAndVerifyConnect(() -> {
			User lUser = getConnectedUser();
			return ok(new UserDTO(lUser));
		});
	}

}
