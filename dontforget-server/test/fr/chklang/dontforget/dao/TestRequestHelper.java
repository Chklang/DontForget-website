/**
 * 
 */
package fr.chklang.dontforget.dao;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;

/**
 * @author Chklang
 *
 */
public class TestRequestHelper {
	
	private static int timeout = 10000000;
	
	private String baseUrl = null;
	
	private String lastCookie = null;
	
	private int lastStatus = -1;
	
	private String lastBody = null;
	
	private TestRequestHelper(String pBaseUrl) {
		baseUrl = pBaseUrl;
	}

	public static TestRequestHelper create(String pBaseUrl) {
		return new TestRequestHelper(pBaseUrl);
	}
	
	public TestRequestHelper get(String pUrl) {
		WSRequestHolder lRequestHolder = WS.url(baseUrl + pUrl);
		if (lastCookie != null) {
			lRequestHolder = lRequestHolder.setHeader("Cookie", lastCookie);
		}
		WSResponse response = lRequestHolder.get().get(timeout);
		extractCookie(response);
		lastStatus = response.getStatus();
		lastBody = response.getBody();
		return this;
	}
	
	public TestRequestHelper post(String pUrl) {
		WSRequestHolder lRequestHolder = WS.url(baseUrl + pUrl);
		if (lastCookie != null) {
			lRequestHolder = lRequestHolder.setHeader("Cookie", lastCookie);
		}
		WSResponse response = lRequestHolder.post("").get(timeout);
		extractCookie(response);
		lastStatus = response.getStatus();
		lastBody = response.getBody();
		return this;
	}
	public TestRequestHelper post(String pUrl, String pText) {
		WSRequestHolder lRequestHolder = WS.url(baseUrl + pUrl);
		if (lastCookie != null) {
			lRequestHolder = lRequestHolder.setHeader("Cookie", lastCookie);
		}
		WSResponse response = lRequestHolder.post(pText).get(timeout);
		extractCookie(response);
		lastStatus = response.getStatus();
		lastBody = response.getBody();
		return this;
	}
	
	public TestRequestHelper post(String pUrl, JsonNode pNode) {
		WSRequestHolder lRequestHolder = WS.url(baseUrl + pUrl);
		if (lastCookie != null) {
			lRequestHolder = lRequestHolder.setHeader("Cookie", lastCookie);
		}
		WSResponse response = lRequestHolder.post(pNode).get(timeout);
		extractCookie(response);
		lastStatus = response.getStatus();
		lastBody = response.getBody();
		return this;
	}

	private void extractCookie(WSResponse response) {
		String lCookie = response.getHeader("Set-Cookie");
		if (lCookie != null) {
			lastCookie = lCookie;
		}
	}
	
	public int getStatus() {
		return lastStatus;
	}
	
	public String getBody() {
		return lastBody;
	}
	
	public JsonNode getBodyAsJson() {
		return Json.parse(lastBody);
	}
}
