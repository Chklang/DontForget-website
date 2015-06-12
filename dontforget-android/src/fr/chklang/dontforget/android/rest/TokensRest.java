/**
 * 
 */
package fr.chklang.dontforget.android.rest;

import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.dto.TokenDTO;

/**
 * @author Chklang
 *
 */
public class TokensRest extends AbstractRest {

	public static Result<TokenDTO> create(final ServerConfiguration pConfiguration, final String pLogin, final String pPassword, final String pDeviceId) {
		return request(new Callable<TokenDTO>() {
			@Override
			public TokenDTO call() throws Exception {
				JSONObject lContent = new JSONObject();
				lContent.put("pseudo", pLogin);
				lContent.put("password", pPassword);
				lContent.put("deviceId", pDeviceId);
				HttpResponse lResponse = post(pConfiguration, "/rest/tokens/new", lContent);
				if (lResponse.getStatusLine().getStatusCode() == 200) {
					String lResponseText = EntityUtils.toString(lResponse.getEntity());
					JSONObject lResponseJson = new JSONObject(lResponseText);
					TokenDTO lToken = new TokenDTO(lResponseJson, pConfiguration);
					return lToken;
				} else {
					return null;
				}
			}
		});
	}
	
	public static Result<Boolean> connexion(final ServerConfiguration pConfiguration, final Token pToken, final String pDeviceId) {
		return request(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				JSONObject lContent = new JSONObject();
				lContent.put("pseudo", pToken.getPseudo());
				lContent.put("token", pToken.getToken());
				lContent.put("deviceId", pDeviceId);
				HttpResponse lResponse = post(pConfiguration, "/rest/tokens/login", lContent);
				if (lResponse.getStatusLine().getStatusCode() == 200) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
	
	public static void sendUpdateToken(final ServerConfiguration pConfiguration, final long pDate) {
		request(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				post(pConfiguration, "/rest/tokens/update/"+pDate, null);
				return null;
			}
		});
	}
}
