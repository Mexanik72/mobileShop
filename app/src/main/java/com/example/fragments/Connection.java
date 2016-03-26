package com.example.fragments;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class Connection {
	
	private final static String sPrefSessId = "PHPSESSID";
	private static Context context = null;
	protected static HttpURLConnection conn = null;
	public static final String mainUrl = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com";
	public static boolean isLogged = false;
	
	public Connection(Context context) {
		Connection.context = context;
	}
	
	public HttpURLConnection getConnection(String urlString) throws Exception {
		URL url = new URL(urlString);
		Connection.conn = (HttpURLConnection) url
				.openConnection();
		SharedPreferences sPref;
        sPref = context.getSharedPreferences(sPrefSessId, Context.MODE_PRIVATE);
		if (sPref.contains(sPrefSessId)) {
			setPhpSessIdToConnection(getPhpSessIdFromSharedPref(sPref));
		}
		return Connection.conn;
	}
	
	public void setConnection(HttpURLConnection conn) {
		Connection.conn = conn;
		savePhpSessIdToSharedPref(getPhpSessIdFromConnection());
	}
	
	protected static String getPhpSessIdFromConnection() {
		final String COOKIES_HEADER = "Set-Cookie";
		java.net.CookieManager msCookieManager = new java.net.CookieManager();
		Map<String, List<String>> headerFields = conn.getHeaderFields();
		List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

		if (cookiesHeader != null) {
			for (String cookie : cookiesHeader) {
				msCookieManager.getCookieStore().add(null,
						HttpCookie.parse(cookie).get(0));
			}
		}
		List<HttpCookie> cookies = msCookieManager.getCookieStore()
				.getCookies();
		for (HttpCookie cookie : cookies) {
			return cookie.getValue();
		}
		return null;
	}
	
	protected static void setPhpSessIdToConnection(String sessId) {
		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);
		conn.setRequestProperty("Cookie", "PHPSESSID=" + sessId);
	}
	
	protected static String getPhpSessIdFromSharedPref(SharedPreferences sPref) {
		return sPref.getString(sPrefSessId, null);
	}
	
	protected static void savePhpSessIdToSharedPref(String sessId) {
		SharedPreferences sPref;
        sPref = context.getSharedPreferences(sPrefSessId, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(sPrefSessId, sessId);
        ed.apply();
	}
}
