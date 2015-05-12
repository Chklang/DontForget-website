/**
 * 
 */
package fr.chklang.dontforget.android;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Chklang
 *
 */
public class ServerConfiguration {
	
	private static final Collection<ServerConfiguration> configurations = new HashSet<ServerConfiguration>(); 

	private final String protocol;
	
	private final  String host;
	
	private final  int port;
	
	private final  String context;
	
	private final Map<String, String> cookies = new HashMap<String, String>();
	
	public ServerConfiguration(String pProtocol, String pHost, int pPort, String pContext) {
		protocol = pProtocol;
		host = pHost;
		port = pPort;
		context = pContext;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
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
	 * Reset cookies
	 */
	public void resetCookies() {
		cookies.clear();
	}
	
	public String composeUrl() {
		String lCompleteUrl = protocol + "://" + host + ":" + port;
		if (context != null && !context.isEmpty()) {
			lCompleteUrl += context;
		}
		return lCompleteUrl;
	}
	
	public static ServerConfiguration newConfiguration(String pProtocol, String pHost, int pPort, String pContext) {
		ServerConfiguration lConfiguration = new ServerConfiguration(pProtocol, pHost, pPort, pContext);
		synchronized (configurations) {
			configurations.add(lConfiguration);
		}
		return lConfiguration;
	}
	
	public static Collection<ServerConfiguration> getConfigurations() {
		return Collections.unmodifiableCollection(configurations);
	}
	
}
