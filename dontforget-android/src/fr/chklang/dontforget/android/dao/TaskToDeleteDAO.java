/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.business.TaskToDelete;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class TaskToDeleteDAO extends AbstractDAO<TaskToDelete, String> {
	
	public static final String TABLE_NAME = "t_task_to_delete";
	public static final String COLUMN_UUIDTASK = "uuidTask";
	public static final String COLUMN_DATEDELETION = "dateDeletion";
	
	@Override
	public void save(TaskToDelete pObject) {
		super.save(pObject);
	}

	@Override
	protected String getKey(TaskToDelete pObject) {
		return pObject.getUuidTask();
	}

	@Override
	protected Pair<String, String[]> whereKey(String pKey) {
		return Pair.create(COLUMN_UUIDTASK+"=?", new String[] {pKey});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_UUIDTASK, COLUMN_DATEDELETION};
	}

	@Override
	protected void setKey(long pKey, TaskToDelete pObject) {
		//Nothing to do
	}

	@Override
	protected ContentValues onCreate(TaskToDelete pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_UUIDTASK, pObject.getUuidTask());
		lValues.put(COLUMN_DATEDELETION, pObject.getDateDeletion());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(TaskToDelete pObjectToSave, TaskToDelete pOriginalObject) {
		//Nothing to update, only primary keys
		
		return new ContentValues();
	}

	protected TaskToDelete cursorToObjectWithoutControl(Cursor pCursor) {
		
		TaskToDelete lResult = new TaskToDelete();
		lResult.setUuidTask(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUIDTASK)));
		lResult.setDateDeletion(pCursor.getInt(pCursor.getColumnIndex(COLUMN_DATEDELETION)));
		
		return lResult;
	}
	
	public Collection<TaskToDelete> findByTasks(Task pTask) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_UUIDTASK+"=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pTask.getUuid()});
		return toListObjects(lCursor);
	}
	
	public Collection<TaskToDelete> findAfterDate(long pDate) {
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
