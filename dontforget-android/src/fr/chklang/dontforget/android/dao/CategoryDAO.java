/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Category;

/**
 * @author S0075724
 *
 */
public class CategoryDAO extends AbstractDAO<Category, Integer> {
	
	public static final String TABLE_NAME = "t_category";
	public static final String COLUMN_IDCATEGORY = "idCategory";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LASTUPDATE = "lastUpdate";
	public static final String COLUMN_UUID = "uuid";

	@Override
	protected Integer getKey(Category pObject) {
		return pObject.getIdCategory();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		return Pair.create(COLUMN_IDCATEGORY+"=?", new String[] {pKey.toString()});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDCATEGORY, COLUMN_NAME, COLUMN_LASTUPDATE, COLUMN_UUID};
	}

	@Override
	protected void setKey(long pKey, Category pObject) {
		pObject.setIdCategory((int)pKey);
	}

	@Override
	protected ContentValues onCreate(Category pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_NAME, pObject.getName());
		lValues.put(COLUMN_LASTUPDATE, pObject.getLastUpdate());
		lValues.put(COLUMN_UUID, pObject.getUuid());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Category pObjectToSave, Category pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (areEquals(pOriginalObject.getName(), pObjectToSave.getName())) {
			lValues.put(COLUMN_NAME, pObjectToSave.getName());
		}
		if (areEquals(pOriginalObject.getLastUpdate(), pObjectToSave.getLastUpdate())) {
			lValues.put(COLUMN_LASTUPDATE, pObjectToSave.getLastUpdate());
		}
		if (areEquals(pOriginalObject.getUuid(), pObjectToSave.getUuid())) {
			lValues.put(COLUMN_UUID, pObjectToSave.getUuid());
		}
		
		return lValues;
	}

	protected Category cursorToObjectWithoutControl(Cursor pCursor) {
		
		Category lResult = new Category();
		lResult.setIdCategory(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDCATEGORY)));
		lResult.setName(pCursor.getString(pCursor.getColumnIndex(COLUMN_NAME)));
		lResult.setLastUpdate(pCursor.getLong(pCursor.getColumnIndex(COLUMN_LASTUPDATE)));
		lResult.setUuid(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUID)));
		
		return lResult;
	}
}
