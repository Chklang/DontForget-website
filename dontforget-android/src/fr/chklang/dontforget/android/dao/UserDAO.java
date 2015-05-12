/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.User;

/**
 * @author S0075724
 *
 */
public class UserDAO extends AbstractDAO<User, Integer> {
	
	private static final String TABLE_NAME = "t_user";
	private static final String COLUMN_IDUSER = "idUser";
	private static final String COLUMN_PSEUDO = "pseudo";
	private static final String COLUMN_PROTOCOL = "protocol";
	private static final String COLUMN_HOST = "host";
	private static final String COLUMN_PORT = "port";
	private static final String COLUMN_CONTEXT = "context";
	private static final String COLUMN_TOKEN = "token";

	@Override
	protected Integer getKey(User pObject) {
		return pObject.getIdUser();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		return Pair.create(COLUMN_IDUSER+"=?", new String[] {pKey.toString()});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDUSER, COLUMN_PSEUDO, COLUMN_PROTOCOL, COLUMN_HOST, COLUMN_PORT, COLUMN_CONTEXT, COLUMN_TOKEN};
	}

	@Override
	protected void setKey(long pKey, User pObject) {
		pObject.setIdUser((int)pKey);
	}

	@Override
	protected ContentValues onCreate(User pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_PSEUDO, pObject.getPseudo());
		lValues.put(COLUMN_PROTOCOL, pObject.getProtocol());
		lValues.put(COLUMN_HOST, pObject.getHost());
		lValues.put(COLUMN_PORT, pObject.getPort());
		lValues.put(COLUMN_CONTEXT, pObject.getContext());
		lValues.put(COLUMN_TOKEN, pObject.getToken());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(User pObjectToSave, User pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (areEquals(pOriginalObject.getPseudo(), pObjectToSave.getPseudo())) {
			lValues.put(COLUMN_PSEUDO, pObjectToSave.getPseudo());
		}
		if (areEquals(pOriginalObject.getProtocol(), pObjectToSave.getProtocol())) {
			lValues.put(COLUMN_PROTOCOL, pObjectToSave.getProtocol());
		}
		if (areEquals(pOriginalObject.getHost(), pObjectToSave.getHost())) {
			lValues.put(COLUMN_HOST, pObjectToSave.getHost());
		}
		if (areEquals(pOriginalObject.getPort(), pObjectToSave.getPort())) {
			lValues.put(COLUMN_PORT, pObjectToSave.getPort());
		}
		if (areEquals(pOriginalObject.getContext(), pObjectToSave.getContext())) {
			lValues.put(COLUMN_CONTEXT, pObjectToSave.getContext());
		}
		if (areEquals(pOriginalObject.getToken(), pObjectToSave.getToken())) {
			lValues.put(COLUMN_TOKEN, pObjectToSave.getToken());
		}
		
		return lValues;
	}

	protected User cursorToObjectWithoutControl(Cursor pCursor) {
		
		User lResult = new User();
		lResult.setIdUser(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDUSER)));
		lResult.setPseudo(pCursor.getString(pCursor.getColumnIndex(COLUMN_PSEUDO)));
		lResult.setProtocol(pCursor.getString(pCursor.getColumnIndex(COLUMN_PROTOCOL)));
		lResult.setHost(pCursor.getString(pCursor.getColumnIndex(COLUMN_HOST)));
		lResult.setPort(pCursor.getInt(pCursor.getColumnIndex(COLUMN_PORT)));
		lResult.setContext(pCursor.getString(pCursor.getColumnIndex(COLUMN_CONTEXT)));
		lResult.setToken(pCursor.getString(pCursor.getColumnIndex(COLUMN_TOKEN)));
		
		return lResult;
	}
}
