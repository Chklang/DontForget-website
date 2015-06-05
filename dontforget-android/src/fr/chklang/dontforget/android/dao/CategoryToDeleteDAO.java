/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.CategoryToDelete;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class CategoryToDeleteDAO extends AbstractDAO<CategoryToDelete, String> {

	public static final String TABLE_NAME = "t_category_to_delete";
	public static final String COLUMN_UUIDCATEGORY = "uuidCategory";
	public static final String COLUMN_DATEDELETION = "dateDeletion";

	@Override
	public void save(CategoryToDelete pObject) {
		super.save(pObject);
	}

	@Override
	protected String getKey(CategoryToDelete pObject) {
		return pObject.getUuidCategory();
	}

	@Override
	protected Pair<String, String[]> whereKey(String pKey) {
		return Pair.create(COLUMN_UUIDCATEGORY + "=?", new String[] { pKey });
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] { COLUMN_UUIDCATEGORY, COLUMN_DATEDELETION };
	}

	@Override
	protected void setKey(long pKey, CategoryToDelete pObject) {
		// Nothing to do
	}

	@Override
	protected ContentValues onCreate(CategoryToDelete pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_UUIDCATEGORY, pObject.getUuidCategory());
		lValues.put(COLUMN_DATEDELETION, pObject.getDateDeletion());

		return lValues;
	}

	@Override
	protected ContentValues onUpdate(CategoryToDelete pObjectToSave, CategoryToDelete pOriginalObject) {
		// Nothing to update, only primary keys

		return new ContentValues();
	}

	protected CategoryToDelete cursorToObjectWithoutControl(Cursor pCursor) {

		CategoryToDelete lResult = new CategoryToDelete();
		lResult.setUuidCategory(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUIDCATEGORY)));
		lResult.setDateDeletion(pCursor.getInt(pCursor.getColumnIndex(COLUMN_DATEDELETION)));

		return lResult;
	}

	public Collection<CategoryToDelete> findByCategorys(Category pCategory) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T." + COLUMN_UUIDCATEGORY + "=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] { pCategory.getUuid() });
		return toListObjects(lCursor);
	}

	public Collection<CategoryToDelete> findAfterDate(long pDate) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T." + COLUMN_DATEDELETION + ">=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] { Long.toString(pDate) });
		return toListObjects(lCursor);
	}

	public void deleteBeforeLastToken() {
		DatabaseManager.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_DATEDELETION + " < (SELECT MIN(" + TokenDAO.COLUMN_LASTSYNCRHO + ") FROM " + TokenDAO.TABLE_NAME
				+ ");", null);
	}

}
