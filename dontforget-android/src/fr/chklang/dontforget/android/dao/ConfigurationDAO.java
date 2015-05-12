/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Configuration;

/**
 * @author S0075724
 *
 */
public class ConfigurationDAO extends AbstractDAO<Configuration, String> {
	
	private static final String TABLE_NAME = "t_configuration";
	private static final String COLUMN_KEY = "confkey";
	private static final String COLUMN_VALUE = "value";

	protected Configuration cursorToObjectWithoutControl(Cursor pCursor) {
		
		Configuration lResult = new Configuration();
		lResult.setKey(pCursor.getString(pCursor.getColumnIndex(COLUMN_KEY)));
		lResult.setValue(pCursor.getString(pCursor.getColumnIndex(COLUMN_VALUE)));
		
		return lResult;
	}

	@Override
	protected ContentValues onCreate(Configuration pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_KEY, pObject.getKey());
		lValues.put(COLUMN_VALUE, pObject.getValue());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Configuration pObjectToSave, Configuration pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (!areEquals(pOriginalObject.getValue(), pObjectToSave.getValue())) {
			lValues.put(COLUMN_VALUE, pObjectToSave.getValue());
		}
		
		return lValues;
	}

	@Override
	protected String getKey(Configuration pObject) {
		return pObject.getKey();
	}

	@Override
	protected Pair<String, String[]> whereKey(String pKey) {
		return Pair.create(COLUMN_KEY+"=?", new String[] {pKey});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_KEY, COLUMN_VALUE};
	}

	@Override
	protected void setKey(long pKey, Configuration pObject) {
		//Ignore it
	}
}
