package com.dk.autotool.model;

import java.util.HashMap;
import java.util.Map;

public class HeaderFileModel {
	private String mDate;
	private String mAuthor;
	private String mEmail;
	private String mRole;
	private String mReferenceDoc;
	private String mComments;
	private String mFile;
	private String mLabels;
	private String mModifDate;
	private String mKey;
	private String mComment;
	public static Map<String, String> sCaptionMap = new HashMap();

	public HeaderFileModel() {
		sCaptionMap.put("mDate", "Create Date");
		sCaptionMap.put("mAuthor", "Author");
		sCaptionMap.put("mEmail", "Email");
		sCaptionMap.put("mRole", "Role");
		sCaptionMap.put("mReferenceDoc", "Reference documents");
		sCaptionMap.put("mComments", "File Comments");
		sCaptionMap.put("mFile", "File Path");
		sCaptionMap.put("mLabels", "Labels");
		sCaptionMap.put("mModifDate", "Modify Date");
		sCaptionMap.put("mKey", "Key");
		sCaptionMap.put("mComment", "Modify Comment(what, where, why)");
	}

	public String getMDate() {
		return this.mDate;
	}

	public void setMDate(String mDate) {
		this.mDate = mDate;
	}

	public String getMAuthor() {
		return this.mAuthor;
	}

	public void setMAuthor(String author) {
		this.mAuthor = author;
	}

	public String getMEmail() {
		return this.mEmail;
	}

	public void setMEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	public String getMRole() {
		return this.mRole;
	}

	public void setMRole(String role) {
		this.mRole = role;
	}

	public String getMReferenceDoc() {
		return this.mReferenceDoc;
	}

	public void setMReferenceDoc(String referenceDoc) {
		this.mReferenceDoc = referenceDoc;
	}

	public String getMComments() {
		return this.mComments;
	}

	public void setMComments(String comments) {
		this.mComments = comments;
	}

	public String getMFile() {
		return this.mFile;
	}

	public void setMFile(String file) {
		this.mFile = file;
	}

	public String getMLabels() {
		return this.mLabels;
	}

	public void setMLabels(String labels) {
		this.mLabels = labels;
	}

	public String getMModifDate() {
		return this.mModifDate;
	}

	public void setMModifDate(String modifDate) {
		this.mModifDate = modifDate;
	}

	public String getMKey() {
		return this.mKey;
	}

	public void setMKey(String key) {
		this.mKey = key;
	}

	public String getMComment() {
		return this.mComment;
	}

	public void setMComment(String comment) {
		this.mComment = comment;
	}
}
