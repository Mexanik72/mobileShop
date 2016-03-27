package com.example.fragments;

import android.graphics.Bitmap;

/**
 * Created by Андрей on 27.03.2016.
 */
public class ListRowItem {
    private int id;
    private String title;
    private Bitmap icon;
    private int price;
    private String availability;

    public ListRowItem (int id, String title, Bitmap icon,int price,String availability) {
        this.id = id;
        this.title = title;
        this.icon = icon;
        this.price = price;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
