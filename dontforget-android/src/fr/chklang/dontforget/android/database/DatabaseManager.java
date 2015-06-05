/**
 * 
 */
package fr.chklang.dontforget.android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author S0075724
 *
 */
public class DatabaseManager {

	private static int VERSION = 3;

	private static ThreadLocal<DatabaseConnection> connection = new InheritableThreadLocal<DatabaseConnection>();
	
	public static interface Transaction {
		void execute();
	}
	
	public static void transaction(Context pContext, Transaction pTransaction) {
		try {
			open(pContext);
			pTransaction.execute();
		} finally {
			close();
		}
	}
	
	public static void open(Context pContext) {
		connection.set(new DatabaseConnection(pContext, "DONT_FORGET", null, VERSION));
	}

	private static DatabaseConnection getConnection() {
		return connection.get();
	}
	
	private static SQLiteDatabase getWrittableDatabase() {
		return getConnection().getWritableDatabase();
	}
	
	private static SQLiteDatabase getReadableDatabase() {
		return getConnection().getReadableDatabase();
	}
	
	public static void close() {
		connection.get().close();
		connection.remove();
	}
	
	public static long insert(String table, String nullColumnHack, ContentValues values) {
		SQLiteDatabase lWrittableDatabase = getWrittableDatabase();
		return lWrittableDatabase.insert(table, nullColumnHack, values);
	}
	
	public static int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase lWrittableDatabase = getWrittableDatabase();
		return lWrittableDatabase.update(table, values, whereClause, whereArgs);
	}
	
	public static int delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase lWrittableDatabase = getWrittableDatabase();
		return lWrittableDatabase.delete(table, whereClause, whereArgs);
	}
	
	public static Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		SQLiteDatabase lReadableDatabase = getReadableDatabase();
		return lReadableDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public static Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase lReadableDatabase = getReadableDatabase();
		return lReadableDatabase.rawQuery(sql, selectionArgs);
	}
}
