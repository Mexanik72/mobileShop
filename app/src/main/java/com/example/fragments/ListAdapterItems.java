package com.example.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapterItems  extends BaseAdapter {

    private ArrayList dataList;
    private LayoutInflater layoutInflater;

    ListAdapterItems(Context context, ArrayList dataList) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.list_items, null);
            holder.name = (TextView) convertView.findViewById(R.id.titleItems);
            holder.image = (ImageView) convertView.findViewById(R.id.iconItems);
            holder.price = (TextView) convertView.findViewById(R.id.priceItems);
            holder.availability = (TextView) convertView.findViewById(R.id.availabilityItems);
            // Get reference to carousel container
            holder.mCarouselContainer = (LinearLayout) convertView.findViewById(R.id.carousel);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }


        ListRowItem item = (ListRowItem) dataList.get(position);
        holder.name.setText(item.getTitle());
        if(item.getIcon().equals(null)) {
            Bitmap icon = BitmapFactory.decodeResource(convertView.getResources(),
                    R.drawable.ic_launcher);
            holder.image.setImageBitmap(icon);
        } else {
            holder.image.setImageBitmap(item.getIcon());
        }
        holder.price.setText("Price: " + item.getPrice()+"");
        holder.availability.setText(item.getAvailability());

        // Compute the width of a carousel item based on the screen width and number of initial items.
        //final DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int imageWidth = 150;//(int) (displayMetrics.widthPixels / INITIAL_ITEMS_COUNT);

        // Get the array of puppy resources
        //final TypedArray puppyResourcesTypedArray = getResources().obtainTypedArray(R.array.puppies_array);

        // Populate the carousel with items
        ImageView imageItem;
        TextView text;
        // Create new ImageView
        imageItem = new ImageView(convertView.getContext());
        // Set the shadow background
        imageItem.setImageBitmap(((ListRowItem) dataList.get(position)).getIcon());
        // Set the size of the image view to the previously computed value
        imageItem.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));
        // text = new TextView(this);
        // text.setText("hello");
        /// Add image view to the carousel container
        holder.mCarouselContainer.addView(imageItem);

        return convertView;
    }

    static class ViewHolder
    {
        TextView name;
        ImageView image;
        TextView price;
        TextView availability;
        private LinearLayout mCarouselContainer;
    }
}
