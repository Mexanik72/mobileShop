package com.example.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.ProgressDialog;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class fCatalog extends Fragment {

	 private ProgressDialog pDialog;
	 
	 private static final String TAG_DATA = "data";
	 private static final String TAG_ID = "id";
	 private static final String TAG_NAME = "name";
	 private static final String TAG_IMAGE = "image_url";
	 
	 JSONArray data = null;
	 ArrayList<ListRow> dataList;
	 ListView lv;
	 String url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/?r=categories/getCategories";
	 String pic_url = "http://ec2-54-218-6-169.us-west-2.compute.amazonaws.com/resources/categories/";
	 
	 public TextView js;
	 
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {   
	        dataList = new ArrayList<ListRow>();
	        View view = inflater.inflate(R.layout.catalog_frg, container, false);
	        js = (TextView) view.findViewById(R.id.ctlg);
	        lv = (ListView) view.findViewById(R.id.data_list);
	        ImageButton ib = (ImageButton) view.findViewById(R.id.imageButton);
	        ib.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
					getFragmentManager().beginTransaction();
					getFragmentManager().popBackStack();
					getFragmentManager().executePendingTransactions();
	            }
	        });
            new GetData().execute();
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					fSubCatalog fSubCat = new fSubCatalog();
					Bundle bundle = new Bundle();
					bundle.putInt("id", position);
					bundle.putString("category", dataList.get(position).getTitle());
					fSubCat.setArguments(bundle);
					getFragmentManager().beginTransaction().replace(R.id.frgmCont, fSubCat).addToBackStack("subcatalog").commit();
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
	            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
	  
	            if (jsonStr != null) {
	                try {
	                    JSONObject jsonObj = new JSONObject(jsonStr);
	                    data = jsonObj.getJSONArray(TAG_DATA);
	                    for (int i = 0; i < data.length(); i++) {
	                        JSONObject c = data.getJSONObject(i);
	                         
	                        String id = c.getString(TAG_ID);
	                        String name = c.getString(TAG_NAME);
	                        String image = c.getString(TAG_IMAGE);
	                        String urldisplay = pic_url+image;
	                        
	                        InputStream in = new java.net.URL(urldisplay).openStream();
	                        Bitmap icon = BitmapFactory.decodeStream(in);
	        	        	ListRow row = new ListRow(Integer.parseInt(id), name, icon);
	                        
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
	           lv.setAdapter(new ListAdapter(getActivity(), dataList));
	        }
	}
}


//
//	    @Override
//	    public void onActivityCreated(Bundle savedInstanceState) {
//
//	        super.onActivityCreated(savedInstanceState);
//      
//	        menutitles = new String[] {"1", "2", "3", "4"}; //getResources().getStringArray(R.array.titles);
//	       // String [] photos = new String [] {"pic_10", "pic_101"};
//	        menuIcons = getResources().obtainTypedArray(R.array.icons);
//	        
//	        rowItems = new ArrayList <ListRow>();
//
//	        for (int i = 0; i < menutitles.length; i++) {
//	            ListRow items = new ListRow (menutitles[i], menuIcons.getResourceId(
//	                    i, -1));
//
//	            rowItems.add(items);
//	        }
//
//	        adapter = new ListAdapter(getActivity(), rowItems);
//	        setListAdapter(adapter);
	      //  getListView().setOnItemClickListener(this);
//	    }
	