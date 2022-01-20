package com.cookandroid.registeration.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cookandroid.registeration.data.NoticeItem;
import com.cookandroid.registeration.R;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeItem> noticeItemList;

    public NoticeListAdapter(Context context, List<NoticeItem> noticeItemList) {
        this.context = context;
        this.noticeItemList = noticeItemList;
    }

    @Override
    public int getCount() {
        return noticeItemList.size();

    }

    @Override
    public Object getItem(int i) {
        return noticeItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v=View.inflate(context, R.layout.item_notice,null);
        TextView noticeText=(TextView)v.findViewById(R.id.noticeText);
        TextView nameText=(TextView)v.findViewById(R.id.nameText);
        TextView dateText=(TextView)v.findViewById(R.id.dateText);

        noticeText.setText(noticeItemList.get(i).getNotice());
        nameText.setText(noticeItemList.get(i).getNotice());
        dateText.setText(noticeItemList.get(i).getNotice());

        v.setTag(noticeItemList.get(i).getNotice());
        return v;
    }
}
