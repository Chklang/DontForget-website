/**
 * 
 */
package fr.chklang.dontforget.android.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import fr.chklang.dontforget.android.WebException;
import fr.chklang.dontforget.android.dto.TaskDTO;

/**
 * @author Chklang
 *
 */
public class TasksService extends AbstractService {

	public static Result<Collection<TaskDTO>> getAll() {
		return request(new Callable<Collection<TaskDTO>>() {

			@Override
			public Collection<TaskDTO> call() throws Exception {
				HttpResponse lResponse = get("/rest/tasks");
				if (lResponse.getStatusLine().getStatusCode() != 200) {
					throw new WebException(); //TODO
				}
				
				String lResponseText = EntityUtils.toString(lResponse.getEntity());
				JSONArray lResponseJson = new JSONArray(lResponseText);
				Collection<TaskDTO> lResults = new ArrayList<TaskDTO>();
				for (int i=0; i<lResponseJson.length(); i++) {
					JSONObject lEntry = lResponseJson.getJSONObject(i);
					lResults.add(new TaskDTO(lEntry));
				}
				return lResults;
			}
		});
	}
}
