package com.example.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fSubCatalog extends Fragment {

    private ProgressDialog pDialog;

    private static final String TAG_DATA = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";

    JSONArray data = null;
    ArrayList<ListRow> dataList;
    ListView lv;
    String url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=subcategories/getCategories";

    int id;
    public TextView js;
    Bitmap icon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setRetainInstance(true);
        dataList = new ArrayList<ListRow>();
        final View view = inflater.inflate(R.layout.catalog_sub_frg, container, false);
        js = (TextView) view.findViewById(R.id.catalog_sub_view);
        lv = (ListView) view.findViewById(R.id.catalog_sub_list);
        ImageButton ib = (ImageButton) view.findViewById(R.id.catalog_sub_imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction();
                getFragmentManager().popBackStack();
                getFragmentManager().executePendingTransactions();
            }
        });
        id = getArguments().getInt("id");
        js.setText(getArguments().getString("category"));
        icon =(Bitmap)getArguments().getParcelable("icon");

        ImageButton user = (ImageButton) view.findViewById(R.id.userButton);
        if (Connection.isLogged) {
            user.setImageResource(R.drawable.user);
        } else {
            user.setImageResource(R.drawable.guest);
        }
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connection.isLogged) {
                    Toast.makeText(view.getContext(), "User page", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(view.getContext(),
                            LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        new GetData().execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fItems fItm = new fItems();
                Bundle bundle = new Bundle();
                bundle.putInt("id", dataList.get(position).getId());
                bundle.putString("subCategory", dataList.get(position).getTitle());
                fItm.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frgmCont, fItm).addToBackStack("subCategory").commit();
            }
        });
        return view;
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading fresh products:)");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url+"&id="+getArguments().getInt("id"), ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    data = jsonObj.getJSONArray(TAG_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        ListRow row = new ListRow(Integer.parseInt(id), name, icon);

                        dataList.add(row);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            lv.setAdapter(new ListAdapterSub(getActivity(), dataList));
        }
    }
}
