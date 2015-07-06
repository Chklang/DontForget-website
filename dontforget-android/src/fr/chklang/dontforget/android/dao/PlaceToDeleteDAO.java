/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.Place;
import fr.chklang.dontforget.android.business.PlaceToDelete;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public class PlaceToDeleteDAO extends AbstractDAO<PlaceToDelete, String> {
	
	public static final String TABLE_NAME = "t_place_to_delete";
	public static final String COLUMN_UUIDPLACE = "uuidPlace";
	public static final String COLUMN_DATEDELETION = "dateDeletion";
	
	@Override
	public void save(PlaceToDelete pObject) {
		super.save(pObject);
	}

	@Override
	protected String getKey(PlaceToDelete pObject) {
		return pObject.getUuidPlace();
	}

	@Override
	protected Pair<String, String[]> whereKey(String pKey) {
		return Pair.create(COLUMN_UUIDPLACE+"=?", new String[] {pKey});
	}

	@Override
	protected String getTableName() {
		return TABLE_NAME;
	}

	@Override
	protected String[] getColumnNames() {
		return new String[] {COLUMN_UUIDPLACE, COLUMN_DATEDELETION};
	}

	@Override
	protected void setKey(long pKey, PlaceToDelete pObject) {
		//Nothing to do
	}

	@Override
	protected ContentValues onCreate(PlaceToDelete pObject) {
		ContentValues lValues = new ContentValues();
		lValues.put(COLUMN_UUIDPLACE, pObject.getUuidPlace());
		lValues.put(COLUMN_DATEDELETION, pObject.getDateDeletion());
		
		return lValues;
	}

	@Override
	protected ContentValues onUpdate(PlaceToDelete pObjectToSave, PlaceToDelete pOriginalObject) {
		//Nothing to update, only primary keys
		
		return new ContentValues();
	}

	protected PlaceToDelete cursorToObjectWithoutControl(Cursor pCursor) {
		
		PlaceToDelete lResult = new PlaceToDelete();
		lResult.setUuidPlace(pCursor.getString(pCursor.getColumnIndex(COLUMN_UUIDPLACE)));
		lResult.setDateDeletion(pCursor.getInt(pCursor.getColumnIndex(COLUMN_DATEDELETION)));
		
		return lResult;
	}
	
	public Collection<PlaceToDelete> findByPlaces(Place pPlace) {
		String lQuery = generateSelectFrom("T");
		lQuery += " WHERE T."+COLUMN_UUIDPLACE+"=?";
		Cursor lCursor = DatabaseManager.rawQuery(lQuery, new String[] {pPlace.getUuid()});
		return toListObjects(lCursor);
	}
	
	public Collection<PlaceToDelete> findAfterDate(long pDate) {
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
