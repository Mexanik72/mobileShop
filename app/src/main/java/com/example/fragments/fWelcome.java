package com.example.fragments;
 
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class fWelcome extends Fragment {

	View velcomeView;
	ImageView imageView;

	fAboutUs fAbout = null;
	fHelp fHelp = null;
	fCatalog fCatalog = null;
	class LogoutAsync extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			try {
				URL url = new URL(Connection.mainUrl + "/?r=authentication/logout&access_token="
						+ SharedPreferencesManager.getAccessToken());
				urlConnection = (HttpURLConnection) url.openConnection();
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				reader = new BufferedReader(new InputStreamReader(in));
				reader.readLine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				urlConnection.disconnect();
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return true;
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		velcomeView = inflater.inflate(R.layout.welcome_frg, container, false);
		final FragmentManager fm = getFragmentManager();

		imageView = (ImageView) velcomeView.findViewById(R.id.imageView1);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!Connection.isLogged) {
					Intent intent = new Intent(velcomeView.getContext(),
							LoginActivity.class);
					startActivity(intent);
				} else {
					LogoutAsync task = new LogoutAsync();
					task.execute();
					Connection.isLogged = false;
					updateImageView();
				}
			}
		});
		updateImageView();
		RelativeLayout ll = (RelativeLayout) velcomeView.findViewById(R.id.rWelcomLayout);
		ll.setOnTouchListener(new OnSwipeTouchListener(velcomeView.getContext()) {

			public void onSwipeTop() {
				fAbout = (fAboutUs) fm.findFragmentByTag("about");
				if(fAbout == null) {
					fAbout = new fAboutUs();
					fm.beginTransaction().add(R.id.frgmCont, fAbout, "about").addToBackStack("about").commit();
				}
			}

			public void onSwipeRight() {
				fHelp = (fHelp) fm.findFragmentByTag("help");
				if(fHelp == null) {
					fHelp = new fHelp();
					fm.beginTransaction().replace(R.id.frgmCont, fHelp, "help").addToBackStack("help").commit();
				}
			}

			public void onSwipeLeft() {
				fCatalog = (fCatalog) fm.findFragmentByTag("catalog");
				if(fCatalog == null) {
					fCatalog = new fCatalog();
					fm.beginTransaction().replace(R.id.frgmCont, fCatalog, "catalog").addToBackStack("catalog").commit();
				}
			}

			public void onSwipeBottom() {
			}

		});
		return velcomeView;

	}
	void updateImageView() {
		if (Connection.isLogged) {
			imageView.setImageResource(R.drawable.logout);
		} else {
			imageView.setImageResource(R.drawable.guest);
		}
	}
}