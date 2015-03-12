package fr.chklang.dontforget.dao;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import play.libs.Json;
import play.test.FakeApplication;
import play.test.Helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.chklang.dontforget.business.User;


public class IntegrationTest {


	private FakeApplication generateApplication() {
		return Helpers.fakeApplication(Helpers.inMemoryDatabase());
	}
    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
    @Test
    public void test() {
		Helpers.running(Helpers.testServer(9876, generateApplication()), () -> {
			TestRequestHelper lTestRequestHelper = TestRequestHelper.create("http://127.0.0.1:9876/rest/");
			
			//Vérife les utilisateurs actuellement en DB
			List<User> lUsers = User.dao.all();
			Assert.assertEquals("Aucun utilisateur ne doit être en base de donnée", 0, lUsers.size());
			
			ObjectNode lUserCreate = Json.newObject();
			lUserCreate.put("pseudo", "Chklang");
			lUserCreate.put("password", "Motdepasse");
			
			lTestRequestHelper.post("users/login", lUserCreate);
			Assert.assertEquals("La connexion doit être refusée", 401, lTestRequestHelper.getStatus());
			
			lTestRequestHelper.post("users/create", lUserCreate);
			Assert.assertEquals("La création de l'utilisateur a échoué", 200, lTestRequestHelper.getStatus());
			
			lTestRequestHelper.post("users/create", lUserCreate);
			Assert.assertEquals("La création de l'utilisateur devrait échouer", 409, lTestRequestHelper.getStatus());
			
			lTestRequestHelper.post("users/login", lUserCreate);
			Assert.assertEquals("L'identification de l'utilisateur a échoué", 200, lTestRequestHelper.getStatus());
			
			lTestRequestHelper.get("tasks");
			Assert.assertEquals("La récupération de toutes les tâches a échoué", 200, lTestRequestHelper.getStatus());
			JsonNode lResponse = lTestRequestHelper.getBodyAsJson();
			Assert.assertTrue("Le résultat doit être une liste", lResponse.isArray());
			Assert.assertEquals("Il ne doit y avoir aucune tâche pour l'instant", 0, lResponse.size());
			
			String lTaskString = "Nouvelle tâche #test @maison";
			lTestRequestHelper.post("tasks", lTaskString);
			Assert.assertEquals("La création de la tâche a échoué", 200, lTestRequestHelper.getStatus());
			lResponse = lTestRequestHelper.getBodyAsJson();
			Assert.assertTrue("Le résultat doit être un objet", lResponse.isObject());
			Assert.assertEquals("La tâche créée ne correspond pas à la tâche récupérée", "Nouvelle tâche", lResponse.get("text").asText());
			Assert.assertEquals("Un tag doit avoit été trouvé", 1, lResponse.get("tags").size());
			Assert.assertEquals("Un lieu doit avoit été trouvé", 1, lResponse.get("places").size());
			lResponse.get("tags").forEach((lEntry) -> {
				Assert.assertEquals("Le tag renvoyé n'est pas bon", "test", lEntry.asText());
			});
			lResponse.get("places").forEach((lEntry) -> {
				Assert.assertEquals("Le lieu renvoyé n'est pas bon", "maison", lEntry.asText());
			});
			
			lTestRequestHelper.get("tags");
			Assert.assertEquals("La récupération des tags a échoué", 200, lTestRequestHelper.getStatus());
			lResponse = lTestRequestHelper.getBodyAsJson();
			Assert.assertTrue("Le résultat doit être un tableau", lResponse.isArray());
			Assert.assertEquals("Un tag doit avoit été trouvé", 1, lResponse.size());
			lResponse.forEach((lEntry) -> {
				Assert.assertEquals("Le tag renvoyé n'est pas bon", "test", lEntry.asText());
			});
			
			lTestRequestHelper.get("places");
			Assert.assertEquals("La récupération des lieux a échoué", 200, lTestRequestHelper.getStatus());
			lResponse = lTestRequestHelper.getBodyAsJson();
			Assert.assertTrue("Le résultat doit être un tableau", lResponse.isArray());
			Assert.assertEquals("Un lieu doit avoit été trouvé", 1, lResponse.size());
			lResponse.forEach((lEntry) -> {
				Assert.assertEquals("Le lieu renvoyé n'est pas bon", "maison", lEntry.asText());
			});
			
			lTestRequestHelper.post("users/disconnect");
			lTestRequestHelper.get("tasks");
			Assert.assertEquals("La récupération de toutes les tâches devrait échouer", 401, lTestRequestHelper.getStatus());
			lTestRequestHelper.get("tags");
			Assert.assertEquals("La récupération de tous les tags devrait échouer", 401, lTestRequestHelper.getStatus());
			lTestRequestHelper.get("places");
			Assert.assertEquals("La récupération de tous les lieux devrait échouer", 401, lTestRequestHelper.getStatus());
		});
    }

}
