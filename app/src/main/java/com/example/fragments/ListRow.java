package com.example.fragments;

import android.graphics.Bitmap;

public class ListRow {
	
	    private String title;
	    private Bitmap icon;

	    public ListRow (String title, Bitmap icon) {
	        this.title = title;
	        this.icon = icon;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public Bitmap getIcon() {
	        return icon;
	    }

	    public void setIcon(Bitmap icon) {
	        this.icon = icon;
	    }
}
