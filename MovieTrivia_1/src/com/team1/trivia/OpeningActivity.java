package com.team1.trivia;

import java.util.ArrayList;
import java.util.List;

import com.team1.trivia.R;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OpeningActivity extends TriviaActivity implements OnClickListener {
	static final int DIALOG_START_ID = 0;
    static final int DIALOG_CATEGORY_ID = 1;
    static final int DIALOG_OPTIONS_ID = 2;
    static final int DIALOG_MODE_ID = 3;
    static final int DIALOG_INSTRUCTIONS_ID = 4;
    protected static int maxAnswers = 3;
    protected static List<String> cat = new ArrayList<String>();
    SharedPreferences mPrefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.opening);
        
        // Activate check FirstRun
        firstRunPreferences();
        
        // Set up button for OnClickListener
        Button startButton = (Button) findViewById(R.id.startGameButton);
        Button categoriesButton = (Button) findViewById(R.id.categoriesButton);
        Button optionsButton = (Button) findViewById(R.id.optionsButton);
        Button modeButton = (Button) findViewById(R.id.modeButton);
        Button instructionButton = (Button) findViewById(R.id.instructionButton);
        
        // Waits for one of the buttons to be clicked
        startButton.setOnClickListener(this);
        categoriesButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
        modeButton.setOnClickListener(this);
        instructionButton.setOnClickListener(this);
    
        // Create a background gradient
        GradientDrawable grad = new GradientDrawable(Orientation.TOP_BOTTOM,
                        new int[] { Color.WHITE, Color.BLUE });
        this.getWindow().setBackgroundDrawable(grad);

    } // End onCreate

    /**
     * setting up preferences storage
     */
    public void firstRunPreferences() 
    {
            Context mContext = this.getApplicationContext();
            mPrefs = mContext.getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE); 
            // 0 = mode private: only this app can read these preferences
    }
    
    // This method tells the app what to do when a button is clicked
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.startGameButton:
			String playerName = "";
			
			// Instantiate the Shared Preferences class
			SharedPreferences sp = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
			Editor ed = sp.edit();

			// Retrieve the player's name
			EditText playerNameEditText = (EditText) findViewById(R.id.playerNameEditText);
			playerName = playerNameEditText.getText().toString();

			if (!playerName.equals("")) {
				ed.putString(GAME_PREFERENCES_PLAYER, playerName);
			} else {
				playerName = "Player 1";
				ed.putString(GAME_PREFERENCES_PLAYER, playerName);
			}

			ed.commit();

			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.toast_layout,
			                               (ViewGroup) findViewById(R.id.toast_layout_root));

			ImageView image = (ImageView) layout.findViewById(R.id.image);
			image.setImageResource(R.drawable.sm_logo);
			TextView text = (TextView) layout.findViewById(R.id.text);
			text.setText("Get ready to play, " + sp.getString(GAME_PREFERENCES_PLAYER, "Player 1"));

			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
			
			// Call the Game Activity and close the Opening Activity
			startActivity(new Intent(
					OpeningActivity.this, 
            		GameActivity.class));
			OpeningActivity.this.finish();

			break;

		case R.id.categoriesButton:
			showDialog(DIALOG_CATEGORY_ID);
	         break;
		
		case R.id.optionsButton:
			showDialog(DIALOG_OPTIONS_ID);
			break;
	         
		case R.id.modeButton:
			showDialog(DIALOG_MODE_ID);
			break;
			
		case R.id.instructionButton:
			showDialog(DIALOG_INSTRUCTIONS_ID);
			break;
	        
		} // End switch
	} // End onClick
	
	// This method tells the app the details of what to do to create the dialogs
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0: // Display the startup dialog and start the game


		case 1: // Display the categories dialog
			final CharSequence[] categories = { // Put the categories here so they are easier to update
					"Select All", "Action", "Adventure", "Drama",
					"Westerns", "Mystery","Horror",
					"Sci-Fi"}; 
		    final boolean[] categoryStates = {false, false, false,
		    		false,false,false,false,false}; 

		    // Build the dialog
			return new AlertDialog.Builder(this)
			.setTitle("Select Categories:")
			.setCancelable(false)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() { 
                public void onClick(DialogInterface dialog, int which) {
                    for (int i = 0; i < categories.length; i++) { 
                    	if (categoryStates[i]) {
                    		cat.add((String) categories[i]);
                    		}
                    	} // End for loop
                    } // End onClick method
                }) // End setPositiveButton
                
            // Set up the Category list with checkboxes
	        .setMultiChoiceItems(categories, categoryStates, new DialogInterface.OnMultiChoiceClickListener() { 
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                	if (which == 0) {  // If Select All is clicked, check all checkboxes
            			for (int i = 1; i < categories.length; i++) {
            				categoryStates[i]=true;
            			}
                	} // End if
                } // End onClick
            }) // End setMultipleChoiceItems
            
            .create();
		
		case 2: // display the options dialog
			final CharSequence[] options = {"Easy (3 choices)", "Medium (6 choices)", "Hard (9 choices)"}; 

			// Build the options dialog
			return new AlertDialog.Builder(this)
			.setTitle("Select Difficulty:")
			.setCancelable(false)
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() { 
                 public void onClick(DialogInterface dialog, int which) { 
        	        // Temporary Toast to show that the onClick method works
                	Toast.makeText(getBaseContext(), "Maximum number of answers " + maxAnswers, Toast.LENGTH_LONG).show();
                } // End onClick
            }) // End setPositiveButton
            
            // Set up the radio buttons
	        .setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {    
				public void onClick(DialogInterface dialog, int item) {        
                    switch (item) {
                    case 0:
                    	maxAnswers = 3;
                    	break;
                    case 1:
                    	maxAnswers = 6;
                    	break;
                    case 2:
                    	maxAnswers = 9;
                    	break;
                    } // End switch
                    
                 // Instantiate the Shared Preferences class
					SharedPreferences sp = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
					Editor ed = sp.edit();
					
					ed.putInt(GAME_PREFERENCES_DIFFICULTY, maxAnswers);
					ed.commit();
					
                } // End onClick
	        }) // End setSingleChoiceItems
	        
            .create();
	         
		case 3: // Display the set mode dialog
			final CharSequence[] modes = {"Limited", "Unlimited"};
			final boolean[] modeStates = {true, false};
			
			// Build the dialog
			return new AlertDialog.Builder(this)
			.setTitle("Select Game Mode")
			.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
 
                public void onClick(DialogInterface dialog, int which) { 
                    for (int i = 0; i < modes.length; i++) { 
                    if (modeStates[i]) { 
                    	// Instantiate the Shared Preferences class
    					SharedPreferences sp = getSharedPreferences(GAME_PREFERENCES, MODE_PRIVATE);
    					Editor ed = sp.edit();
    					
    					ed.putString(GAME_PREFERENCES_MODE, modes[i].toString());
    					ed.commit();
    					}
                    } 
                } 
            })
            
            // Set up the radio buttons
            .setSingleChoiceItems(modes, -1, new DialogInterface.OnClickListener() {    
				public void onClick(DialogInterface dialog, int item) {        
					Toast.makeText(getApplicationContext(), modes[item], Toast.LENGTH_SHORT).show();
					}
				})
			.create();
			
		case 4: // Display the Help view
	         Builder instructionDialog = new AlertDialog.Builder(this);
	         LayoutInflater instructionInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	         // Uses a pre-constructed layout instead of an AlertDialog
	         View instructions = instructionInflater.inflate(R.layout.instructions_dialog, null);
	         instructionDialog.setCancelable(true);
	         instructionDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) { 
	            	   // Do nothing except close the dialog
	               } // end method onClick
	            } // end DialogInterface
	         ); // end call to dialogBuilder.setPositiveButton

	         instructionDialog.setView(instructions);
	         instructionDialog.show();
	         return null;
	         
		} // End switch
		return null;
		
	} // End onCreateDialog method
} // End OpeningActivity class