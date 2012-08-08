package com.team1.trivia;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QueryBuilder extends TriviaActivity {
	public static String qry;
	private Integer dbCount = 0;

	// Create a List to hold the random numbers
	protected static List<Integer> numbers = new ArrayList<Integer>();
	
	public QueryBuilder() {
		super();
		}

		public String getQuestions() {
			dbCount = DatabaseAdapter.getCount();
		
			// First we get a set of random numbers
			randomize(dbCount);
			
			String cats = categories();
			qry = assembleQuery(cats);
			
			return qry;
			}

		// Set up the WHERE clause for the categories
		public String categories() {
			String catFilter = "";
			
			for (String s: OpeningActivity.cat) {
				if (s != "Select All") { 	// If the user selects 'Select All' we don't need to build
											// our WHERE clause.
					for (Integer i=1; i<4; i++) {
						if (catFilter == "") {
							catFilter = "genre_" + i + "=\"" + s + "\"";
						} else {
							catFilter = "genre_" + i + "=\"" + s + "\" OR " + catFilter;
						}
					}
					
					
				} else {					// so we make sure the filter is empty and exit the for loop.
					catFilter = "";
					break;
				}
	        }
			if (catFilter != "") {
				catFilter = catFilter + ");";
			}
			
			return catFilter;
		}
		
		protected String assembleQuery(String cats) {
			
			StringBuffer sb = new StringBuffer();
			String sqry = "SELECT phrase._id, phrase, title FROM phrase, movie WHERE (";
			String rn = "";		// Initialize a String for the random numbers
			
			sb.append(sqry);
			
			// Assemble the clause for the 6 random numbers
			for (Integer i: numbers) {
				if (rn == "") {
					rn = "phrase._id=" + i.toString();
				} else {
					rn = rn + " OR phrase._id=" + i.toString();
				}
			}
			
			rn = rn + ")";
			sb.append(rn);
			
			// Add the categories if any are selected
			if (cats != "") {
				sb.append(" AND (" + cats);
			}
			
			sb.append("AND movie._id=phrase.movieID;");
			
			return sb.toString();
		}
		
		// Create an array with random numbers 
		public void randomize(Integer count){
		    for(int i = 0; i < 6; i++){
				// Add a random number between 0 and the number of rows in the Phrase table.
		    	Random randomNumber = new Random();
		    	numbers.add(randomNumber.nextInt(count)); 
		    	}
		    }
}
