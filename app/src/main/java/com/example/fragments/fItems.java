package com.example.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class fItems extends Fragment {

    private ProgressDialog pDialog;

    private static final String TAG_DATA = "data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_IMAGE = "image_url";
    private static final String TAG_PRICE = "price";
    private static final String TAG_AVAILABILITY = "availability";

    JSONArray data = null;
    ArrayList<ListRowItem> dataList;
    ListView lv;
    String url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=items/getItems";
    String pic_url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/resources/items/";

    public TextView js;
    int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataList = new ArrayList<ListRowItem>();
        View view = inflater.inflate(R.layout.items_frg, container, false);
        js = (TextView) view.findViewById(R.id.items_view);
        lv = (ListView) view.findViewById(R.id.data_list_items);
        ImageButton ib = (ImageButton) view.findViewById(R.id.imageButtonItems);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction();
                getFragmentManager().popBackStack();
                getFragmentManager().executePendingTransactions();
            }
        });
        id = getArguments().getInt("id");
        js.setText(getArguments().getString("subCategory"));
        new GetData().execute();
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
            String jsonStr = sh.makeServiceCall(url+"&id="+id, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    data = jsonObj.getJSONArray(TAG_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String id = c.getString(TAG_ID);
                        String name = c.getString(TAG_NAME);
                        String image = c.getString(TAG_IMAGE);
                        String price = "0";
                        if(!c.getString(TAG_PRICE).isEmpty()) {
                            c.getString(TAG_PRICE);
                        }
                        String availability = c.getString(TAG_AVAILABILITY);
                        String urldisplay = pic_url+image;

                        InputStream in = new java.net.URL(urldisplay).openStream();
                        Bitmap icon = BitmapFactory.decodeStream(in);
                        ListRowItem row = new ListRowItem(Integer.parseInt(id), name, icon, Integer.parseInt(price), availability);

                        dataList.add(row);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
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
            lv.setAdapter(new ListAdapterItems(getActivity(), dataList));
        }
    }
}