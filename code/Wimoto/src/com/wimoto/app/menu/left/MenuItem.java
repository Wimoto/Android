package com.wimoto.app.menu.left;

public class MenuItem {

	public static final String SEARCH = "search";
	public static final String SETTINGS = "settings";
	public static final String HELP = "help";

	
	private String mTitle;
	private String mTag;
	
	public MenuItem(String title) {
		this(title, "");
	}
	
	public MenuItem(String title, String tag) {
		mTitle = title;
		mTag = tag;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public String getTag() {
		return mTag;
	}

	
}
