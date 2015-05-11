/**
 * 
 */
package fr.chklang.dontforget;

import java.util.Collection;

import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;

/**
 * @author S0075724
 *
 */
public class DatabaseVersionManager {
	
	public static void checkDatabase() {
		v4_to_v5();
	}

	private static void v4_to_v5() {
		String lDeviceId = ConstantsHelper.singleton().getDEVICE_ID();
		
		Collection<Tag> lTags = Tag.dao.where().isNull("uuid").findList();
		for (Tag lTag : lTags) {
			lTag.setUuid(lDeviceId + "_" + lTag.getId());
			lTag.save();
		}
		Collection<Category> lCategories = Category.dao.where().isNull("uuid").findList();
		for (Category lCategory : lCategories) {
			lCategory.setUuid(lDeviceId + "_" + lCategory.getId());
			lCategory.save();
		}
		Collection<Place> lPlaces = Place.dao.where().isNull("uuid").findList();
		for (Place lPlace : lPlaces) {
			lPlace.setUuid(lDeviceId + "_" + lPlace.getId());
			lPlace.save();
		}
		Collection<Task> lTasks = Task.dao.where().isNull("uuid").findList();
		for (Task lTask : lTasks) {
			lTask.setUuid(lDeviceId + "_" + lTask.getIdTask());
			lTask.save();
		}
	}
}
