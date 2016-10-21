package com.mitu.carrecorder.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mitu.carrecorder.R;
import com.mitu.carrecorder.view.wheelwidget.views.AbstractWheelTextAdapter;

import java.util.ArrayList;

/**
 * 说明：
 * 2016/5/8 0008
 */

public class ArriveTimeWheelViewAdapter extends AbstractWheelTextAdapter {

    ArrayList<String> list;

    public ArriveTimeWheelViewAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
        super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
        this.list = list;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index) + "";
    }





}
