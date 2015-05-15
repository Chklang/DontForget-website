/**
 * 
 */
package fr.chklang.dontforget.android.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author S0075724
 *
 */
public class DatabaseConnection extends SQLiteOpenHelper {

	public DatabaseConnection(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String lTableCategory = "CREATE TABLE \"t_category\" ("
				+ "\"idCategory\" INTEGER PRIMARY KEY NOT NULL,"
				+ "\"name\" VARCHAR NOT NULL,"
				+ "\"lastUpdate\" INTEGER NOT NULL,"
				+ "\"uuid\" VARCHAR, UNIQUE (name) )";
		
		String lTableConfiguration = "CREATE TABLE \"t_configuration\" ("
				+ "\"confkey\" VARCHAR PRIMARY KEY NOT NULL,"
				+ "\"value\" VARCHAR)";
		
		String lTablePlace = "CREATE TABLE \"t_place\" ("
				+ "\"idPlace\" INTEGER PRIMARY KEY NOT NULL,"
				+ "\"name\" VARCHAR NOT NULL,"
				+ "\"lastUpdate\" INTEGER NOT NULL,"
				+ "\"uuid\" VARCHAR )";
		
		String lTableTag = "CREATE TABLE \"t_tag\" ("
				+ "\"idTag\" INTEGER PRIMARY KEY NOT NULL,"
				+ "\"name\" VARCHAR NOT NULL,"
				+ "\"lastUpdate\" INTEGER NOT NULL,"
				+ "\"uuid\" VARCHAR )";
		
		String lTableTask = "CREATE TABLE \"t_task\" ("
				+ "\"idTask\" INTEGER PRIMARY KEY NOT NULL,"
				+ "\"text\" TEXT NOT NULL,"
				+ "\"category_id\" INTEGER NOT NULL,"
				+ "\"status\" INTEGER NOT NULL,"
				+ "\"lastUpdate\" INTEGER NOT NULL,"
				+ "\"uuid\" VARCHAR )";
		
		String lTableTaskPlace = "CREATE TABLE \"t_task_place\" ("
				+ "\"idTask\" INTEGER NOT NULL,"
				+ "\"idPlace\" INTEGER NOT NULL,"
				+ "PRIMARY KEY (\"idTask\", \"idPlace\"))";
		
		String lTableTaskTag = "CREATE TABLE \"t_task_tag\" ("
				+ "\"idTask\" INTEGER NOT NULL,"
				+ "\"idTag\" INTEGER NOT NULL,"
				+ "PRIMARY KEY (\"idTask\", \"idTag\"))";
		
		String lTableToken = "CREATE TABLE \"t_token\" ("
				+ "\"pseudo\" VARCHAR NOT NULL,"
				+ "\"protocol\" VARCHAR NOT NULL,"
				+ "\"host\" VARCHAR NOT NULL,"
				+ "\"port\" INTEGER NOT NULL,"
				+ "\"context\" VARCHAR NOT NULL,"
				+ "\"token\" VARCHAR NOT NULL,"
				+ "PRIMARY KEY (\"pseudo\", \"protocol\", \"host\", \"port\", \"context\"),"
				+ "UNIQUE (pseudo, protocol, host, port, context))";

		//Database creation
		db.execSQL(lTableCategory);
		db.execSQL(lTableConfiguration);
		db.execSQL(lTablePlace);
		db.execSQL(lTableTag);
		db.execSQL(lTableTask);
		db.execSQL(lTableTaskPlace);
		db.execSQL(lTableTaskTag);
		db.execSQL(lTableToken);
	}
	
	private void v2(SQLiteDatabase db) {
		String lAddColumnToken = "ALTER TABLE \"t_token\" ADD COLUMN \"lastSynchro\" INTEGER NOT NULL DEFAULT 0";
		db.execSQL(lAddColumnToken);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1 :
			v2(db);
		default :
			break;
		}
	}

}
