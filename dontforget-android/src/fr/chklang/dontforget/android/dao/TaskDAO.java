/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Category;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Tag;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.dto.TaskStatus;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;

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
	public void save(Task pObject) {
		super.save(pObject);
		if (pObject.getUuid() == null || pObject.getUuid().isEmpty()) {
			pObject.setUuid(ConfigurationHelper.getDeviceId() + "_" + pObject.getIdTask());
			super.save(pObject);
		}
	}

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
	
	public Collection<Task> findByCategory(Category pCategory) {
		return findByCriterias(Pair.create(COLUMN_IDCATEGORY + "=?", new String[] {Integer.toString(pCategory.getIdCategory())}));
	}
	
	public Collection<Task> findByTag(Tag pTag) {
		String lQuery = generateSelectFrom("T");
		lQuery += ", t_task_tag TT WHERE TT.idTask = T.idTask AND TT.idTag = ?";
		Cursor lCursor = DatabaseManager.getReadableDatabase().rawQuery(lQuery, new String[] {Integer.toString(pTag.getIdTag())});
		return toListObjects(lCursor);
	}
	
	public Collection<Task> findByPlace(Place pPlace) {
		String lQuery = generateSelectFrom("T");
		lQuery += ", t_task_place TP WHERE TP.idTask = T.idTask AND TP.idPlace = ?";
		Cursor lCursor = DatabaseManager.getReadableDatabase().rawQuery(lQuery, new String[] {Integer.toString(pPlace.getIdPlace())});
		return toListObjects(lCursor);
	}
	
	public void addTagToTask(Task pTask, Tag pTag) {
		ContentValues lValues = new ContentValues();
		lValues.put("idTask", pTask.getIdTask());
		lValues.put("idTag", pTag.getIdTag());
		DatabaseManager.getWrittableDatabase().insert("t_task_tag", null, lValues);
	}
	
	public void removeTagFromTask(Task pTask, Tag pTag) {
		DatabaseManager.getWrittableDatabase().delete("t_task_tag", "idTask=? AND idTag=?", new String[]{Integer.toString(pTask.getIdTask()), Integer.toString(pTag.getIdTag())});
	}
	
	public void addPlaceToTask(Task pTask, Place pPlace) {
		ContentValues lValues = new ContentValues();
		lValues.put("idTask", pTask.getIdTask());
		lValues.put("idPlace", pPlace.getIdPlace());
		DatabaseManager.getWrittableDatabase().insert("t_task_place", null, lValues);
	}
	
	public void removePlaceFromTask(Task pTask, Place pPlace) {
		DatabaseManager.getWrittableDatabase().delete("t_task_place", "idTask=? AND idPlace=?", new String[]{Integer.toString(pTask.getIdTask()), Integer.toString(pPlace.getIdPlace())});
	}
}
