/**
 * 
 */
package fr.chklang.dontforget.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import play.test.FakeApplication;
import play.test.Helpers;

import com.avaje.ebean.Ebean;

import fr.chklang.dontforget.business.PlaceToDelete;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.TokenKey;
import fr.chklang.dontforget.business.User;

/**
 * @author Chklang
 *
 */
public class CleanOldObjectsByTokenTest {

	private FakeApplication generateApplication() {
		return Helpers.fakeApplication(Helpers.inMemoryDatabase());
	}

	@Test
	public void test() {
		Helpers.running(Helpers.testServer(9876, generateApplication()), () -> {
			Ebean.execute(() -> {
				User lUserOk = new User();
				lUserOk.setCodelang("fr");
				lUserOk.setDateInscription(10);
				lUserOk.setMail("ici@là.fr");
				lUserOk.setPassword("password");
				lUserOk.setPseudo("UserTest");
				lUserOk.save();

				User lUserNok = new User();
				lUserNok.setCodelang("fr");
				lUserNok.setDateInscription(10);
				lUserNok.setMail("ici@là.fr");
				lUserNok.setPassword("password");
				lUserNok.setPseudo("UserTestBad");
				lUserNok.save();

				TokenKey lTokenKey = new TokenKey();
				lTokenKey.setIdUser(lUserOk.getIdUser());
				lTokenKey.setToken("THE Token");
				Token lToken = new Token();
				lToken.setKey(lTokenKey);
				lToken.setDeviceId("DeviceTest");
				lToken.setLastUpdate(20);
				lToken.save();

				TokenKey lTokenKeyBad = new TokenKey();
				lTokenKeyBad.setIdUser(lUserNok.getIdUser());
				lTokenKeyBad.setToken("THE Token2");
				Token lTokenBad = new Token();
				lTokenBad.setKey(lTokenKeyBad);
				lTokenBad.setDeviceId("DeviceTest");
				lTokenBad.setLastUpdate(12);
				lTokenBad.save();

				PlaceToDelete lPlaceToDeleteOk = new PlaceToDelete();
				lPlaceToDeleteOk.setDateDeletion(15);
				lPlaceToDeleteOk.setUuidPlace("azertyuiop");
				lPlaceToDeleteOk.setIdUser(lUserOk.getIdUser());
				lPlaceToDeleteOk.save();

				PlaceToDelete lPlaceToDeleteNok;
				lPlaceToDeleteNok = new PlaceToDelete();
				lPlaceToDeleteNok.setDateDeletion(25);
				lPlaceToDeleteNok.setUuidPlace("qsdfjklm");
				lPlaceToDeleteNok.setIdUser(lUserNok.getIdUser());
				lPlaceToDeleteNok.save();

				PlaceToDelete.dao.deleteOldObjects(lUserOk);

				List<PlaceToDelete> lPlaces = PlaceToDelete.dao.all();
				Assert.assertEquals("We must have only one entry", 1, lPlaces.size());
				for (PlaceToDelete lPlace : lPlaces) {
					Assert.assertEquals("It's not the good PlaceToDelete", "qsdfjklm", lPlace.getUuidPlace());
				}
			});
		});
	}
}
