package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

//    public User(JSONObject object){
//        try {
//            this.name = object.getString("name");
//            this.hometown = object.getString("hometown");
//       } catch (JSONException e) {
//            e.printStackTrace();
//       }
//    }

//    // Factory method to convert an array of JSON objects into a list of objects
//    // User.fromJson(jsonArray);
//    public static ArrayList<User> fromJson(JSONArray jsonObjects) {
//           ArrayList<User> users = new ArrayList<User>();
//           for (int i = 0; i < jsonObjects.length(); i++) {
//               try {
//                  users.add(new User(jsonObjects.getJSONObject(i)));
//               } catch (JSONException e) {
//                  e.printStackTrace();
//               }
//          }
//          return users;
//    }
//}
//
//class ItemAdapter extends ArrayAdapter<CatItem> 
//{
//    public ItemAdapter(Context context, ArrayList<CatItem> items) 
//    {
//       super(context, 0, items);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//       // Get the data item for this position
//       CatItem item = getItem(position);    
//       // Check if an existing view is being reused, otherwise inflate the view
//       if (convertView == null) {
//          convertView = LayoutInflater.from(getContext()).inflate(R.layout.catalog_item, parent, false);
//       }
//       // Lookup view for data population
//       TextView tvName = (TextView) convertView.findViewById(R.id.tv_id);
//       TextView tvHome = (TextView) convertView.findViewById(R.id.tv_name);
//       // Populate the data into the template view using the data object
//       tvName.setText(item.id);
//       tvHome.setText(item.name);
//       // Return the completed view to render on screen
//       return convertView;
//   }
//}

public class List extends ListFragment{

	  Communicator communicator;
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) 
	  {
		  super.onActivityCreated(savedInstanceState);
		  communicator = (Communicator) getActivity();
		  
//		  SimpleAdapter adapter = new SimpleAdapter(this, cd.Data(), android.R.layout.simple_list_item_1, 
//			        new String[] {"Name", "Tel"}, 
//			        new int[] {android.R.id.text1, android.R.id.text2});
//		    
//		  setListAdapter(adapter);
		  
		  return inflater.inflate(R.layout.fragment1, null);
	  }
	  
	  @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        communicator.onListItemClick(position, "catalog");
	    }

	    public interface Communicator{
	        public void onListItemClick(int position, String type);
	    }
}

