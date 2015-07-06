/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.Task;
import fr.chklang.dontforget.android.database.DatabaseManager;
import fr.chklang.dontforget.android.helpers.ConfigurationHelper;

/**
 * @author S0075724
 *
 */
public class PlaceDAO extends AbstractDAO<Place, Integer> {
	
	public static final String TABLE_NAME = "t_place";
	public static final String COLUMN_IDPLACE = "idPlace";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LASTUPDATE = "lastUpdate";
	public static final String COLUMN_UUID = "uuid";
	
	@Override
	public void save(Place pObject) {
		super.save(pObject);
		if (pObject.getUuid() == null || pObject.getUuid().isEmpty()) {
			pObject.setUuid(ConfigurationHelper.getDeviceId() + "_" + pObject.getIdPlace());
			super.save(pObject);
		}
	}

	@Override
	protected Integer getKey(Place pObject) {
		return pObject.getIdPlace();
	}

	@Override
	protected Pair<String, String[]> whereKey(Integer pKey) {
		return Pair.create(COLUMN_IDPLACE+"=?", new String[] {pKey.toString()});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_IDPLACE, COLUMN_NAME, COLUMN_LASTUPDATE, COLUMN_UUID};
	}

	@Override
	protected void setKey(long pKey, Place pObject) {
		pObject.setIdPlace((int)pKey);
	}

	@Override
	protected ContentValues onCreate(Place pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_NAME, pObject.getName());
		lValues.put(COLUMN_LASTUPDATE, pObject.getLastUpdate());
		lValues.put(COLUMN_UUID, pObject.getUuid());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(Place pObjectToSave, Place pOriginalObject) {
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

	protected Place cursorToObjectWithoutControl(Cursor pCursor) {
		
		Place lResult = new Place();
		lResult.setIdPlace(pCursor.getInt(pCursor.getColumnIndex(COLUMN_IDPLACE)));
		lResult.setName(pCursor.getString(pCursor.getColumnIndex(COLUMN_NAME)));
		lResult.setLastUpdate(pCursor.getLong(pCursor.getColumnIndex(COLUMN_LASTUPDATE)));
		lResult.setUuid(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUID)));
		
		return lResult;
	}
	
	public Collection<Place> getPlacesOfTask(Task pTask) {
		String lQuery = generateSelectFrom("T");
		lQuery += ", t_task_place TT WHERE TT.idTask=? AND TT.idPlace = T.idPlace";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {Integer.toString(pTask.getIdTask())});
		return toListObjects(lCursor);
	}
	
	public Place getByName(String pName) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_NAME+"=+?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pName});
		return toObject(lCursor);
	}
	public Place getByUuid(String pUuid) {
		return getByCriterias(Pair.create(COLUMN_UUID+"=?", new String[] {pUuid}));
	}
	
	public Collection<Place> findAfterLastUpdate(long pDate) {
		return findByCriterias(Pair.create(COLUMN_LASTUPDATE + ">=?", new String[]{Long.toString(pDate)}));
	}
}
