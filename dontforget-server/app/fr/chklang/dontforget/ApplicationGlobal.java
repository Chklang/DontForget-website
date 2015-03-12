/**
 * 
 */
package fr.chklang.dontforget;

import play.GlobalSettings;
import play.libs.F.Promise;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;
import fr.chklang.dontforget.exceptions.WebException;

/**
 * @author Chklang
 *
 */
public class ApplicationGlobal extends GlobalSettings {
	
	@Override
	public Promise<Result> onError(RequestHeader pRequest, Throwable pError) {
		if (pError != null && pError.getCause() instanceof WebException) {
			return Promise.<Result> pure(((WebException)pError.getCause()).getResult());
		}
		return Promise.<Result> pure(Results.internalServerError());
	}
}
