package com.team1.trivia.daos;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.team1.trivia.models.QuizQuestion;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// We're extending the ListActivity class in order to use specific functions.
public class DatabaseAdapter extends ListActivity {
	private static final String DB_NAME = "movie_time.db";
	private static final Integer DB_VERSION = 1;
	private static String DB_PATH_PREFIX = "/data/data/";
	private static String DB_PATH_SUFFIX = "/databases/";
	private static String DB_PACKAGE = "com.team1.trivia";
	private static String DB_PATH;
	private static final String PHRASE_TABLE = "phrase";
	private static final String TITLE_TABLE = "movie";
	
	
	private Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase myDb;
	
	// DatabaseAdapter Constructor
	public DatabaseAdapter(Context c) {
		this.context = c;
		setDBHelper(new DatabaseHelper(context));
		}
	
	public DatabaseHelper getDBHelper() {
		return DBHelper;
	}

	public void setDBHelper(DatabaseHelper dBHelper) {
		DBHelper = dBHelper;
	}

	// Database helper class contains all database functions
	public class DatabaseHelper extends SQLiteOpenHelper {
		private Context context;

		public DatabaseHelper(Context c) {
			super(c, DB_NAME, null, DB_VERSION);
			this.context = c;
			} // End constructor
	
		// Since we are copying the database from the assets directory,
		// we don't need any logic here.
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			}
		 
		// Required method. Will drop the tables and recreate them on update
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DatabaseHelper.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + PHRASE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + TITLE_TABLE);
			onCreate(db);
			} // End onUpgrade
		
		// Method to get database instance          
		public SQLiteDatabase getDatabase() {
			return myDb;
			}          

		public void open() throws SQLException {
			//Open the database
			String myPath = DB_PATH + DB_NAME;
			myDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			}
		 
		@Override
		public synchronized void close() {
			if(myDb != null)
				myDb.close();
			super.close();
			}

		public SQLiteDatabase createDataBase() throws IOException {
			boolean dbExist = checkDataBase();
			
			if (dbExist) {
				// Do nothing
			} else {
				try {
					copyDataBase();
				} catch (Exception e) {
					Log.e("Database failed to copy correctly. " + e.getLocalizedMessage(), null);
					}
				}
			
			myDb = this.getReadableDatabase();
			return myDb;
			}
		
		// Check if the database already exists so we don't re-copy every time
		private boolean checkDataBase() {
			SQLiteDatabase checkDB = null;
			StringBuilder sb = new StringBuilder();
			sb.append(DB_PATH_PREFIX);
			sb.append(DB_PACKAGE);
			sb.append(DB_PATH_SUFFIX);
			DB_PATH = sb.toString();
			
			try {
				String myPath = DB_PATH + DB_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			} catch (SQLiteException e) {
				// the database does not exist.
			}
			
			return checkDB != null ? true : false;
			}

		private void copyDataBase() throws IOException{ 
		    //Open the file in the assets directory as the input stream 
		    InputStream myInput = context.getAssets().open(DB_NAME); 
		 
		    // Path to the just created empty db 
		    String outFileName = DB_PATH + DB_NAME; 
		    
		    //Open the empty db file as the output stream 
		    OutputStream myOutput = new FileOutputStream(outFileName); 
		 
		    //transfer bytes from the input file to the output file 
		    byte[] buffer = new byte[1024]; 
		    int length; 
		    while ((length = myInput.read(buffer))>0){ 
		        myOutput.write(buffer, 0, length); 
		    } 
		
		    //Close the streams 
		    myOutput.flush(); 
		    myOutput.close(); 
		    myInput.close(); 
		    } 

		
		// Get the number of rows in the Phrase table
		public int getCount() {
			// Find the number of rows in the Phrase table.
			long count = DatabaseUtils.queryNumEntries(myDb, PHRASE_TABLE);
			
			return (int) count;
			} // end getCount
		
		// Get the phrases and titles
		@SuppressWarnings("null")
		public QuizQuestion getTitles(String qry) {
			QuizQuestion qq = null;
			int[] id = new int[5];
			String[] phrase = new String[5];
			String[] title = new String[5];
			
			Cursor results = myDb.rawQuery(qry, null);
			//startManagingCursor(results);
			
			if (results.getCount() > 0) {
				int i = 0;
				while (results.moveToNext()) {
					id[i] = results.getInt(0);
					phrase[i] = results.getString(1);
					title[i] = results.getString(2);
					i++;
				}
			}
			
			qq.set_id(id);
			qq.setPhrase(phrase);
			qq.setTitle(title);
			
			return qq;
			}
		}
	}
