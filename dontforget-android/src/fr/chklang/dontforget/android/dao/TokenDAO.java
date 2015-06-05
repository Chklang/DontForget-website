/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.ServerConfiguration;
import fr.chklang.dontforget.android.business.Token;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class TokenDAO extends AbstractDAO<Token, Integer> {
	
	public static final String TABLE_NAME = "t_token";
	public static final String COLUMN_IDTOKEN = "idToken";
	public static final String COLUMN_PSEUDO = "pseudo";
	public static final String COLUMN_PROTOCOL = "protocol";
	public static final String COLUMN_HOST = "host";
	public static final String COLUMN_PORT = "port";
	public static final String COLUMN_CONTEXT = "context";
	public static final String COLUMN_TOKEN = "token";
	public static final String COLUMN_LASTSYNCRHO = "lastSynchro";

	@Override
	protected Integer getKey(Token pObject) {
		return pObject.getIdToken();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		String lWhere = COLUMN_IDTOKEN + "=?";
		String[] lWhereArgs = new String[] {pKey.toString()};
		return Pair.create(lWhere, lWhereArgs);
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDTOKEN, COLUMN_PSEUDO, COLUMN_PROTOCOL, COLUMN_HOST, COLUMN_PORT, COLUMN_CONTEXT, COLUMN_TOKEN, COLUMN_LASTSYNCRHO};
	}

	@Override
	protected void setKey(long pKey, Token pObject) {
		pObject.setIdToken((int)pKey);
	}

	@Override
	protected ContentValues onCreate(Token pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_IDTOKEN, pObject.getIdToken());
		lValues.put(COLUMN_PSEUDO, pObject.getPseudo());
		lValues.put(COLUMN_PROTOCOL, pObject.getProtocol());
		lValues.put(COLUMN_HOST, pObject.getHost());
		lValues.put(COLUMN_PORT, pObject.getPort());
		lValues.put(COLUMN_CONTEXT, pObject.getContext());
		lValues.put(COLUMN_TOKEN, pObject.getToken());
		lValues.put(COLUMN_LASTSYNCRHO, pObject.getLastSynchro());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Token pObjectToSave, Token pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (!areEquals(pOriginalObject.getPseudo(), pObjectToSave.getPseudo())) {
			lValues.put(COLUMN_PSEUDO, pObjectToSave.getPseudo());
		}
		if (!areEquals(pOriginalObject.getProtocol(), pObjectToSave.getProtocol())) {
			lValues.put(COLUMN_PROTOCOL, pObjectToSave.getProtocol());
		}
		if (!areEquals(pOriginalObject.getHost(), pObjectToSave.getHost())) {
			lValues.put(COLUMN_HOST, pObjectToSave.getHost());
		}
		if (!areEquals(pOriginalObject.getPort(), pObjectToSave.getPort())) {
			lValues.put(COLUMN_PORT, pObjectToSave.getPort());
		}
		if (!areEquals(pOriginalObject.getContext(), pObjectToSave.getContext())) {
			lValues.put(COLUMN_CONTEXT, pObjectToSave.getContext());
		}
		if (!areEquals(pOriginalObject.getToken(), pObjectToSave.getToken())) {
			lValues.put(COLUMN_TOKEN, pObjectToSave.getToken());
		}
		if (!areEquals(pOriginalObject.getLastSynchro(), pObjectToSave.getLastSynchro())) {
			lValues.put(COLUMN_LASTSYNCRHO, pObjectToSave.getLastSynchro());
		}
		
		return lValues;
	}

	protected Token cursorToObjectWithoutControl(Cursor pCursor) {
		
		Token lResult = new Token();
		lResult.setIdToken(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDTOKEN)));
		lResult.setPseudo(pCursor.getString(pCursor.getColumnIndex(COLUMN_PSEUDO)));
		lResult.setProtocol(pCursor.getString(pCursor.getColumnIndex(COLUMN_PROTOCOL)));
		lResult.setHost(pCursor.getString(pCursor.getColumnIndex(COLUMN_HOST)));
		lResult.setPort(pCursor.getInt(pCursor.getColumnIndex(COLUMN_PORT)));
		lResult.setContext(pCursor.getString(pCursor.getColumnIndex(COLUMN_CONTEXT)));
		lResult.setToken(pCursor.getString(pCursor.getColumnIndex(COLUMN_TOKEN)));
		lResult.setLastSynchro(pCursor.getLong(pCursor.getColumnIndex(COLUMN_LASTSYNCRHO)));
		
		return lResult;
	}
	
	public Token findByPseudoProtocolHostPortAndContext(String pPseudo, String pProtocol, String pHost, int pPort, String pContext) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_PSEUDO+"=? AND T."+COLUMN_PROTOCOL+"=? AND T."+COLUMN_HOST+"=? AND T."+COLUMN_PORT+"=? AND T."+COLUMN_CONTEXT+"=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pPseudo, pProtocol, pHost, Integer.toString(pPort), pContext});
		return toObject(lCursor);
	}
	
	public Token findByPseudoProtocolHostPortAndContext(String pPseudo, ServerConfiguration pServerConfiguration) {
		return findByPseudoProtocolHostPortAndContext(pPseudo, pServerConfiguration.getProtocol(), pServerConfiguration.getHost(), pServerConfiguration.getPort(), pServerConfiguration.getContext());
	}
}
