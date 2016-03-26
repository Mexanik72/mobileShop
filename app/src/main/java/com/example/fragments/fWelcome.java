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


		RelativeLayout ll = (RelativeLayout) velcomeView.findViewById(R.id.rWelcomLayout);
		ll.setOnTouchListener(new OnSwipeTouchListener(velcomeView.getContext()) {

			public void onSwipeTop() {
				frt = getFragmentManager().beginTransaction();
				frt.replace(R.id.frgmCont, fabout);
				frt.commit();
			}

			public void onSwipeRight() {
				frt = getFragmentManager().beginTransaction();
				frt.replace(R.id.frgmCont, fhelp);
				frt.commit();
			}

			public void onSwipeLeft() {
				frt = getFragmentManager().beginTransaction();
				frt.replace(R.id.frgmCont, fcat);
				frt.commit();
			}

			public void onSwipeBottom() {
			}

		});

		fcat = new fCatalog();
		fabout = new fAboutUs();
		fhelp = new fHelp();

		return velcomeView;

	}	
		
}