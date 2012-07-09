package com.team1.trivia.controllers;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.team1.trivia.QueryBuilder;
import com.team1.trivia.activities.GameActivity;
import com.team1.trivia.activities.ScoringActivity;
import com.team1.trivia.daos.DatabaseAdapter;
import com.team1.trivia.daos.DatabaseAdapter.DatabaseHelper;
import com.team1.trivia.models.QuizQuestion;

public class GameController {
	private static GameActivity ga = new GameActivity(null);
	private static Context c = ga.getContext();
	private static DatabaseAdapter DBA = new DatabaseAdapter(c);
    private static DatabaseHelper DBHelper = DBA.new DatabaseHelper(c);
    private static QueryBuilder qb = new QueryBuilder();
	private static ListView titleListView = new ListView(c);
	static final String ROW_ID = "row_id";
	
    @SuppressWarnings("unused")
	public static void newGame() {
		// TODO Set up user name and scoring
    	qb.getQuestions();
    	// Retrieves the id's, phrases, and titles.
    	QuizQuestion qq = DBHelper.getTitles(QueryBuilder.qry);
		
		
    	titleListView.setOnItemClickListener(titleListener);
    	}

	// event listener that responds to the user touching a title
	// in the ListView
	static OnItemClickListener titleListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			// create an Intent to launch the Scoring Activity
			Intent scoring = new Intent(c, ScoringActivity.class);
 
			// pass the selected title's row ID as an extra with the Intent
			scoring.putExtra(ROW_ID, arg3);
			c.startActivity(scoring); // start the Scoring Activity
			} // end method onItemClick
		}; // end titleListener


}
