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

import fr.chklang.dontforget.android.dto.CategoryDTO;

/**
 * @author Chklang
 *
 */
public class CategoriesService extends AbstractService {

	public static Result<Collection<CategoryDTO>> getAll() {
		return request(new Callable<Collection<CategoryDTO>>() {
			@Override
			public Collection<CategoryDTO> call() throws Exception {
			HttpResponse lResponse = get("/rest/categories");
			
			String lContent = EntityUtils.toString(lResponse.getEntity());
			JSONArray lResponseJSON = new JSONArray(lContent);
			Collection<CategoryDTO> lResults = new ArrayList<CategoryDTO>();
			for (int i=0; i < lResponseJSON.length(); i++) {
				JSONObject lEntry = lResponseJSON.getJSONObject(i);
				lResults.add(new CategoryDTO(lEntry));
			}
			return lResults;
			}
		});
	}
}
