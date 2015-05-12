/**
 * 
 */
package fr.chklang.dontforget.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author S0075724
 *
 */
public class DatabaseManager {

	private static int VERSION = 1;

	private static ThreadLocal<DatabaseConnection> connection = new InheritableThreadLocal<DatabaseConnection>();

	public static void initConnection(Context pContext) {
		connection.set(new DatabaseConnection(pContext, "DONT_FORGET", null, VERSION));
	}

	public static DatabaseConnection getConnection() {
		return connection.get();
	}
	
	public static SQLiteDatabase getWrittableDatabase() {
		return connection.get().getWritableDatabase();
	}
	
	public static SQLiteDatabase getReadableDatabase() {
		return connection.get().getReadableDatabase();
	}
}
