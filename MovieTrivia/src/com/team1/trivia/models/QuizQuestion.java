package com.team1.trivia.models;

import java.util.ArrayList;

// Model to contain Quiz Question data
public class QuizQuestion {
	private ArrayList<Integer> _id;
	private ArrayList<String> phrase;
	private ArrayList<String> title;

	public QuizQuestion(ArrayList<Integer> _id, ArrayList<String> phrase,
			ArrayList<String> title) {
		super();
		this._id = _id;
		this.phrase = phrase;
		this.title = title;
	}

	public ArrayList<Integer> get_id() {
		return _id;
	}

	public void set_id(ArrayList<Integer> _id) {
		this._id = _id;
	}

	public ArrayList<String> getPhrase() {
		return phrase;
	}

	public void setPhrase(ArrayList<String> phrase) {
		this.phrase = phrase;
	}

	public ArrayList<String> getTitle() {
		return title;
	}

	public void setTitle(ArrayList<String> title) {
		this.title = title;
	}
}
