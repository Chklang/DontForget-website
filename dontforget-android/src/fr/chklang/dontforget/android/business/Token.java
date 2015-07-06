/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.dao.TokenDAO;
import fr.chklang.dontforget.android.dto.SynchronizationDTO;

/**
 * @author S0075724
 *
 */
public class Token extends AbstractBusinessObject {

	private int idToken;
	private String pseudo;
	private String protocol;
	private String host;
	private int port;
	private String context;
	private String token;
	private long lastSynchro;
	
	public static final TokenDAO dao = new TokenDAO();

	/**
	 * @return the idToken
	 */
	public int getIdToken() {
		return idToken;
	}

	/**
	 * @param idToken the idToken to set
	 */
	public void setIdToken(int idToken) {
		this.idToken = idToken;
	}

	/**
	 * @return the pseudo
	 */
	public String getPseudo() {
		return pseudo;
	}

	/**
	 * @param pseudo the pseudo to set
	 */
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
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
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
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

	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the lastSynchro
	 */
	public long getLastSynchro() {
		return lastSynchro;
	}

	/**
	 * @param lastSynchro the lastSynchro to set
	 */
	public void setLastSynchro(long lastSynchro) {
		this.lastSynchro = lastSynchro;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + idToken;
		result = prime * result + (int) (lastSynchro ^ (lastSynchro >>> 32));
		result = prime * result + port;
		result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
		result = prime * result + ((pseudo == null) ? 0 : pseudo.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (idToken != other.idToken)
			return false;
		if (lastSynchro != other.lastSynchro)
			return false;
		if (port != other.port)
			return false;
		if (protocol == null) {
			if (other.protocol != null)
				return false;
		} else if (!protocol.equals(other.protocol))
			return false;
		if (pseudo == null) {
			if (other.pseudo != null)
				return false;
		} else if (!pseudo.equals(other.pseudo))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Token [idToken=" + idToken + ", pseudo=" + pseudo + ", protocol=" + protocol + ", host=" + host + ", port=" + port + ", context=" + context + ", token=" + token
				+ ", lastSynchro=" + lastSynchro + "]";
	}
	
	public ServerConfiguration toServerConfiguration() {
		String lProtocol = getProtocol();
		String lHost = getHost();
		int lPort = getPort();
		String lContext = getContext();
		return new ServerConfiguration(lProtocol, lHost, lPort, lContext);
	}
}
