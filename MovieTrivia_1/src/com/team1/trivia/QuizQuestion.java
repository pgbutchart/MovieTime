package com.team1.trivia;

import java.util.ArrayList;
import java.util.List;

// Model to contain Quiz Question data
public class QuizQuestion {
	private List<Integer> _id;
	private List<String> phrase;
	private List<String> title;

	public QuizQuestion(ArrayList<Integer> _id, ArrayList<String> phrase,
			List<String> title) {
		super();
		this._id = _id;
		this.phrase = phrase;
		this.title = title;
	}

	public List<Integer> get_id() {
		return _id;
	}

	public void set_id(List<Integer> id) {
		this._id = id;
	}

	public List<String> getPhrase() {
		return phrase;
	}

	public void setPhrase(List<String> phrase) {
		this.phrase = phrase;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

}
