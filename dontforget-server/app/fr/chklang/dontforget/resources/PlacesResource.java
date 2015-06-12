/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.List;
import java.util.concurrent.Callable;

import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.dto.PlaceDTO;

/**
 * @author Chklang
 *
 */
public class PlacesResource extends AbstractRest {

	public static Result findAll() {
		return executeAndVerifyConnect(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				List<Place> lPlaces = Place.dao.findByUser(getConnectedUser());
				
				ObjectNode lFactory = Json.newObject();
				ArrayNode lResults = lFactory.arrayNode();
				for(Place lPlace : lPlaces) {
					lResults.add(new PlaceDTO(lPlace));
				}
				return ok(lResults);
			}
		});
	}
	
	public static Result delete(String pUuid) {
		return executeAndVerifyConnect(() -> {
			Place lPlace = Place.dao.getByUuid(pUuid);
			if (lPlace == null) {
				return notFound();
			}
			if (lPlace.getUser().getIdUser() != getConnectedUser().getIdUser()) {
				return unauthorized();
			}
			lPlace.delete();
			return ok();
		});
	}
}
