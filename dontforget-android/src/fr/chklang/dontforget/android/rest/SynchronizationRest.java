/**
 * 
 */
package fr.chklang.dontforget.android.rest;

import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.dto.SynchronizationDTO;

/**
 * @author Chklang
 *
 */
public class SynchronizationRest extends AbstractRest {

	public static Result<SynchronizationDTO> connexion(final ServerConfiguration pConfiguration, final long pLastUpdate) {
		return request(new Callable<SynchronizationDTO>() {
			@Override
			public SynchronizationDTO call() throws Exception {
				HttpResponse lResponse = get(pConfiguration, "/rest/synchronization/" + pLastUpdate);
				if (lResponse.getStatusLine().getStatusCode() == 200) {
					String lResponseText = EntityUtils.toString(lResponse.getEntity());
					JSONObject lResponseJson = new JSONObject(lResponseText);
					SynchronizationDTO lToken = new SynchronizationDTO(lResponseJson);
					return lToken;
				} else {
					return null;
				}
			}
		});
	}
	
	public static Result<Boolean> update(final ServerConfiguration pConfiguration, final SynchronizationDTO pSynchronizationDTO) {
		return request(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				JSONObject lContent = pSynchronizationDTO.toJson();
				HttpResponse lResponse = post(pConfiguration, "/rest/synchronization", lContent);
				if (lResponse.getStatusLine().getStatusCode() == 200) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
}
