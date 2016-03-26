package com.example.fragments;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.content.Context;

public class LoginHelper {
	
	public static boolean isGuest(Context context) throws Exception {
		Connection conn = new Connection(context);
		HttpURLConnection urlConnection = conn.getConnection(Connection.mainUrl + "/?r=authentication/isGuest");
		try {
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line = reader.readLine();
	        reader.close();
	        return line.equals("true") ? true : false;
		} finally {
			urlConnection.disconnect();
		}
	}
}
