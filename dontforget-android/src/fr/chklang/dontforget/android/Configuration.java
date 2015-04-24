/**
 * 
 */
package fr.chklang.dontforget.android;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chklang
 *
 */
public class Configuration {

	private static final Configuration INSTANCE = new Configuration();
	
	private String protocol;
	
	private Map<String, String> cookies = new HashMap<String, String>();
	
	private String url;
	
	private int port;
	
	private Configuration() {
		
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the cookies
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * @param cookies the cookies to set
	 */
	public void setCookies(Map<String, String> cookies) {
		this.cookies.putAll(cookies);
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	public static Configuration set(String pUrl, int pPort) {
		INSTANCE.setProtocol("http");
		INSTANCE.setUrl(pUrl);
		INSTANCE.setPort(pPort);
		return INSTANCE;
	}
	
	public static Configuration get() {
		return INSTANCE;
	}
}
