package com.example.fragments;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapterSub extends BaseAdapter {

    private ArrayList dataList;
    private LayoutInflater layoutInflater;

    ListAdapterSub(Context context, ArrayList dataList) {
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
            convertView = layoutInflater.inflate(R.layout.list_item_sub, null);
            holder.name = (TextView) convertView.findViewById(R.id.sub_title);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        ListRow item = (ListRow) dataList.get(position);
        holder.name.setText(item.getTitle());
        return convertView;
    }

    static class ViewHolder
    {
        TextView name;
    }
}
