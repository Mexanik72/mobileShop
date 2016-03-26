package com.example.fragments;
 
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class fWelcome extends Fragment {

	View velcomeView;

	class LogoutAsync extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			Connection conn = new Connection(getActivity().getApplicationContext());
			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			try {
				urlConnection = conn.getConnection(Connection.mainUrl
						+ "/?r=authentication/logout");
				InputStream in = new BufferedInputStream(
						urlConnection.getInputStream());
				reader = new BufferedReader(new InputStreamReader(in));
				reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

		TextView tw = (TextView) velcomeView.findViewById(R.id.buttonB);
		tw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!Connection.isLogged) {
					Intent intent = new Intent(velcomeView.getContext(),
							LoginActivity.class);
					startActivity(intent);
				} else {
					LogoutAsync task = new LogoutAsync();
					task.execute();
					Connection.isLogged = false;
				}
			}
		});
		RelativeLayout ll = (RelativeLayout) velcomeView.findViewById(R.id.rWelcomLayout);
		ll.setOnTouchListener(new OnSwipeTouchListener(velcomeView.getContext()) {

			public void onSwipeTop() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, new fAboutUs()).addToBackStack("about").commit();
			}

			public void onSwipeRight() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, new fHelp()).addToBackStack("help").commit();
			}

			public void onSwipeLeft() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, new fCatalog()).addToBackStack("catalog").commit();
			}

			public void onSwipeBottom() {
			}

		});

		return velcomeView;

	}
	void update() {
		TextView tw = (TextView) velcomeView.findViewById(R.id.buttonB);
		if (!Connection.isLogged) {
			tw.setText("Log in");
		} else {
			tw.setText("Log out");
		}
	}
		
}