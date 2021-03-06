/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.TaskToDelete;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.TokenKey;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.TokenDTO;
import fr.chklang.dontforget.dto.UserDTO;
import fr.chklang.dontforget.helpers.SessionHelper;

/**
 * @author S0075724
 *
 */
public class TokenResource extends AbstractRest {

	public static Result create() {
		return execute(() -> {
			JsonNode lJson = request().body().asJson();
			String lPseudo = getMandatoryValueAsString(lJson, "pseudo");
			String lPassword = getMandatoryValueAsString(lJson, "password");
			String lDeviceId = getMandatoryValueAsString(lJson, "deviceId");
			lPassword = DigestUtils.sha1Hex(lPassword);

			User lUser = User.dao.findByPseudo(lPseudo);
			if (lUser == null) {
				return unauthorized();
			}
			if (!lUser.getPassword().equals(lPassword)) {
				return unauthorized();
			}

			SessionHelper.setPlayerId(session(), lUser);
			
			//Create the token
			TokenKey lTokenKey = new TokenKey();
			lTokenKey.setIdUser(lUser.getIdUser());
			lTokenKey.setToken(UUID.randomUUID().toString());
			
			Token lToken = new Token();
			lToken.setKey(lTokenKey);
			lToken.setUser(lUser);
			lToken.setToken(lTokenKey.getToken());
			lToken.setDeviceId(lDeviceId);
			lToken.save();
			
			return ok(new TokenDTO(lToken));
		});
	}

	public static Result login() {
		return execute(() -> {
			JsonNode lJson = request().body().asJson();
			String lPseudo = getMandatoryValueAsString(lJson, "pseudo");
			String lTokenString = getMandatoryValueAsString(lJson, "token");
			String lDeviceId = getMandatoryValueAsString(lJson, "deviceId");

			User lUser = User.dao.findByPseudo(lPseudo);
			if (lUser == null) {
				return unauthorized();
			}
			
			TokenKey lTokenKey = new TokenKey();
			lTokenKey.setIdUser(lUser.getIdUser());
			lTokenKey.setToken(lTokenString);
			
			Token lToken = Token.dao.byId(lTokenKey);
			if (lToken == null) {
				return unauthorized();
			}
			if (!lToken.getDeviceId().equals(lDeviceId)) {
				return unauthorized();
			}

			SessionHelper.setPlayerId(session(), lUser);
			SessionHelper.setTokenId(session(), lToken);
			
			
			return ok(new UserDTO(lUser));
		});
	}
	
	public static Result saveUpdatedToken(long pDate) {
		return executeAndVerifyToken(() -> {
			final Token lToken = getConnectedToken();
			lToken.setLastUpdate(pDate);
			lToken.save();
			
			//And update old tokens
			CategoryToDelete.dao.deleteOldObjects(lToken.getUser());
			TagToDelete.dao.deleteOldObjects(lToken.getUser());
			PlaceToDelete.dao.deleteOldObjects(lToken.getUser());
			TaskToDelete.dao.deleteOldObjects(lToken.getUser());
			return ok();
		});
	}
}
