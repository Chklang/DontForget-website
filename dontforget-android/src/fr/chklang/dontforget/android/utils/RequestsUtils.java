package fr.chklang.dontforget.android.utils;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;
import fr.chklang.dontforget.android.AbstractDontForgetException;

public class RequestsUtils {

	public static void connexion(Context pContext, String pUrl, int pPort, String pLogin, String pPassword) {
		try {
			HttpClient lHttpClient = new DefaultHttpClient();
			HttpPost lHttpPost = new HttpPost("http://" + pUrl + ":" + pPort + "/rest/users/login");
			JSONObject lContent = new JSONObject();
			lContent.put("pseudo", pLogin);
			lContent.put("password", pPassword);
			String lContentString = lContent.toString();
			StringEntity lEntity = new StringEntity(lContentString);
			lEntity.setContentType("application/json;charset=UTF-8");
			lEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
			lHttpPost.setEntity(lEntity);
			HttpResponse lHttpResponse = lHttpClient.execute(lHttpPost);
			
			int lStatus = lHttpResponse.getStatusLine().getStatusCode();
			String lResponseText = EntityUtils.toString(lHttpResponse.getEntity());
			Header[] lCookies = lHttpResponse.getHeaders("Set-Cookie");
			String lResult = "Status : " + lStatus + "\r\n";
			for (Header lCookie : lCookies) {
				lResult += "Cookie : " + lCookie.getValue();
			}
			lResult += lResponseText;
			Toast.makeText(pContext, lResult, Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}
}
