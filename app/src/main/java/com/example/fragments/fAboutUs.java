package com.example.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class fAboutUs extends Fragment {

    FragmentTransaction frt;
    Fragment fWelcome;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_frg, container, false);
        TextView text_about = (TextView)view.findViewById(R.id.textView1);
        text_about.setText("\n'Om-Nom-Nom' is one of the fastest growing retail companies. The company is committed to creating the best supermarkets in the world." +
                "\n\nIf you have any questions, comments or suggestions to us, you can contact us" +
                "\n\nphone number: 8-987-65-43" +
                "\ne-mail: om_nom_nom@mail.ru" +
                "\n\nWe are always happy to communicate with potential customers and business partners!");

        ImageButton ib = (ImageButton) view.findViewById(R.id.aboutImageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frt = getActivity().getFragmentManager().beginTransaction();
                frt.replace(R.id.frgmCont, fWelcome);
                frt.commit();
            }
        });
        fWelcome = new fWelcome();

        return view;
    }
    public void setText() {

    }
}