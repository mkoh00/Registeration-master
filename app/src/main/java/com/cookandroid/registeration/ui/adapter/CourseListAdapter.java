package com.cookandroid.registeration.ui.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cookandroid.registeration.helper.network.AddRequest;
import com.cookandroid.registeration.data.CourseItem;
import com.cookandroid.registeration.R;
import com.cookandroid.registeration.data.Schedule;
import com.cookandroid.registeration.ui.main.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends BaseAdapter {
    private Context context;
    private List<CourseItem> courseItemList;
    private Fragment parent;
    private String userID= MainActivity.userID;
    private Schedule schedule= new Schedule();
    private List<Integer> courseIDList;
    private static int totalCredit=0;

    public CourseListAdapter(Context context, List<CourseItem> courseItemList, Fragment parent) {
        this.context = context;
        this.courseItemList = courseItemList;
        this.parent=parent;
        schedule=new Schedule();
        courseIDList=new ArrayList<Integer>();
        new BackgroundTask().execute();
        totalCredit=0;
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
        View v=View.inflate(context, R.layout.iteem_course,null);
        TextView courseGrade=(TextView)v.findViewById(R.id.courseGrade);
        TextView courseTitle=(TextView)v.findViewById(R.id.courseTitle);
        TextView courseCredit=(TextView)v.findViewById(R.id.courseCredit);
        TextView courseDivide=(TextView)v.findViewById(R.id.courseDivide);
        TextView courseProfessor=(TextView)v.findViewById(R.id.courseProfessor);
        TextView courseTime=(TextView)v.findViewById(R.id.courseTime);

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
        else {
            courseProfessor.setText(courseItemList.get(i).getCourseProfessor() + "교수");
        }

        courseTime.setText(courseItemList.get(i).getCourseTime()+"");


        v.setTag(courseItemList.get(i).getCourseID());

        Button addButton=(Button)v.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                boolean validate = false;
                validate = schedule.validate(courseItemList.get(i).getCourseTime());
                if (!already(courseIDList, courseItemList.get(i).getCourseID())) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog = builder.setMessage("이미 추가한 강의입니다.")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else if (totalCredit+ courseItemList.get(i).getCourseCredit()>21)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog = builder.setMessage("21학점을 초과 할 수 없습니다.")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else if (validate == false)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                    AlertDialog dialog = builder.setMessage("시간표가 중복됩니다.")
                            .setPositiveButton("다시 시도", null)
                            .create();
                    dialog.show();
                }
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("강의가 추가되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    courseIDList.add(courseItemList.get(i).getCourseID());
                                    schedule.addSchedule(courseItemList.get(i).getCourseTime());
                                    totalCredit+= courseItemList.get(i).getCourseCredit();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(parent.getActivity());
                                    AlertDialog dialog = builder.setMessage("강의 추가에 실패했습니다.")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    AddRequest validateRequest = new AddRequest(userID, courseItemList.get(i).getCourseID() + "", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(parent.getActivity());
                    queue.add(validateRequest);
                }
            }
        });

        return v;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            try {
                target = "https://jaohoo.cafe24.com/ScheduleList.php?userID=" + URLEncoder.encode(userID, "UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url=new URL(target);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder=new StringBuilder();
                while((temp=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(temp+"\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result)
        {
            try{
                totalCredit=0;
                JSONObject jsonObject=new JSONObject(result);
                JSONArray jsonArray=jsonObject.getJSONArray("response");
                int count=0;
                String courseProfessor, courseTime;
                int courseID;
                totalCredit=0;
                while(count<jsonArray.length()){
                    JSONObject object=jsonArray.getJSONObject(count);
                    courseID=object.getInt("courseID");
                    courseProfessor=object.getString("courseProfessor");
                    courseTime=object.getString("courseTime");
                    totalCredit+=object.getInt("courseCredit");
                    courseIDList.add(courseID);
                    schedule.addSchedule(courseTime);
                    count++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public boolean already(List<Integer>courseIDList, int item)
    {
        for(int i=0; i<courseIDList.size(); i++)
        {
            if(courseIDList.get(i)==item){
                return false;
            }
        }

        return true;
    }
}
