/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;

/**
 * @author S0075724
 *
 */
public class TagDAO extends AbstractDAO<Tag, Integer> {
	
	public static final String TABLE_NAME = "t_tag";
	public static final String COLUMN_IDTAG = "idTag";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LASTUPDATE = "lastUpdate";
	public static final String COLUMN_UUID = "uuid";
	
	@Override
	public void save(Tag pObject) {
		super.save(pObject);
		if (pObject.getUuid() == null || pObject.getUuid().isEmpty()) {
			pObject.setUuid(ConfigurationHelper.getDeviceId() + "_" + pObject.getIdTag());
			super.save(pObject);
		}
	}

	@Override
	protected Integer getKey(Tag pObject) {
		return pObject.getIdTag();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		return Pair.create(COLUMN_IDTAG+"=?", new String[] {pKey.toString()});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDTAG, COLUMN_NAME, COLUMN_LASTUPDATE, COLUMN_UUID};
	}

	@Override
	protected void setKey(long pKey, Tag pObject) {
		pObject.setIdTag((int)pKey);
	}

	@Override
	protected ContentValues onCreate(Tag pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_NAME, pObject.getName());
		lValues.put(COLUMN_LASTUPDATE, pObject.getLastUpdate());
		lValues.put(COLUMN_UUID, pObject.getUuid());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Tag pObjectToSave, Tag pOriginalObject) {
		ContentValues lValues = new ContentValues();
		if (!areEquals(pOriginalObject.getName(), pObjectToSave.getName())) {
			lValues.put(COLUMN_NAME, pObjectToSave.getName());
		}
		if (!areEquals(pOriginalObject.getLastUpdate(), pObjectToSave.getLastUpdate())) {
			lValues.put(COLUMN_LASTUPDATE, pObjectToSave.getLastUpdate());
		}
		if (!areEquals(pOriginalObject.getUuid(), pObjectToSave.getUuid())) {
			lValues.put(COLUMN_UUID, pObjectToSave.getUuid());
		}
		
		return lValues;
	}

	protected Tag cursorToObjectWithoutControl(Cursor pCursor) {
		
		Tag lResult = new Tag();
		lResult.setIdTag(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDTAG)));
		lResult.setName(pCursor.getString(pCursor.getColumnIndex(COLUMN_NAME)));
		lResult.setLastUpdate(pCursor.getLong(pCursor.getColumnIndex(COLUMN_LASTUPDATE)));
		lResult.setUuid(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUID)));
		
		return lResult;
	}
	
	public Collection<Tag> getTagsOfTask(Task pTask) {
		String lQuery = generateSelectFrom("T");
		lQuery += ", t_task_tag TT WHERE TT.idTask=? AND TT.idTag = T.idTag";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {Integer.toString(pTask.getIdTask())});
		return toListObjects(lCursor);
	}
	
	public Tag getByName(String pName) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_NAME+"=+?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pName});
		return toObject(lCursor);
	}
	public Tag getByUuid(String pUuid) {
		return getByCriterias(Pair.create(COLUMN_UUID+"=?", new String[] {pUuid}));
	}
	
	public Collection<Tag> findAfterLastUpdate(long pDate) {
		return findByCriterias(Pair.create(COLUMN_LASTUPDATE + ">?", new String[]{Long.toString(pDate)}));
	}
}
