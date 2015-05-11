/**
 * 
 */
package fr.chklang.dontforget.android.dao;

import java.util.ArrayList;
import java.util.Collection;

import android.database.Cursor;
import fr.chklang.dontforget.android.business.AbstractBusinessObject;

/**
 * @author S0075724
 *
 */
public abstract class AbstractDAO<T extends AbstractBusinessObject, Key> {

	public void save(T pObject) {
		if (pObject.isAlreadyIntoDB()) {
			update(pObject);
		} else {
			create(pObject);
		}
	}
	
	public void create(T pObject) {
		onCreate(pObject);
		pObject.setAlreadyIntoDB(true);
	}
	
	public T get(Key pKey) {
		T lResult = onGet(pKey);
		lResult.setAlreadyIntoDB(true);
		return lResult;
	}
	
	public void update(T pObject) {
		onUpdate(pObject);
	}
	
	public void delete(T pObject) {
		onDelete(pObject);
	}
	
	protected T toObject(Cursor pCursor) {
		
		if (pCursor.getCount() == 0) {
			return null;
		}
		
		pCursor.moveToFirst();
		
		T lResult = cursorToObjectWithoutControl(pCursor);
		
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
			lResults.add(toObject(pCursor));
		}
		
		pCursor.close();
		
		return lResults;
	}
	
	protected abstract void onCreate(T pObject);
	protected abstract void onUpdate(T pObject);
	protected abstract T onGet(Key pKey);
	protected abstract void onDelete(T pObject);
	protected abstract T cursorToObjectWithoutControl(Cursor pCursor);
}
