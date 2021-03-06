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

import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.dto.TagDTO;

/**
 * @author Chklang
 *
 */
public class TagsResource extends AbstractRest {

	public static Result findAll() {
		return executeAndVerifyConnect(new Callable<Result>() {
			@Override
			public Result call() throws Exception {
				List<Tag> lTags = Tag.dao.findByUser(getConnectedUser());
				
				ObjectNode lFactory = Json.newObject();
				ArrayNode lResults = lFactory.arrayNode();
				for(Tag lTag : lTags) {
					lResults.add(new TagDTO(lTag));
				}
				return ok(lResults);
			}
		});
	}
	
	public static Result delete(String pUuid) {
		return executeAndVerifyConnect(() -> {
			Tag lTag = Tag.dao.getByUuid(pUuid);
			if (lTag == null) {
				return notFound();
			}
			if (lTag.getUser().getIdUser() != getConnectedUser().getIdUser()) {
				return unauthorized();
			}
			lTag.delete();
			return ok();
		});
	}
}
