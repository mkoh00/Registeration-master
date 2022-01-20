package com.cookandroid.registeration.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cookandroid.registeration.R;

import java.util.ArrayList;

public class FloorListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Integer> list;

    public FloorListAdapter(Context context, ArrayList<Integer> floorItemList) {
        this.context = context;
        this.list = floorItemList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setData(ArrayList<Integer> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        View v=View.inflate(context, R.layout.item_floor,null);
        TextView floorText=(TextView)v.findViewById(R.id.floorText);
        floorText.setText(list.get(i)+"층");
        v.setTag(list.get(i));
        return v;
    }
}
