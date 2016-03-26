package com.example.fragments;
 
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class fWelcome extends Fragment {
	Fragment fcat;
	Fragment fabout;
	Fragment fhelp;
	FragmentTransaction frt;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View velcomeView = inflater.inflate(R.layout.welcome_frg, container, false);

		TextView tw = (TextView) velcomeView.findViewById(R.id.buttonB);
		tw.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(velcomeView.getContext(),
						LoginActivity.class);
				startActivity(intent);
			}
		});

		fcat = new fCatalog();
		fabout = new fAboutUs();
		fhelp = new fHelp();

		RelativeLayout ll = (RelativeLayout) velcomeView.findViewById(R.id.rWelcomLayout);
		ll.setOnTouchListener(new OnSwipeTouchListener(velcomeView.getContext()) {

			public void onSwipeTop() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, fabout).addToBackStack("about").commit();
			}

			public void onSwipeRight() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, fhelp).addToBackStack("help").commit();
			}

			public void onSwipeLeft() {
				getFragmentManager().beginTransaction().replace(R.id.frgmCont, fcat).addToBackStack("catalog").commit();
			}

			public void onSwipeBottom() {
			}

		});

		return velcomeView;

	}	
		
}