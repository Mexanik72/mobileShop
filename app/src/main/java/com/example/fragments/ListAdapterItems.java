package com.example.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        return convertView;
    }

    static class ViewHolder
    {
        TextView name;
        ImageView image;
        TextView price;
        TextView availability;
    }
}
