/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.TagToDelete;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class TagToDeleteDAO extends AbstractDAO<TagToDelete, String> {
	
	public static final String TABLE_NAME = "t_tag_to_delete";
	public static final String COLUMN_UUIDTAG = "uuidTag";
	public static final String COLUMN_DATEDELETION = "dateDeletion";
	
	@Override
	public void save(TagToDelete pObject) {
		super.save(pObject);
	}

	@Override
	protected String getKey(TagToDelete pObject) {
		return pObject.getUuidTag();
	}

	@Override
	protected Pair<String, String[]> whereKey(String pKey) {
		return Pair.create(COLUMN_UUIDTAG+"=?", new String[] {pKey});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_UUIDTAG, COLUMN_DATEDELETION};
	}

	@Override
	protected void setKey(long pKey, TagToDelete pObject) {
		//Nothing to do
	}

	@Override
	protected ContentValues onCreate(TagToDelete pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_UUIDTAG, pObject.getUuidTag());
		lValues.put(COLUMN_DATEDELETION, pObject.getDateDeletion());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(TagToDelete pObjectToSave, TagToDelete pOriginalObject) {
		//Nothing to update, only primary keys
		
		return new ContentValues();
	}

	protected TagToDelete cursorToObjectWithoutControl(Cursor pCursor) {
		
		TagToDelete lResult = new TagToDelete();
		lResult.setUuidTag(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUIDTAG)));
		lResult.setDateDeletion(pCursor.getInt(pCursor.getColumnIndex(COLUMN_DATEDELETION)));
		
		return lResult;
	}
	
	public Collection<TagToDelete> findByTags(Tag pTag) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_UUIDTAG+"=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pTag.getUuid()});
		return toListObjects(lCursor);
	}
	
	public Collection<TagToDelete> findAfterDate(long pDate) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_DATEDELETION+">=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {Long.toString(pDate)});
		return toListObjects(lCursor);
	}

	public void deleteBeforeLastToken() {
		DatabaseManager.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_DATEDELETION + " < (SELECT MIN(" + TokenDAO.COLUMN_LASTSYNCRHO + ") FROM " + TokenDAO.TABLE_NAME
				+ ");", null);
	}
	
}
