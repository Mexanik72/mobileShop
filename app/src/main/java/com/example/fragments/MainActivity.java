package com.example.fragments;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.content.res.Configuration;
import android.widget.Toast;

public class MainActivity extends Activity {

	private boolean switcher = true;

	class IsGuestAsync extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return LoginHelper.isGuest(getApplicationContext());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean("state", switcher);
		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConfigManager.setApplicationContext(getApplicationContext());
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		if (savedInstanceState != null) {
			switcher = savedInstanceState.getBoolean("state");
		} else {
			getFragmentManager().beginTransaction().replace(R.id.frgmCont, new fWelcome()).commit();
		}

		IsGuestAsync task = new IsGuestAsync();
		task.execute();
		try {
			Connection.isLogged = !task.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Connection.isLogged)
			getFragmentManager().beginTransaction().replace(R.id.frgmCont, new fWelcome()).commit();
	}
}
