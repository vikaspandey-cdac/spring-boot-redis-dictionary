package com.techgig.challenge.happiestmind.model;

public class DictionaryData implements Comparable<DictionaryData> {

	private String value;
	private int score;

	public DictionaryData(String value, int score) {
		this.value = value;
		this.score = score;
	}

	@Override
	public int compareTo(DictionaryData dictionaryData) {
		return ((Integer)dictionaryData.getScore()).compareTo(this.score);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "DictionaryData{" +
				"value='" + value + '\'' +
				'}';
	}
}
