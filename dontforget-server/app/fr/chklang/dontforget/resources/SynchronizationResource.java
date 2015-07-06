package fr.chklang.dontforget.resources;

import java.util.Collection;

import play.mvc.Result;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.CategoryToDelete;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.TagToDelete;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskToDelete;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.User;
import fr.chklang.dontforget.dto.SynchronizationDTO;
import fr.chklang.dontforget.helpers.SynchronizationHelper;


public class SynchronizationResource extends AbstractRest {

	public static Result getUpdates(long pLastUpdate) {
		return executeAndVerifyToken(() -> {
			Token lToken = getConnectedToken();
			if (lToken == null) {
				return unauthorized();
			}
			
			final User lConnectedUser = getConnectedUser();

			Collection<Task> lTasks = Task.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Tag> lTags = Tag.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Place> lPlaces = Place.dao.findByLastUpdate(pLastUpdate, lConnectedUser);
			Collection<Category> lCategories = Category.dao.findByLastUpdate(pLastUpdate, lConnectedUser);

			Collection<CategoryToDelete> lCategoriesToDelete = CategoryToDelete.dao.findAfterDate(pLastUpdate);
			Collection<PlaceToDelete> lPlacesToDelete = PlaceToDelete.dao.findAfterDate(pLastUpdate);
			Collection<TagToDelete> lTagsToDelete = TagToDelete.dao.findAfterDate(pLastUpdate);
			Collection<TaskToDelete> lTasksToDelete = TaskToDelete.dao.findAfterDate(pLastUpdate);
			return ok(new SynchronizationDTO(lTasks.stream(), lTags.stream(), lPlaces.stream(), lCategories.stream(), lToken.getUser(), lCategoriesToDelete.stream(),
					lPlacesToDelete.stream(), lTagsToDelete.stream(), lTasksToDelete.stream()));
		});
	}

	public static Result setUpdates() {
		return executeAndVerifyToken(() -> {
			SynchronizationDTO lDTO = new SynchronizationDTO(request().body().asJson());
			final Token lToken = getConnectedToken();
			
			SynchronizationHelper.update(lToken, lDTO);
			
			return ok();
		});
	}

}
