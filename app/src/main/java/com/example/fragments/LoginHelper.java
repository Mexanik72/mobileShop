package com.example.fragments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

public class LoginHelper {
	
	public static boolean isGuest(Context context) throws Exception {
		URL url = new URL(Connection.mainUrl + "/?r=authentication/isGuest&access_token="
				+ SharedPreferencesManager.getAccessToken());
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line = reader.readLine();
	        reader.close();
	        return line.equals("true");
		} finally {
			urlConnection.disconnect();
		}
	}
}
