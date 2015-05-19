/**
 * 
 */
package fr.chklang.dontforget.android.rest;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import fr.chklang.dontforget.android.AbstractDontForgetException;
import fr.chklang.dontforget.android.ServerConfiguration;

/**
 * @author Chklang
 *
 */
public abstract class AbstractRest {
	public static interface CallbackOnException {
		void call(Exception pException);
	}

	public static class Result<T> {
		private final Future<T> future;
		private CallbackOnException onException = null;

		public Result(Future<T> pFuture) {
			future = pFuture;
		}

		public T get() {
			try {
				return future.get();
			} catch (Exception e) {
				if (onException != null) {
					onException.call(e);
				} else {
					throw new AbstractDontForgetException(e);
				}
			}
			return null;
		}

		public void setOnException(CallbackOnException pOnException) {
			onException = pOnException;
		}
	}

	protected static <T> Result<T> request(Callable<T> pCallable) {
		ExecutorService lExecutorService = Executors.newSingleThreadExecutor();
		return new Result<T>(lExecutorService.submit(pCallable));
	}

	protected static HttpResponse get(final ServerConfiguration pConfiguration, final String pUrl) {
		try {
			HttpGet lHttpGet = new HttpGet(pConfiguration.composeUrl() + pUrl);
			return generic(pConfiguration, lHttpGet);
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	protected static HttpResponse delete(final ServerConfiguration pConfiguration, final String pUrl) {
		try {
			HttpDelete lHttpDelete = new HttpDelete(pConfiguration.composeUrl() + pUrl);
			return generic(pConfiguration, lHttpDelete);
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	protected static HttpResponse post(final ServerConfiguration pConfiguration, final String pUrl, final JSONObject pData) {
		try {
			HttpPost lHttpPost = new HttpPost(pConfiguration.composeUrl() + pUrl);
			if (pData != null) {
				String lContentString = pData.toString();
				StringEntity lEntity = new StringEntity(lContentString);
				lEntity.setContentType("application/json;charset=UTF-8");
				lEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
				lHttpPost.setEntity(lEntity);
			}
			return generic(pConfiguration, lHttpPost);
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	protected static HttpResponse put(final ServerConfiguration pConfiguration, final String pUrl, JSONObject pData) {
		try {
			HttpPut lHttpPut = new HttpPut(pConfiguration.composeUrl() + pUrl);
			if (pData != null) {
				String lContentString = pData.toString();
				StringEntity lEntity = new StringEntity(lContentString);
				lEntity.setContentType("application/json;charset=UTF-8");
				lEntity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));
				lHttpPut.setEntity(lEntity);
			}
			return generic(pConfiguration, lHttpPut);
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	private static HttpResponse generic(final ServerConfiguration pConfiguration, final HttpUriRequest pRequest) {
		try {
			HttpClient lHttpClient = new DefaultHttpClient();
			for (Entry<String, String> lCookie : pConfiguration.getCookies().entrySet()) {
				pRequest.setHeader("Cookie", lCookie.getValue());
			}
			HttpResponse lHttpResponse = lHttpClient.execute(pRequest);

			Header[] lCookies = lHttpResponse.getHeaders("Set-Cookie");
			Map<String, String> lCookiesValues = new HashMap<String, String>();
			for (Header lCookie : lCookies) {
				Cookie lCookieObject = parseRawCookie(lCookie.getValue());
				lCookiesValues.put(lCookieObject.getName(), lCookie.getValue());
			}
			pConfiguration.setCookies(lCookiesValues);
			return lHttpResponse;
		} catch (Exception e) {
			throw new AbstractDontForgetException(e);
		}
	}

	protected static BasicClientCookie parseRawCookie(final String rawCookie) throws Exception {
		String[] rawCookieParams = rawCookie.split(";");

		String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
		if (rawCookieNameAndValue.length < 2) {
			throw new Exception("Invalid cookie: missing name and value. " + rawCookie);
		}

		String cookieName = rawCookieNameAndValue[0].trim();
		String cookieValue = rawCookieNameAndValue[1].trim();
		for (int i = 2; i < rawCookieNameAndValue.length; i++) {
			cookieValue += "=" + rawCookieNameAndValue[i];
		}
		BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
		for (int i = 1; i < rawCookieParams.length; i++) {
			String rawCookieParamNameAndValue[] = rawCookieParams[i].trim().split("=");

			String paramName = rawCookieParamNameAndValue[0].trim();

			if (paramName.equalsIgnoreCase("secure")) {
				cookie.setSecure(true);
			} else {
				if (rawCookieParamNameAndValue.length != 2) {
					continue;
					// throw new
					// Exception("Invalid cookie: attribute not a flag or missing value. index="+i+",line="+rawCookieParams[i].trim());
				}

				String paramValue = rawCookieParamNameAndValue[1].trim();

				if (paramName.equalsIgnoreCase("expires")) {
					Date expiryDate = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).parse(paramValue);
					cookie.setExpiryDate(expiryDate);
				} else if (paramName.equalsIgnoreCase("max-age")) {
					long maxAge = Long.parseLong(paramValue);
					Date expiryDate = new Date(System.currentTimeMillis() + maxAge);
					cookie.setExpiryDate(expiryDate);
				} else if (paramName.equalsIgnoreCase("domain")) {
					cookie.setDomain(paramValue);
				} else if (paramName.equalsIgnoreCase("path")) {
					cookie.setPath(paramValue);
				} else if (paramName.equalsIgnoreCase("comment")) {
					cookie.setPath(paramValue);
				} else {
					throw new Exception("Invalid cookie: invalid attribute name.");
				}
			}
		}

		return cookie;
	}
}
