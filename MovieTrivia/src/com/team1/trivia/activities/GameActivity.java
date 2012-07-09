package com.team1.trivia.activities;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;

import com.team1.trivia.R;
import com.team1.trivia.controllers.GameController;
import com.team1.trivia.daos.DatabaseAdapter;
import com.team1.trivia.daos.DatabaseAdapter.DatabaseHelper;

public class GameActivity extends TriviaActivity {

	static final int DIALOG_INSTRUCTIONS_ID = 4;
	static final String ROW_ID = "row_id";
	protected Context context;
	private CursorAdapter titleAdapter;
	private DatabaseAdapter DBA = new DatabaseAdapter(this);
    private DatabaseHelper DBHelper = DBA.new DatabaseHelper(this);
	@SuppressWarnings("unused")
	private SQLiteDatabase myDb;

	public GameActivity(Context context) {
		super();
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

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
		
        GameController.newGame();
        }
    

    @Override
    protected void onStop() {
    	Cursor cursor = titleAdapter.getCursor();
    	
    	if (cursor != null)
    		cursor.deactivate();
    	
    	titleAdapter.changeCursor(null);
    	super.onStop();
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
	            GameController.newGame();
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
