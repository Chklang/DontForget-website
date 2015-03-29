/**
 * 
 */
package fr.chklang.dontforget.android.services;

import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import fr.chklang.dontforget.android.WebException;
import fr.chklang.dontforget.android.dto.UserDTO;

/**
 * @author Chklang
 *
 */
public class UsersService extends AbstractService {

	public static Result<Boolean> connexion(final String pLogin, final String pPassword) {
		return request(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				JSONObject lContent = new JSONObject();
				lContent.put("pseudo", pLogin);
				lContent.put("password", pPassword);
				HttpResponse lResponse = post("/rest/users/login", lContent);
				return lResponse.getStatusLine().getStatusCode() == 200;
			}
		});
	}
	
	public static Result<UserDTO> me() {
		return request(new Callable<UserDTO>() {
			@Override
			public UserDTO call() throws Exception {
				HttpResponse lResponse = get("/rest/users/me");
				if (lResponse.getStatusLine().getStatusCode() != 200) {
					throw new WebException(); //TODO
				}
				
				String lResponseText = EntityUtils.toString(lResponse.getEntity());
				JSONObject lResponseJson = new JSONObject(lResponseText);
				return new UserDTO(lResponseJson);
			}
		});
	}
}
