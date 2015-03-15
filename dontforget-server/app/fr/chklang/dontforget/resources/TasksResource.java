/**
 * 
 */
package fr.chklang.dontforget.resources;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class TasksResource extends AbstractRest {

	public static Result create() {
		return executeAndVerifyConnect(() -> {
			String lText = request().body().asText();
			Set<Tag> lTags = new HashSet<>();
			Set<Place> lPlaces = new HashSet<>();
			
			Pattern lPatternTags = Pattern.compile(" #([^ ]+)");
			Matcher lMatcher = lPatternTags.matcher(lText);
			while (lMatcher.find()) {
				String lTagString = lMatcher.group(1);
				Tag lTag = Tag.dao.findByTagAndUser(getConnectedUser(), lTagString);
				if (lTag == null) {
					lTag = new Tag();
					lTag.setTag(lTagString);
					lTag.setUser(getConnectedUser());
					lTag.save();
				}
				lTags.add(lTag);
				lText = lText.replace(" #" + lTagString, "");
			}
			
			Pattern lPatternPlaces = Pattern.compile(" @([^ ]+)");
			lMatcher = lPatternPlaces.matcher(lText);
			while (lMatcher.find()) {
				String lPlaceString = lMatcher.group(1);
				Place lPlace = Place.dao.findByPlaceAndUser(getConnectedUser(), lPlaceString);
				if (lPlace == null) {
					lPlace = new Place();
					lPlace.setPlace(lPlaceString);
					lPlace.setUser(getConnectedUser());
					lPlace.save();
				}
				lPlaces.add(lPlace);
				lText = lText.replace(" @" + lPlaceString, "");
			}
			
			lText = lText.trim();
			
			Task lTask = new Task();
			lTask.setPlaces(lPlaces);
			lTask.setTags(lTags);
			lTask.setText(lText);
			lTask.setUser(getConnectedUser());
			lTask.save();
			return ok(new TaskDTO(lTask));
		});
	}

	public static Result findAll() {
		return executeAndVerifyConnect(() -> {
			List<Task> lTasks = Task.dao.findByUser(getConnectedUser());
			
			ObjectNode lFactory = Json.newObject();
			ArrayNode lResults = lFactory.arrayNode();
			for(Task lTask : lTasks) {
				lResults.add(new TaskDTO(lTask));
			}
			return ok(lResults);
		});
	}
	
	public static Result get(final int pIdTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.byId(pIdTask);
			return ok(new TaskDTO(lTask));
		});
	}
	
	public static Result delete(int pIdTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.byId(pIdTask);
			if (lTask == null) {
				return notFound();
			}
			if (!lTask.getUser().equals(getConnectedUser())) {
				return unauthorized();
			}
			lTask.delete();
			return ok();
		});
	}
	
	public static Result update(int pIdTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.byId(pIdTask);
			if (lTask == null) {
				return notFound();
			}
			if (!lTask.getUser().equals(getConnectedUser())) {
				return unauthorized();
			}

			String lText = request().body().asText();
			Set<Tag> lTags = new HashSet<>();
			Set<Place> lPlaces = new HashSet<>();
			
			Pattern lPatternTags = Pattern.compile(" #([^ ]+)");
			Matcher lMatcher = lPatternTags.matcher(lText);
			while (lMatcher.find()) {
				String lTagString = lMatcher.group(1);
				Tag lTag = Tag.dao.findByTagAndUser(getConnectedUser(), lTagString);
				if (lTag == null) {
					lTag = new Tag();
					lTag.setTag(lTagString);
					lTag.setUser(getConnectedUser());
					lTag.save();
				}
				lTags.add(lTag);
				lText = lText.replace(" #" + lTagString, "");
			}
			
			Pattern lPatternPlaces = Pattern.compile(" @([^ ]+)");
			lMatcher = lPatternPlaces.matcher(lText);
			while (lMatcher.find()) {
				String lPlaceString = lMatcher.group(1);
				Place lPlace = Place.dao.findByPlaceAndUser(getConnectedUser(), lPlaceString);
				if (lPlace == null) {
					lPlace = new Place();
					lPlace.setPlace(lPlaceString);
					lPlace.setUser(getConnectedUser());
					lPlace.save();
				}
				lPlaces.add(lPlace);
				lText = lText.replace(" @" + lPlaceString, "");
			}
			
			lText = lText.trim();
			
			lTask.setPlaces(lPlaces);
			lTask.setTags(lTags);
			lTask.setText(lText);
			lTask.save();
			return ok(new TaskDTO(lTask));
		});
	}
}
