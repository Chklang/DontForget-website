/**
 * 
 */
package fr.chklang.dontforget.android.business;

import fr.chklang.dontforget.android.ServerConfiguration;

/**
 * @author S0075724
 *
 */
public class Token extends AbstractBusinessObject {

	private TokenKey tokenKey;
	private String token;
	
	public Token() {
		super();
	}
	
	public Token(String pPseudo, ServerConfiguration pServerConfiguration) {
		super();
		tokenKey = new TokenKey(pPseudo, pServerConfiguration);
	}

	/**
	 * @return the tokenKey
	 */
	public TokenKey getTokenKey() {
		return tokenKey;
	}

	/**
	 * @param tokenKey the tokenKey to set
	 */
	public void setTokenKey(TokenKey tokenKey) {
		this.tokenKey = tokenKey;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result + ((tokenKey == null) ? 0 : tokenKey.hashCode());
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
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (tokenKey == null) {
			if (other.tokenKey != null)
				return false;
		} else if (!tokenKey.equals(other.tokenKey))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Token [tokenKey=" + tokenKey + ", token=" + token + "]";
	}
}
