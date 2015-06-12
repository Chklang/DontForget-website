/**
 * 
 */
package fr.chklang.dontforget.dao;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;
import fr.chklang.dontforget.business.Category;
import fr.chklang.dontforget.business.Place;
import fr.chklang.dontforget.business.Tag;
import fr.chklang.dontforget.business.Task;
import fr.chklang.dontforget.business.TaskStatus;
import fr.chklang.dontforget.business.User;

/**
 * @author Chklang
 *
 */
public class TaskDAOTest {

	private FakeApplication generateApplication() {
		return Helpers.fakeApplication(Helpers.inMemoryDatabase());
	}

	@Test
	public void findByTagAndUser() {
		Helpers.running(Helpers.testServer(9876, generateApplication()), () -> {
			User lUserOk = new User();
			lUserOk.setCodelang("fr");
			lUserOk.setDateInscription(10);
			lUserOk.setMail("ici@là.fr");
			lUserOk.setPassword("password");
			lUserOk.setPseudo("UserTest");
			lUserOk.save();

			Category lCategory = new Category();
			lCategory.setLastUpdate(10);
			lCategory.setName("Category1");
			lCategory.setUser(lUserOk);
			lCategory.setUuid("UUID-CATEGORY1");
			lCategory.save();

			Tag lTagOK = new Tag();
			lTagOK.setLastUpdate(10);
			lTagOK.setName("Tag1");
			lTagOK.setUser(lUserOk);
			lTagOK.setUuid("UUID-TAG1");
			lTagOK.save();

			Tag lTagNOK = new Tag();
			lTagNOK.setLastUpdate(10);
			lTagNOK.setName("Tag2");
			lTagNOK.setUser(lUserOk);
			lTagNOK.setUuid("UUID-TAG2");
			lTagNOK.save();

			Task lTaskOK = new Task();
			lTaskOK.setCategory(lCategory);
			lTaskOK.setLastUpdate(20);
			lTaskOK.setStatus(TaskStatus.OPENED);
			lTaskOK.setText("Task for tests");
			lTaskOK.setUser(lUserOk);
			lTaskOK.setUuid("UUID-TASK1");
			lTaskOK.setPlaces(new HashSet<>());
			lTaskOK.setTags(new HashSet<>(Arrays.asList(new Tag[] { lTagOK })));
			lTaskOK.save();

			Task lTaskNOK = new Task();
			lTaskNOK.setCategory(lCategory);
			lTaskNOK.setLastUpdate(20);
			lTaskNOK.setStatus(TaskStatus.OPENED);
			lTaskNOK.setText("Bad task for tests");
			lTaskNOK.setUser(lUserOk);
			lTaskNOK.setUuid("UUID-TASK2");
			lTaskNOK.setPlaces(new HashSet<>());
			lTaskNOK.setTags(new HashSet<>(Arrays.asList(new Tag[] { lTagNOK })));
			lTaskNOK.save();

			Set<Task> lTasksFound = Task.dao.findByTagAndUser(lTagOK, lUserOk);
			Assert.assertEquals("We must have only one task", 1, lTasksFound.size());
			Task lTaskDB = lTasksFound.iterator().next();
			Assert.assertEquals("Bad task was gotten", lTaskOK.getUuid(), lTaskDB.getUuid());
		});
	}

	@Test
	public void findByPlaceAndUser() {
		Helpers.running(Helpers.testServer(9876, generateApplication()), () -> {
			User lUserOk = new User();
			lUserOk.setCodelang("fr");
			lUserOk.setDateInscription(10);
			lUserOk.setMail("ici@là.fr");
			lUserOk.setPassword("password");
			lUserOk.setPseudo("UserTest");
			lUserOk.save();

			Category lCategory = new Category();
			lCategory.setLastUpdate(10);
			lCategory.setName("Category1");
			lCategory.setUser(lUserOk);
			lCategory.setUuid("UUID-CATEGORY1");
			lCategory.save();

			Place lPlaceOK = new Place();
			lPlaceOK.setLastUpdate(10);
			lPlaceOK.setName("Place1");
			lPlaceOK.setUser(lUserOk);
			lPlaceOK.setUuid("UUID-PLACE1");
			lPlaceOK.save();

			Place lPlaceNOK = new Place();
			lPlaceNOK.setLastUpdate(10);
			lPlaceNOK.setName("Place2");
			lPlaceNOK.setUser(lUserOk);
			lPlaceNOK.setUuid("UUID-PLACE2");
			lPlaceNOK.save();

			Task lTaskOK = new Task();
			lTaskOK.setCategory(lCategory);
			lTaskOK.setLastUpdate(20);
			lTaskOK.setStatus(TaskStatus.OPENED);
			lTaskOK.setText("Task for tests");
			lTaskOK.setUser(lUserOk);
			lTaskOK.setUuid("UUID-TASK1");
			lTaskOK.setTags(new HashSet<>());
			lTaskOK.setPlaces(new HashSet<>(Arrays.asList(new Place[] { lPlaceOK })));
			lTaskOK.save();

			Task lTaskNOK = new Task();
			lTaskNOK.setCategory(lCategory);
			lTaskNOK.setLastUpdate(20);
			lTaskNOK.setStatus(TaskStatus.OPENED);
			lTaskNOK.setText("Bad task for tests");
			lTaskNOK.setUser(lUserOk);
			lTaskNOK.setUuid("UUID-TASK2");
			lTaskNOK.setTags(new HashSet<>());
			lTaskNOK.setPlaces(new HashSet<>(Arrays.asList(new Place[] { lPlaceNOK })));
			lTaskNOK.save();

			Set<Task> lTasksFound = Task.dao.findByPlaceAndUser(lPlaceOK, lUserOk);
			Assert.assertEquals("We must have only one task", 1, lTasksFound.size());
			Task lTaskDB = lTasksFound.iterator().next();
			Assert.assertEquals("Bad task was gotten", lTaskOK.getUuid(), lTaskDB.getUuid());
		});
	}
}
