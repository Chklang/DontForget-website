/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import play.libs.Json;
import play.mvc.Result;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.dto.TaskDTO;

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
					lResults.add(lTag.getTag());
				}
				return ok(lResults);
			}
		});
	}
	
	public static Result delete(String pName) {
		return executeAndVerifyConnect(() -> {
			Tag lTag = Tag.dao.findByTagAndUser(getConnectedUser(), pName);
			if (lTag == null) {
				return notFound();
			}
			if (!lTag.getUser().equals(getConnectedUser())) {
				return unauthorized();
			}
			lTag.delete();
			return ok();
		});
	}
}
