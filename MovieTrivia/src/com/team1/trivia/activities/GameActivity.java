package com.team1.trivia.activities;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.team1.trivia.QueryBuilder;
import com.team1.trivia.R;
import com.team1.trivia.daos.DatabaseAdapter;
import com.team1.trivia.daos.DatabaseAdapter.DatabaseHelper;
import com.team1.trivia.models.QuizQuestion;

public class GameActivity extends TriviaActivity {
	public GameActivity() {
		super();
		// TODO Auto-generated constructor stub
	}

	static final int DIALOG_INSTRUCTIONS_ID = 4;
	static final String ROW_ID = "row_id";
	private CursorAdapter titleAdapter;
	private DatabaseAdapter DBA = new DatabaseAdapter(this);
    private DatabaseHelper DBHelper = DBA.new DatabaseHelper(this);
	private SQLiteDatabase myDb;
	private QueryBuilder qb = new QueryBuilder();
	private ListView titleListView;
	
	// Called when the activity is first created.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        try {
			setMyDb(DBHelper.createDataBase());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        newGame();
        }
    
    @SuppressWarnings("unused")
	private void newGame() {
		// TODO Set up user name and scoring
    	qb.getQuestions();
    	
    	QuizQuestion qq = DBHelper.getTitles(QueryBuilder.qry);
		
		
    	titleListView.setOnItemClickListener(titleListener);
    	}

    @Override
    protected void onStop() {
    	Cursor cursor = titleAdapter.getCursor();
    	
    	if (cursor != null)
    		cursor.deactivate();
    	
    	titleAdapter.changeCursor(null);
    	super.onStop();
    	}
    
	public SQLiteDatabase getMyDb() {
		return myDb;
	}

	public void setMyDb(SQLiteDatabase myDb) {
		this.myDb = myDb;
	}

	// Create the Options menu, displayed when Menu button is pressed
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
        }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle menu item selection
	    switch (item.getItemId()) {
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
		OnItemClickListener titleListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// create an Intent to launch the Scoring Activity
				Intent scoring = new Intent(GameActivity.this, ScoringActivity.class);
	 
				// pass the selected title's row ID as an extra with the Intent
				scoring.putExtra(ROW_ID, arg3);
				startActivity(scoring); // start the Scoring Activity
				} // end method onItemClick
			}; // end titleListener

		private void exitGame() {
			// TODO Create logic to settle storage before exit.
			}

		private void showHelp() {
			// Show the instructions dialog
			showDialog(DIALOG_INSTRUCTIONS_ID);
			}
	
		// Standard onCreateDialog logic
		@Override
		protected Dialog onCreateDialog(int id) {
			Builder instructionDialog = new AlertDialog.Builder(this);
			LayoutInflater instructionInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
			// Uses a pre-constructed layout instead of an AlertDialog
			View instructions = instructionInflater.inflate(R.layout.instructions_dialog, null);
			instructionDialog.setCancelable(true);
			instructionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// Do nothing except close the dialog
					} // end method onClick
				} // end call to dialogBuilder.setPositiveButton
			); // end onCreateDialog
	
			instructionDialog.setView(instructions);
			instructionDialog.show();
			return null;
			}

}
