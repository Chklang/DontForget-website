/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.dto.TaskStatus;

/**
 * @author S0075724
 *
 */
public class TaskDAO extends AbstractDAO<Task, Integer> {
	
	public static final String TABLE_NAME = "t_task";
	public static final String COLUMN_IDTASK = "idTask";
	public static final String COLUMN_NAME = "text";
	public static final String COLUMN_LASTUPDATE = "lastUpdate";
	public static final String COLUMN_UUID = "uuid";
	public static final String COLUMN_IDCATEGORY = "category_id";
	public static final String COLUMN_STATUS = "status";

	@Override
	protected Integer getKey(Task pObject) {
		return pObject.getIdTask();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		return Pair.create(COLUMN_IDTASK+"=?", new String[] {pKey.toString()});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDTASK, COLUMN_NAME, COLUMN_LASTUPDATE, COLUMN_UUID, COLUMN_IDCATEGORY, COLUMN_STATUS};
	}

	@Override
	protected void setKey(long pKey, Task pObject) {
		pObject.setIdTask((int)pKey);
	}

	@Override
	protected ContentValues onCreate(Task pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_NAME, pObject.getName());
		lValues.put(COLUMN_LASTUPDATE, pObject.getLastUpdate());
		lValues.put(COLUMN_UUID, pObject.getUuid());
		lValues.put(COLUMN_IDCATEGORY, pObject.getIdCategory());
		lValues.put(COLUMN_STATUS, pObject.getStatus().getIdStatus());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Task pObjectToSave, Task pOriginalObject) {
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
		if (!areEquals(pOriginalObject.getIdCategory(), pObjectToSave.getIdCategory())) {
			lValues.put(COLUMN_IDCATEGORY, pObjectToSave.getIdCategory());
		}
		if (!areEquals(pOriginalObject.getStatus(), pObjectToSave.getStatus())) {
			lValues.put(COLUMN_STATUS, pObjectToSave.getStatus().getIdStatus());
		}
		
		return lValues;
	}

	protected Task cursorToObjectWithoutControl(Cursor pCursor) {
		
		Task lResult = new Task();
		lResult.setIdTask(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDTASK)));
		lResult.setName(pCursor.getString(pCursor.getColumnIndex(COLUMN_NAME)));
		lResult.setLastUpdate(pCursor.getLong(pCursor.getColumnIndex(COLUMN_LASTUPDATE)));
		lResult.setUuid(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUID)));
		lResult.setIdCategory(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDCATEGORY)));
		lResult.setStatus(TaskStatus.getById(pCursor.getInt(pCursor.getColumnIndex(COLUMN_STATUS))));
		
		return lResult;
	}
}
