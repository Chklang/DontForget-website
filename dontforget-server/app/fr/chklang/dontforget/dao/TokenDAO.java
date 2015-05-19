package fr.chklang.dontforget.dao;

import play.db.ebean.Model.Finder;
import fr.chklang.dontforget.business.Token;
import fr.chklang.dontforget.business.TokenKey;

public class TokenDAO extends Finder<TokenKey, Token> {

	/** SVUID */
	private static final long serialVersionUID = -1645532994969895002L;

	public TokenDAO() {
		super(TokenKey.class, Token.class);
	}
	
}
