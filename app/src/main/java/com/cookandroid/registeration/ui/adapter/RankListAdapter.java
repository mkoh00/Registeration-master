package com.cookandroid.registeration.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cookandroid.registeration.R;
import com.cookandroid.registeration.data.CourseItem;

import java.util.List;

public class RankListAdapter extends BaseAdapter {
    private Context context;
    private List<CourseItem> courseItemList;
    private Fragment parent;

    public RankListAdapter(Context context, List<CourseItem> courseItemList, Fragment parent) {
        this.context = context;
        this.courseItemList = courseItemList;
        this.parent=parent;
        //new BackgroundTask().execute();
    }

    @Override
    public int getCount() {
        return courseItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return courseItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View v=View.inflate(context, R.layout.item_rank,null);
        TextView rankTextView=(TextView)v.findViewById(R.id.rankTextView);
        TextView courseGrade=(TextView)v.findViewById(R.id.courseGrade);
        TextView courseTitle=(TextView)v.findViewById(R.id.courseTitle);
        TextView courseCredit=(TextView)v.findViewById(R.id.courseCredit);
        TextView courseDivide=(TextView)v.findViewById(R.id.courseDivide);
        TextView coursePersonnel=(TextView)v.findViewById(R.id.coursePersonnel);
        TextView courseProfessor=(TextView)v.findViewById(R.id.courseProfessor);
        TextView courseTime=(TextView)v.findViewById(R.id.courseTime);

        rankTextView.setText((i+1)+"위");
        if(i!=0)
        {
            rankTextView.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimary));
        }
        if(courseItemList.get(i).getCourseGrade().equals("제한 없음")|| courseItemList.get(i).getCourseGrade().equals(""))
        {
            courseGrade.setText("모든 학년");
        }
        else
        {
            courseGrade.setText(courseItemList.get(i).getCourseGrade()+"학년");
        }
        courseTitle.setText(courseItemList.get(i).getCourseTitle());
        courseCredit.setText(courseItemList.get(i).getCourseCredit()+"학점");
        courseDivide.setText(courseItemList.get(i).getCourseDivide()+"분반");


        if(courseItemList.get(i).getCourseProfessor().equals(""))
        {
            courseProfessor.setText("개인 연구");
        }
        else
        {
            courseProfessor.setText(courseItemList.get(i).getCourseProfessor()+"교수님");
        }

        courseTime.setText(courseItemList.get(i).getCourseTime()+"");
        v.setTag(courseItemList.get(i).getCourseID());

        return v;
    }
}
