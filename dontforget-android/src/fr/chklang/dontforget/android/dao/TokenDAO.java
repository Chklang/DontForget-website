/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.business.TokenKey;

/**
 * @author S0075724
 *
 */
public class TokenDAO extends AbstractDAO<Token, TokenKey> {
	
	private static final String TABLE_NAME = "t_token";
	private static final String COLUMN_PSEUDO = "pseudo";
	private static final String COLUMN_PROTOCOL = "protocol";
	private static final String COLUMN_HOST = "host";
	private static final String COLUMN_PORT = "port";
	private static final String COLUMN_CONTEXT = "context";
	private static final String COLUMN_TOKEN = "token";

	@Override
	protected TokenKey getKey(Token pObject) {
		return pObject.getTokenKey();
	}

	@Override
	protected Pair<String, String[]> whereKey(TokenKey pKey) {
		String lWhere = COLUMN_PSEUDO + "=? AND " + 
				COLUMN_PROTOCOL + "=? AND " + 
				COLUMN_HOST + "=? AND " + 
				COLUMN_PORT + "=? AND " + 
				COLUMN_CONTEXT + "=?";
		String[] lWhereArgs = new String[] {
				pKey.getPseudo(),
				pKey.getProtocol(),
				pKey.getHost(),
				Integer.toString(pKey.getPort()),
				pKey.getContext()
		};
		return Pair.create(lWhere, lWhereArgs);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_PSEUDO, COLUMN_PROTOCOL, COLUMN_HOST, COLUMN_PORT, COLUMN_CONTEXT, COLUMN_TOKEN};
	}

	@Override
	protected void setKey(long pKey, Token pObject) {
		//ignore it
	}

	@Override
	protected ContentValues onCreate(Token pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_PSEUDO, pObject.getTokenKey().getPseudo());
		lValues.put(COLUMN_PROTOCOL, pObject.getTokenKey().getProtocol());
		lValues.put(COLUMN_HOST, pObject.getTokenKey().getHost());
		lValues.put(COLUMN_PORT, pObject.getTokenKey().getPort());
		lValues.put(COLUMN_CONTEXT, pObject.getTokenKey().getContext());
		lValues.put(COLUMN_TOKEN, pObject.getToken());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Token pObjectToSave, Token pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (areEquals(pOriginalObject.getToken(), pObjectToSave.getToken())) {
			lValues.put(COLUMN_TOKEN, pObjectToSave.getToken());
		}
		
		return lValues;
	}

	protected Token cursorToObjectWithoutControl(Cursor pCursor) {
		
		Token lResult = new Token();
		TokenKey lTokenKey = new TokenKey();
		lTokenKey.setPseudo(pCursor.getString(pCursor.getColumnIndex(COLUMN_PSEUDO)));
		lTokenKey.setProtocol(pCursor.getString(pCursor.getColumnIndex(COLUMN_PROTOCOL)));
		lTokenKey.setHost(pCursor.getString(pCursor.getColumnIndex(COLUMN_HOST)));
		lTokenKey.setPort(pCursor.getInt(pCursor.getColumnIndex(COLUMN_PORT)));
		lTokenKey.setContext(pCursor.getString(pCursor.getColumnIndex(COLUMN_CONTEXT)));
		
		lResult.setTokenKey(lTokenKey);
		lResult.setToken(pCursor.getString(pCursor.getColumnIndex(COLUMN_TOKEN)));
		
		return lResult;
	}
}
