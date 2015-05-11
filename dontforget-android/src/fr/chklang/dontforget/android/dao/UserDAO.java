/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import fr.chklang.dontforget.android.business.User;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class UserDAO extends AbstractDAO<User, Integer> {
	
	public void save(User pUser) {
		
	}

	protected User cursorToObjectWithoutControl(Cursor pCursor) {
		
		User lResult = new User();
		lResult.setIdUser(pCursor.getInt(pCursor.getColumnIndex("idUser")));
		lResult.setPseudo(pCursor.getString(pCursor.getColumnIndex("pseudo")));
		lResult.setProtocol(pCursor.getString(pCursor.getColumnIndex("protocol")));
		lResult.setHost(pCursor.getString(pCursor.getColumnIndex("host")));
		lResult.setPort(pCursor.getInt(pCursor.getColumnIndex("port")));
		lResult.setContext(pCursor.getString(pCursor.getColumnIndex("context")));
		lResult.setToken(pCursor.getString(pCursor.getColumnIndex("token")));
		
		return lResult;
	}

	@Override
	protected void onCreate(User pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put("pseudo", pObject.getPseudo());
		lValues.put("protocol", pObject.getProtocol());
		lValues.put("host", pObject.getHost());
		lValues.put("port", pObject.getPort());
		lValues.put("context", pObject.getContext());
		lValues.put("token", pObject.getToken());
		
		DatabaseManager.getWrittableDatabase().insert("t_user", null, lValues);
	}

	@Override
	protected void onUpdate(User pObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected User onGet(Integer pKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onDelete(User pObject) {
		// TODO Auto-generated method stub
		
	}
}
