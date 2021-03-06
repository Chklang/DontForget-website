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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskStatus;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.TaskDTO;




/**
 * @author Chklang
 *
 */
public class TasksResource extends AbstractRest {

	public static Result create(String pCategoryUuid) {
		return executeAndVerifyConnect(() -> {
			String lText = request().body().asText();
			Set<Tag> lTags = new HashSet<>();
			Set<Place> lPlaces = new HashSet<>();
			Set<Category> lCategories = new HashSet<>();
			
			Pattern lPatternTags = Pattern.compile(" #([^ ]+)");
			Matcher lMatcher = lPatternTags.matcher(" " + lText);
			while (lMatcher.find()) {
				String lTagString = lMatcher.group(1);
				List<Tag> lTagsDB = Tag.dao.findByTagAndUser(getConnectedUser(), lTagString);
				Tag lTag;
				if (lTagsDB.isEmpty()) {
					lTag = new Tag();
					lTag.setName(lTagString);
					lTag.setUser(getConnectedUser());
					lTag.save();
				} else {
					lTag = lTagsDB.get(0);
				}
				lTags.add(lTag);
				lText = lText.replace("#" + lTagString, "");
			}
			
			Pattern lPatternPlaces = Pattern.compile(" @([^ ]+)");
			lMatcher = lPatternPlaces.matcher(" " + lText);
			while (lMatcher.find()) {
				String lPlaceString = lMatcher.group(1);
				List<Place> lPlacesDB = Place.dao.findByPlaceAndUser(getConnectedUser(), lPlaceString);
				Place lPlace;
				if (lPlacesDB.isEmpty()) {
					lPlace = new Place();
					lPlace.setName(lPlaceString);
					lPlace.setUser(getConnectedUser());
					lPlace.save();
				} else {
					lPlace = lPlacesDB.get(0);
				}
				lPlaces.add(lPlace);
				lText = lText.replace("@" + lPlaceString, "");
			}
			
			Category lCategory = Category.dao.getByUuid(pCategoryUuid);
			if (lCategory == null) {
				return notFound();
			}
			lCategories.add(lCategory);
			
			while (lText.contains("  ")) {
				lText = lText.replaceAll("  ",	" ");
			}
			
			lText = lText.trim();
			
			Task lTask = new Task();
			lTask.setPlaces(lPlaces);
			lTask.setTags(lTags);
			lTask.setText(lText);
			lTask.setCategory(lCategory);
			lTask.setUser(getConnectedUser());
			lTask.setStatus(TaskStatus.OPENED);
			lTask.save();
			return ok(new TaskDTO(lTask));
		});
	}

	public static Result findAll() {
		return executeAndVerifyConnect(() -> {
			Set<Task> lTasks = Task.dao.findByUser(getConnectedUser());
			
			ObjectNode lFactory = Json.newObject();
			ArrayNode lResults = lFactory.arrayNode();
			for(Task lTask : lTasks) {
				lResults.add(new TaskDTO(lTask));
			}
			return ok(lResults);
		});
	}
	
	public static Result findAllByCategory(String pCategoryUuid) {
		return executeAndVerifyConnect(() -> {
			User lConnectedUser = getConnectedUser();
			Category lCategory = Category.dao.getByUuid(pCategoryUuid);
			if (lCategory == null) {
				return notFound();
			}
			if (lCategory.getUser().getIdUser() != lConnectedUser.getIdUser()) {
				return unauthorized();
			}
			
			Set<Task> lTasks = Task.dao.findByCategoryAndUser(lCategory, lConnectedUser);
			
			ObjectNode lFactory = Json.newObject();
			ArrayNode lResults = lFactory.arrayNode();
			for(Task lTask : lTasks) {
				lResults.add(new TaskDTO(lTask));
			}
			return ok(lResults);
		});
	}
	
	public static Result get(final String pUuidTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.getByUuid(pUuidTask);
			return ok(new TaskDTO(lTask));
		});
	}
	
	public static Result delete(final String pUuidTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.getByUuid(pUuidTask);
			if (lTask == null) {
				return notFound();
			}
			if (!lTask.getUser().equals(getConnectedUser())) {
				return unauthorized();
			}
			lTask.getPlaces().clear();
			lTask.getTags().clear();
			lTask.save();
			lTask.delete();
			return ok();
		});
	}
	
	public static Result update(final String pUuidTask) {
		return executeAndVerifyConnect(() -> {
			Task lTask = Task.dao.getByUuid(pUuidTask);
			if (lTask == null) {
				return notFound();
			}
			if (!lTask.getUser().equals(getConnectedUser())) {
				return unauthorized();
			}

			JsonNode lContent = request().body().asJson();
			JsonNode lStatus = lContent.get("status");
			if (!lStatus.isNull()) {
				if (TaskStatus.OPENED.name().equals(lStatus.asText())) {
					lTask.setStatus(TaskStatus.OPENED);
				} else if (TaskStatus.FINISHED.name().equals(lStatus.asText())) {
					lTask.setStatus(TaskStatus.FINISHED);
				} else if (TaskStatus.DELETED.name().equals(lStatus.asText())) {
					lTask.setStatus(TaskStatus.DELETED);
				}
			}
			lTask.save();
			return ok(new TaskDTO(lTask));
		});
	}
}
