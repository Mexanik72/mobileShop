package com.example.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_UNITS = "inits";

    JSONArray data = null;
    ArrayList<ListRowItem> dataList;
    ListView lv;
    String url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=items/getItems";
    String url_item = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=items/getItem";
    String pic_url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/resources/items/";

    public TextView js;
    int id;
    int idItem;
    LinearLayout mCarouselContainer;
    LinearLayout item;
    int layoutWidth;
    int layoutHeight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setRetainInstance(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        layoutWidth = displayMetrics.widthPixels;
        layoutHeight = displayMetrics.heightPixels / 2;

        dataList = new ArrayList<ListRowItem>();
        final View view = inflater.inflate(R.layout.items_frg, container, false);
        js = (TextView) view.findViewById(R.id.items_view);
        //lv = (ListView) view.findViewById(R.id.data_list_items);
        mCarouselContainer = (LinearLayout) view.findViewById(R.id.carousel);

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
        item = (LinearLayout) view.findViewById(R.id.item);
        item.setLayoutParams(new RelativeLayout.LayoutParams(layoutWidth, layoutHeight));

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
            showCarousel();
            //lv.setAdapter(new ListAdapterItems(getActivity(), dataList));
        }
    }
    private class GetDataItem extends AsyncTask<String, Void, Void> {

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
            String jsonStr = sh.makeServiceCall(url_item+"&id="+idItem, ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    data = jsonObj.getJSONArray(TAG_DATA);
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        String description = c.getString(TAG_DESCRIPTION);
                        String units = c.getString(TAG_UNITS);
                        //TextView.setText(String.valueOF(description) присвоение данных на нижнюю панель
                        Toast.makeText(getActivity().getBaseContext(), description + " " + units, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }



        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            showCarousel();
            //lv.setAdapter(new ListAdapterItems(getActivity(), dataList));
        }
    }
    private void showCarousel() {

        // Get the array of puppy resources
        //final TypedArray puppyResourcesTypedArray = getResources().obtainTypedArray(R.array.puppies_array);

        Toast.makeText(getActivity().getBaseContext(), "hoh"+dataList.size(), Toast.LENGTH_LONG).show();
        for(int i=0;i<dataList.size();i++) {
            // Populate the carousel with items
            ImageView imageItem;
            TextView text;
            TextView price;
            TextView availability;
            RelativeLayout rl = new RelativeLayout(getActivity().getBaseContext());
            rl.setLayoutParams(new RelativeLayout.LayoutParams(layoutWidth, layoutHeight));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new GetDataItem().execute();
                }
            });
            imageItem = new ImageView(getActivity().getBaseContext());
            imageItem.setImageBitmap(dataList.get(i).getIcon());
            imageItem.setLayoutParams(new LinearLayout.LayoutParams(layoutWidth - layoutWidth / 8, layoutWidth - layoutWidth / 8));

            text = new TextView(getActivity().getBaseContext());
            text.setText(String.valueOf(dataList.get(i).getTitle()));

            price = new TextView(getActivity().getBaseContext());

            price.setText(String.valueOf(dataList.get(i).getPrice()));

            availability = new TextView(getActivity().getBaseContext());
            availability.setText(String.valueOf(dataList.get(i).getAvailability()));

            rl.addView(imageItem);
            rl.addView(text);
            rl.addView(price);
            rl.addView(availability);
            mCarouselContainer.addView(rl);
        }
    }
}