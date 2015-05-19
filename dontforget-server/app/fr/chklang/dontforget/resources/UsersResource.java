/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.UserDTO;
import fr.chklang.dontforget.exceptions.WebException;
import fr.chklang.dontforget.helpers.SessionHelper;
import fr.chklang.dontforget.managers.CategoriesManager;

/**
 * @author Chklang
 *
 */
public class UsersResource extends AbstractRest {

	public static Result create() {
		return execute(() -> {
			JsonNode lJson = request().body().asJson();
			String lPseudo = getMandatoryValueAsString(lJson, "pseudo");
			String lPassword = getMandatoryValueAsString(lJson, "password");
			String lMail = getMandatoryValueAsString(lJson, "mail");
			String lCodelang = getValueAsString(lJson, "codelang");
			
			lPassword = DigestUtils.sha1Hex(lPassword);

			User lUser = User.dao.findByPseudo(lPseudo);
			if (lUser != null) {
				return status(409);
			}

			lUser = new User();
			lUser.setPseudo(lPseudo);
			lUser.setPassword(lPassword);
			lUser.setDateInscription(System.currentTimeMillis());
			lUser.setMail(lMail);
			lUser.setCodelang(lCodelang);
			lUser.save();

			SessionHelper.setPlayerId(session(), lUser);

			// Create categories
			CategoriesManager lCategoriesManager = CategoriesManager.getInstance();
			List<String> lDefaultCategories = lCategoriesManager.getTasks(request().acceptLanguages());
			for (String lDefaultCategory : lDefaultCategories) {
				Category lCategory = new Category();
				lCategory.setName(lDefaultCategory);
				lCategory.setUser(lUser);
				lCategory.save();
			}

			return ok();
		});
	}

	public static Result login() {
		return execute(() -> {
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
			return ok(new UserDTO(lUser));
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

	public static Result update() {
		return executeAndVerifyConnect(() -> {
			User lUser = getConnectedUser();

			JsonNode lJson = request().body().asJson();
			String lPseudo = getValueAsString(lJson, "pseudo");
			String lPassword = getValueAsString(lJson, "password");
			String lMail = getValueAsString(lJson, "mail");
			String lCodelang = getValueAsString(lJson, "codelang");

			if (!StringUtils.isEmpty(lPseudo)) {
				User lUserFromDB = User.dao.findByPseudo(lPseudo);
				if (lUserFromDB != null && lUserFromDB.getIdUser() != lUser.getIdUser()) {
					throw new WebException(status(409), "Pseudo " + lPseudo + " is already used");
				}
			}
			
			if (!StringUtils.isEmpty(lPseudo)) {
				lUser.setPseudo(lPseudo);
			}
			if (!StringUtils.isEmpty(lPassword)) {
				lPassword = DigestUtils.sha1Hex(lPassword);
				lUser.setPassword(lPassword);
			}
			if (!StringUtils.isEmpty(lMail)) {
				lUser.setMail(lMail);
			}
			if (!StringUtils.isEmpty(lCodelang)) {
				lUser.setCodelang(lCodelang);
			}
			
			lUser.save();

			// Update field
			return ok(new UserDTO(lUser));
		});
	}

}
