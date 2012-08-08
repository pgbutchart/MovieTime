package com.team1.trivia;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GameActivity extends ListActivity 
{
	public GameActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	static final int DIALOG_INSTRUCTIONS_ID = 4;
	static final String ROW_ID = "row_id";
	private CursorAdapter titleAdapter;
	public static Context context = getContext();
	@SuppressWarnings("unused")
	private SQLiteDatabase myDb;
	private QueryBuilder qb = new QueryBuilder();
	private ListView titleListView;
	DatabaseAdapter aDatabaseAdapter;

	private static final String DB_NAME = "movie_time.db";
	private static String DB_PATH_PREFIX = "/data/data/";
	private static String DB_PATH_SUFFIX = "/databases/";
	private static String DB_PACKAGE = "com.team1.trivia";
	private static String DB_PATH;
	SharedPreferences mPrefs; 

	public static Context getContext() 
	{
		return context;
	}

	// Check to see if this is the first time the application has been run
	public boolean getFirstRun() 
	{
		mPrefs = this.getSharedPreferences(
				TriviaActivity.GAME_PREFERENCES, MODE_PRIVATE);
		return mPrefs.getBoolean("firstRun", true);
	}

	// Set first run status
	public void setRunned() 
	{
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("firstRun", false);
		edit.commit();
	}

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// copyDataBase();

		// Set view
		setContentView(R.layout.game);

		// Identify our List View
		titleListView = (ListView) findViewById(android.R.id.list);

		aDatabaseAdapter = DatabaseAdapter.getInstance(this, DB_NAME);

		// Create a background gradient
		GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM,
				new int[] { Color.WHITE, Color.BLUE });
		this.getWindow().setBackgroundDrawable(grad);

		if (getFirstRun()) 
		{
			myDb = DatabaseAdapter.getDatabase();
			setRunned();
		} else {
			myDb = aDatabaseAdapter.getWritableDatabase();
		}

		// Return an ArrayList of QuizQuestion
		QuizQuestion qq = newGame();

		// Break out the individual arrays from the qq
		List<Integer> id = qq.get_id();
		List<String> phrase = qq.getPhrase();
		List<String> title = qq.getTitle();

		int answer = SelectCorrectAnswer();

		int correctId = id.get(answer);
		String correctPhrase = phrase.get(answer);
		ScoringActivity.setCorrectId(correctId);
		TextView tvPhrase = (TextView) findViewById(R.id.phraseTextView);
		tvPhrase.setText(correctPhrase);
				
		// Create an ArrayAdapter that puts the titles into the TextView field of our ListView
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), 
				R.layout.list_item, R.id.titleTextView, title);

		// Using the adapter
		((ListView) findViewById(android.R.id.list)).setAdapter(adapter);

		// Set our item click listener for the ListView
		titleListView.setOnItemClickListener(titleListener);
	}

	private int SelectCorrectAnswer() 
	{
		int maxTitles = mPrefs.getInt("maxTitles", 6);
		// Add a random number between 0 and the number of rows in the Phrase table.
    	Random randomNumber = new Random();
    	return (randomNumber.nextInt(maxTitles));
	}
	
	@SuppressWarnings("unused")
	private void copyDataBase()
	{
		DB_PATH = DB_PATH_PREFIX + DB_PACKAGE + DB_PATH_SUFFIX;

		try
		{
			//Open the file in the assets directory as the input stream
			AssetManager am = getAssets();

			InputStream myInput;
			myInput = am.open(DB_NAME); 

			// Path to the just created empty db 
			String outFileName = DB_PATH + DB_NAME; 

			File myFile = new File(outFileName);
			if (!myFile.exists())
			{
				myFile.canWrite();
				myFile.createNewFile();
			}

			//Open the empty db file as the output stream 
			BufferedOutputStream myOutput = new BufferedOutputStream(new FileOutputStream(outFileName)); 

			//transfer bytes from the input file to the output file 
			byte[] buffer = new byte[1024]; 
			int length; 
			while ((length = myInput.read(buffer))>0)
			{ 
				myOutput.write(buffer, 0, length); 
			} 

			//Close the streams 
			myOutput.flush(); 
			myOutput.close(); 
			myInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	private QuizQuestion newGame() 
	{
		// TODO Set up user name and scoring
		qb.getQuestions();

		QuizQuestion qq = DatabaseAdapter.getTitles(QueryBuilder.qry);

		return qq;
	}

	@Override
	protected void onStop() 
	{
		Cursor cursor = titleAdapter.getCursor();

		if (cursor != null)
			cursor.deactivate();

		titleAdapter.changeCursor(null);
		super.onStop();
	}

	// Create the Options menu, displayed when Menu button is pressed
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.game_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle menu item selection
		switch (item.getItemId()) 
		{
		case R.id.new_game:
			newGame();
			return true;
		case R.id.help:
			showHelp();
			return true;
		case R.id.exit:
			exitGame();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// event listener that responds to the user touching a title
	// in the ListView
	OnItemClickListener titleListener = new OnItemClickListener() 
	{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) 
		{
			// create an Intent to launch the Scoring Activity
			Intent scoring = new Intent(GameActivity.this, ScoringActivity.class);

			// pass the selected title's row ID as an extra with the Intent
			scoring.putExtra(ROW_ID, arg3);
			startActivity(scoring); // start the Scoring Activity
		} // end method onItemClick
	}; // end titleListener

	private void exitGame() 
	{
		// TODO Create logic to settle storage before exit.
	}

	private void showHelp() 
	{
		// Show the instructions dialog
		showDialog(DIALOG_INSTRUCTIONS_ID);
	}

	// Standard onCreateDialog logic
	@Override
	protected Dialog onCreateDialog(int id) 
	{
		Builder instructionDialog = new AlertDialog.Builder(this);
		LayoutInflater instructionInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Uses a pre-constructed layout instead of an AlertDialog
		View instructions = instructionInflater.inflate(R.layout.instructions_dialog, null);
		instructionDialog.setCancelable(true);
		instructionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				// Do nothing except close the dialog
			} // end method onClick
		}); // end call to dialogBuilder.setPositiveButton
		// end onCreateDialog

		instructionDialog.setView(instructions);
		instructionDialog.show();
		return null;
	}
}
