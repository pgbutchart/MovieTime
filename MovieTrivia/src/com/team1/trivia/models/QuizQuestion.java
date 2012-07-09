package com.team1.trivia.models;

// Model to contain Quiz Question data
public class QuizQuestion {
	private int[] _id;
	private String[] phrase;
	private String[] title;
	
	

	public QuizQuestion(int[] _id, String[] phrase, String[] title) {
		super();
		this._id = _id;
		this.phrase = phrase;
		this.title = title;
	}
	public int[] get_id() {
		return _id;
	}
	public void set_id(int[] _id) {
		this._id = _id;
	}
	public String[] getPhrase() {
		return phrase;
	}
	public void setPhrase(String[] phrase) {
		this.phrase = phrase;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
}
