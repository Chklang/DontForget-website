/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Controller;
import play.mvc.Result;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.TxRunnable;
import com.fasterxml.jackson.databind.JsonNode;

import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.TokenKey;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.exceptions.WebException;
import fr.chklang.dontforget.helpers.SessionHelper;

/**
 * @author Chklang
 *
 */
public abstract class AbstractRest extends Controller {
	private static class WrapperResult {
		public Result result;
	}
	
	protected static Result execute(final Callable<Result> pCallable) {
		final WrapperResult lResult = new WrapperResult();
		Ebean.execute(new TxRunnable() {  
			public void run() {  
				try {
					lResult.result = pCallable.call();
				} catch (WebException e) {
					throw e;
				} catch (Throwable e) {
					throw new WebException(status(500), e);
				}
			}
		});
		return lResult.result;
	}
	
	protected static Result executeAndVerifyConnect(final Callable<Result> pCallable) {
		final WrapperResult lResult = new WrapperResult();
		Ebean.execute(new TxRunnable() {  
			public void run() {
				try {
					verifyConnexion();
					lResult.result = pCallable.call();
				} catch (WebException e) {
					throw e;
				} catch (Throwable e) {
					throw new WebException(status(500), e);
				}
			}
		});
		return lResult.result;
	}
	
	protected static Result executeAndVerifyToken(final Callable<Result> pCallable) {
		final WrapperResult lResult = new WrapperResult();
		Ebean.execute(new TxRunnable() {  
			public void run() {
				try {
					verifyToken();
					lResult.result = pCallable.call();
				} catch (WebException e) {
					throw e;
				} catch (Throwable e) {
					throw new WebException(status(500), e);
				}
			}
		});
		return lResult.result;
	}
	
	protected static String getValueAsString(JsonNode pNode, String pKey) {
		if (pNode == null) {
			return null;
		}
		JsonNode lNode = pNode.findPath(pKey);
		if (lNode == null) {
			return null;
		}
		return lNode.textValue();
	}
	
	protected static String getMandatoryValueAsString(JsonNode pNode, String pKey) {
		String lValue = getValueAsString(pNode, pKey);
		if (StringUtils.isEmpty(lValue)) {
			throw new WebException(badRequest("Missing parameter [" + pKey + "]"));
		}
		return lValue;
	}

	protected static void verifyConnexion() {
		if (!SessionHelper.hasPlayerId(session())) {
			throw new WebException(status(401), "Identification requise");
		}
	}
	
	protected static void verifyToken() {
		if (!SessionHelper.hasTokenId(session())) {
			throw new WebException(status(401), "Identification requise");
		}
	}
	
	protected static User getConnectedUser() {
		User lUser = User.dao.byId(SessionHelper.getUserId(session()));
		return lUser;
	}
	
	protected static Token getConnectedToken() {
		int lIdUser = SessionHelper.getUserId(session());
		String lTokenString = SessionHelper.getTokenId(session());
		TokenKey lTokenKey = new TokenKey();
		lTokenKey.setIdUser(lIdUser);
		lTokenKey.setToken(lTokenString);
		return Token.dao.byId(lTokenKey);
	}
	
	
    // -- CONFLICT

    /**
     * Generates a 409 CONFLICT simple result.
     */
	protected static Status conflict() {
        return new Status(play.core.j.JavaResults.Conflict());
    }
}
