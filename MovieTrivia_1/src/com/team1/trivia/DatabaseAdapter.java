package com.team1.trivia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/*** 
 * Helper singleton class to manage SQLiteDatabase Create and Restore
 * 
 * Modified from SQLiteDatabaseAdapter by Alessandro Franzi
 * http://code.google.com/p/almanac/source/browse/trunk/Almanac/#Almanac%2Fres%2Fxml
 *
 */ 
public class DatabaseAdapter extends SQLiteOpenHelper 
{
	private Context context;
	// private String DB_PATH_PACKAGE = context.getPackageName();
	private static SQLiteDatabase myDb;
	private static DatabaseAdapter instance;
	private static String DB_PATH_PREFIX = "/data/data/";
	private static String DB_PATH_SUFFIX = "/databases/";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "movie_time";
	private static final String TAG = "DatabaseAdapter";
	private String DB_PATH = DB_PATH_PREFIX + "com.team1.trivia" + DB_PATH_SUFFIX;
	private static final String PHRASE_TABLE = "phrase";

	/***         
	 * Constructor          
	 *           
	 */         
	private DatabaseAdapter(Context context, String name,
			CursorFactory factory, int version) 
	{
		super(context, name, factory, version);
		this.context = context;
		Log.i(TAG, "Create or Open database : " + name);
	}

	DatabaseAdapter(Context c) 
	{
		super(c, DATABASE_NAME, null, 0);
		this.context = c;
		Log.i(TAG, "Create or Open database : " + DATABASE_NAME);
	}

	/***          
	 * Initialize method          
	 */         
	private static void initialize(Context context, String databaseName) 
	{
		if (instance == null) {
			/**                          
			 * Try to check if there is an copy of DB in Database                          
			 * Directory                          
			 */                         
			if (!checkDatabase(context, databaseName)) 
			{
				// if not exist, I try to copy from asset dir                                 
				try 
				{                                         
					copyDataBase(context, databaseName);
				} catch (IOException e) {
					Log.e(TAG,"Database " + databaseName 
							+ " does not exist and there is no Original Version in Asset dir");
				}
			}

			Log.i(TAG, "Try to create instance of database (" + databaseName + ")");
			instance = new DatabaseAdapter(context, databaseName,
					null, DATABASE_VERSION);
			myDb = instance.getWritableDatabase();
			Log.i(TAG, "instance of database (" + databaseName + ") created !");
		}
	}          

	/***          
	 * Static method for getting singleton instance          
	 */         
	public static final DatabaseAdapter getInstance(
			Context context, String databaseName) 
	{
		initialize(context, databaseName);
		return instance;
	} 

	/***          
	 * Method to get database instance          
	 */         
	public static SQLiteDatabase getDatabase() 
	{
		return myDb;
	}          

	@Override         
	public void onCreate(SQLiteDatabase db) 
	{
		Log.d(TAG, "onCreate : nothing to do");
	}          

	@Override         
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		Log.d(TAG, "onUpgrade : nothing to do");
	}          

	/***          
	 * Method to Copy the database from asset directory to application's data          
	 * directory          
	 */         
	@SuppressWarnings("unused")
	private void copyDataBase(String databaseName) throws IOException 
	{
		copyDataBase(context, databaseName);
	}

	/***          
	 * Static method for copy the database from asset directory to application's          
	 * data directory          
	 */         
	private static void copyDataBase(Context aContext, String databaseName) throws IOException 
	{
		// Open your local db as the input stream
		InputStream myInput = aContext.getAssets().open(databaseName);

		// Path to the just created empty db
		String outFileName = getDatabasePath(aContext, databaseName);
		Log.i(TAG, "Check if create dir : " + DB_PATH_PREFIX
				+ aContext.getPackageName() + DB_PATH_SUFFIX);

		// if the path doesn't exist first, create it
		File f = new File(DB_PATH_PREFIX + aContext.getPackageName()
				+ DB_PATH_SUFFIX);
		if (!f.exists())
			f.mkdir();
		Log.i(TAG, "Trying to copy local DB to : " + outFileName);

		// Open the empty db as the output stream                 
		OutputStream myOutput = new FileOutputStream(outFileName);
		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;                 
		while ((length = myInput.read(buffer)) > 0) 
		{
			myOutput.write(buffer, 0, length);
		}                  

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
		Log.i(TAG, "DB (" + databaseName + ") copied!");
	}

	/***
	 * Method to check if database exists in application's data directory         
	 */
	public boolean checkDatabase(String databaseName) 
	{
		return checkDatabase(context, databaseName);
	}          

	/***          
	 * Static Method to check if database exists in application's data directory          
	 */         
	public static boolean checkDatabase(Context aContext, String databaseName) 
	{
		SQLiteDatabase checkDB = null;
		try 
		{
			String myPath = getDatabasePath(aContext, databaseName);
			Log.i(TAG, "Trying to connect to : " + myPath);
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
			Log.i(TAG, "Database " + databaseName + " found!");
			checkDB.close();
		} catch (SQLiteException e) {
			Log.i(TAG, "Database " + databaseName + " does not exist!");
		}
		return checkDB != null ? true : false;
	}

	/***          
	 * Static Method that returns database path in the application's data          
	 * directory          
	 */         
	private static String getDatabasePath(Context aContext, String databaseName) 
	{
		return DB_PATH_PREFIX + aContext.getPackageName() + DB_PATH_SUFFIX
				+ databaseName;
	}

	public String getDB_PATH() 
	{
		return DB_PATH;
	}

	public void setDB_PATH(String dB_PATH) 
	{
		DB_PATH = dB_PATH;
	}

	// Get the phrases and titles
	public static QuizQuestion getTitles(String qry) 
	{
		QuizQuestion qq = new QuizQuestion(null, null, null);
		ArrayList<Integer> id = new ArrayList<Integer>();
		ArrayList<String> phrase = new ArrayList<String>();
		List<String> title = new ArrayList<String>();

		myDb = getDatabase();

		Cursor results = myDb.rawQuery(qry, null);

		if (results.getCount() > 0) 
		{
			int i = 0;
			while (results.moveToNext()) 
			{
				id.add (i, results.getInt(0));
				phrase.add(i, results.getString(1));
				title.add(i, results.getString(2));
				i++;
			}
		}

		qq.set_id(id);
		qq.setPhrase(phrase);
		qq.setTitle(title);

		results.close();

		return qq;
	}

	// Get the number of rows in the Phrase table
	public static int getCount() 
	{
		myDb = getDatabase();

		// Find the number of rows in the Phrase table.
		long count = DatabaseUtils.queryNumEntries(myDb, PHRASE_TABLE);

		return (int) count;
	} // end getCount
}
