package com.cookandroid.registeration.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cookandroid.registeration.R;
import com.cookandroid.registeration.data.CourseItem;
import com.cookandroid.registeration.helper.network.DeleteRequest;
import com.cookandroid.registeration.ui.main.MainActivity;

import org.json.JSONObject;

import java.util.List;

public class StatisticsCourseListAdapter extends BaseAdapter {
    private Context context;
    private List<CourseItem> courseItemList;
    private Fragment parent;
    private String userID= MainActivity.userID;

    public StatisticsCourseListAdapter(Context context, List<CourseItem> courseItemList, Fragment parent) {
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
        View v=View.inflate(context, R.layout.item_statistics,null);
        TextView courseGrade=(TextView)v.findViewById(R.id.courseGrade);
        TextView courseTitle=(TextView)v.findViewById(R.id.courseTitle);
        TextView courseDivide=(TextView)v.findViewById(R.id.courseDivide);
        TextView coursePersonnel=(TextView)v.findViewById(R.id.coursePersonnel);
        TextView courseRate=(TextView)v.findViewById(R.id.courseRate);


        if(courseItemList.get(i).getCourseGrade().equals("제한 없음")|| courseItemList.get(i).getCourseGrade().equals(""))
        {
            courseGrade.setText("모든 학년");
        }
        else
        {
            courseGrade.setText(courseItemList.get(i).getCourseGrade()+"학년");
        }
        courseTitle.setText(courseItemList.get(i).getCourseTitle());
        courseDivide.setText(courseItemList.get(i).getCourseDivide()+"분반");

      /*  if(courseList.get(i).getCoursePersonnel()==0)
        {
            coursePersonnel.setText("인원 제한 없음");
            courseRate.setText("");
        }
        else
        {
            coursePersonnel.setText("신청 인원 : "+courseList.get(i).getCourseRival()+" / "+courseList.get(i).getCoursePersonnel());
            int rate=((int)(((double)courseList.get(i).getCourseRival()*100/courseList.get(i).getCoursePersonnel())+0.5));
            courseRate.setText("경쟁률 : "+rate+"%");
            if(rate<20){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorSafe));
            }
            else if(rate<=50){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorPrimary));
            }
            else if(rate<=100){
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorDanger));
            }
            else if(rate<=150)
            {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorWarning));
            }
            else
            {
                courseRate.setTextColor(parent.getResources().getColor(R.color.colorRed));
            }
        }*/

        v.setTag(courseItemList.get(i).getCourseID());

        Button deleteButton=(Button)v.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("강의가 삭제되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                              /*  StatisticFragment.totalCredit-=courseList.get(i).getCourseCredit();
                                StatisticFragment.credit.setText(StatisticFragment.totalCredit+"학점");*/
                                courseItemList.remove(i);
                                notifyDataSetChanged();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                AlertDialog dialog = builder.setMessage("강의 삭제에 실패했습니다.")
                                        .setNegativeButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                DeleteRequest deleteRequest = new DeleteRequest(userID, courseItemList.get(i).getCourseID() + "", responseListener);
                RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                queue.add(deleteRequest);
            }
        });

        return v;
    }
}
