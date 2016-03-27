package com.example.fragments;

import android.content.Context;

public class ConfigManager {
	private static Context context = null;

	public static Context getApplicationContext() {
		return context;
	}

	public static void setApplicationContext(Context context) {
		ConfigManager.context = context;
	}
}