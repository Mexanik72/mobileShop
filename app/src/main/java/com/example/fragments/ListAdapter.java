package com.example.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	private ArrayList dataList;
	private LayoutInflater layoutInflater;

    ListAdapter(Context context, ArrayList dataList) {
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
        	convertView = layoutInflater.inflate(R.layout.list_item, null);
        	holder.name = (TextView) convertView.findViewById(R.id.title);
        	holder.image = (ImageView) convertView.findViewById(R.id.icon);
        	convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListRow item = (ListRow) dataList.get(position);
        holder.name.setText(item.getTitle());
        if(item.getIcon().equals(null)) {
        	Bitmap icon = BitmapFactory.decodeResource(convertView.getResources(),
                    R.drawable.ic_launcher);
        	holder.image.setImageBitmap(icon);
        } else {
        	holder.image.setImageBitmap(item.getIcon());
        }
        return convertView;
    }
    
    static class ViewHolder
    {
    	TextView name;
    	ImageView image;
    }
}
