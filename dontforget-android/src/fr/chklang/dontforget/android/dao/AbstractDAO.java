/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Pair;
import fr.chklang.dontforget.android.business.AbstractBusinessObject;
import fr.chklang.dontforget.android.database.DatabaseManager;

/**
 * @author S0075724
 *
 */
public abstract class AbstractDAO<T extends AbstractBusinessObject, Key> {
	
	public T get(Key pKey) {
		return getByCriterias(whereKey(pKey));
	}
	
	public Collection<T> getAll() {
		return findByCriterias(null);
	}

	public void save(T pObject) {
		if (pObject.isAlreadyIntoDB()) {
			update(pObject);
		} else {
			create(pObject);
		}
	}
	
	public void create(T pObject) {
		ContentValues lValues = onCreate(pObject);
		long lIdGenerated = DatabaseManager.insert(getTableName(), null, lValues);
		setKey(lIdGenerated, pObject);
		pObject.setAlreadyIntoDB(true);
	}
	
	public void update(T pObject) {
		ContentValues lValues = onUpdate(pObject, get(getKey(pObject)));
		Pair<String, String[]> lWhere = whereKey(getKey(pObject));
		
		if (lValues.size() != 0) {
			DatabaseManager.update(getTableName(), lValues, lWhere.first, lWhere.second);
		}
	}
	
	public void delete(Key pKey) {
		Pair<String, String[]> lWhere = whereKey(pKey);
		DatabaseManager.delete(getTableName(), lWhere.first, lWhere.second);
	}
	
	protected T getByCriterias(Pair<String, String[]> pCriterias) {
		String lWhere = pCriterias == null?null:pCriterias.first;
		String[] lWhereArgs = pCriterias == null?null:pCriterias.second;
		Cursor lCursor = DatabaseManager.query(getTableName(), getColumnNames(), lWhere, lWhereArgs, null, null, null);
		T lResult = toObject(lCursor);
		if (lResult != null) {
			lResult.setAlreadyIntoDB(true);
		}
		return lResult;
	}
	
	protected Collection<T> findByCriterias(Pair<String, String[]> pCriterias) {
		String lWhere = pCriterias == null?null:pCriterias.first;
		String[] lWhereArgs = pCriterias == null?null:pCriterias.second;
		Cursor lCursor = DatabaseManager.query(getTableName(), getColumnNames(), lWhere, lWhereArgs, null, null, null);
		Collection<T> lResults = toListObjects(lCursor);
		for (T lObject : lResults) {
			lObject.setAlreadyIntoDB(true);
		}
		return lResults;
	}
	
	protected String generateSelectFrom(String pTableAlias) {
		String lQuery = "SELECT ";
		boolean lMustAddSeparator = false;
		for (String lColumnName : getColumnNames()) {
			if (lMustAddSeparator) {
				lQuery += ", ";
			} else {
				lMustAddSeparator = true;
			}
			if (pTableAlias != null && !pTableAlias.isEmpty()) {
				lQuery += pTableAlias + ".";
			}
			lQuery += lColumnName;
		}
		lQuery += " FROM " + getTableName() + " ";
		if (pTableAlias != null && !pTableAlias.isEmpty()) {
			lQuery += pTableAlias + " ";
		}
		return lQuery;
	}
	
	protected T toObject(Cursor pCursor) {
		
		if (pCursor.getCount() == 0) {
			return null;
		}
		
		pCursor.moveToFirst();
		
		T lResult = cursorToObjectWithoutControl(pCursor);
		lResult.setAlreadyIntoDB(true);
		
		pCursor.close();
		
		return lResult;
	}
	
	protected Collection<T> toListObjects(Cursor pCursor) {
		
		Collection<T> lResults = new ArrayList<T>();
		
		if (pCursor.getCount() == 0) {
			return lResults;
		}
		
		pCursor.moveToFirst();
		while (!pCursor.isAfterLast()) {
			lResults.add(cursorToObjectWithoutControl(pCursor));
			pCursor.moveToNext();
		}
		
		pCursor.close();
		
		return lResults;
	}
	
	protected abstract ContentValues onCreate(T pObject);
	protected abstract ContentValues onUpdate(T pObjectToSave, T pOriginalObject);
	protected abstract T cursorToObjectWithoutControl(Cursor pCursor);
	protected abstract Key getKey(T pObject);
	
	protected boolean areEquals(Object pArg1, Object pArg2) {
		if (pArg1 == null && pArg2 == null) {
			return true;
		}
		if (pArg1 == null && pArg2 != null) {
			return false;
		}
		if (pArg1 != null && pArg2 == null) {
			return false;
		}
		return pArg1.equals(pArg2);
	}
	
	protected abstract Pair<String, String[]> whereKey(Key pKey);
	protected abstract String getTableName();
	protected abstract String[] getColumnNames();
	protected abstract void setKey(long pKey, T pObject);
}
