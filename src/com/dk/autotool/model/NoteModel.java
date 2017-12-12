package com.dk.autotool.model;

import java.util.HashMap;
import java.util.Map;

public class NoteModel {
	public static Map<String, String> sCaptionMap = new HashMap();
	private String mBugNum;
	private String mAuthor;
	private String mDate;
	private String mReason;

	public NoteModel() {
		sCaptionMap.put("mBugNum", "Number");
		sCaptionMap.put("mAuthor", "Author");
		sCaptionMap.put("mDate", "change Date");
		sCaptionMap.put("mReason", "change Reason");
	}

	public String getMBugNum() {
		return this.mBugNum;
	}

	public void setMBugNum(String bugNum) {
		this.mBugNum = bugNum;
	}

	public String getMAuthor() {
		return this.mAuthor;
	}

	public void setMAuthor(String author) {
		this.mAuthor = author;
	}

	public String getMDate() {
		return this.mDate;
	}

	public void setMDate(String date) {
		this.mDate = date;
	}

	public String getMReason() {
		return this.mReason;
	}

	public void setMReason(String reason) {
		this.mReason = reason;
	}
}
