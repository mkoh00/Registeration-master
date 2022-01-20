package com.cookandroid.registeration.helper.network;

import android.os.AsyncTask;

import com.cookandroid.registeration.data.NoticeItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetAsyncTaskHelper {
    private final String BASE_URL="https://jaohoo.cafe24.com";
    private AsyncTask task;
    public static final int GET_CLASSROOM_LIST = 121;
    public static final int GET_NOTICE_LIST = 122;
    GetAsyncTaskHelper instance;
    private int type;

    public GetAsyncTaskHelper getInstance(){
        if(instance == null){
            synchronized (this){
                if(instance == null){
                    instance = new GetAsyncTaskHelper();
                }
            }
        }
        return instance;
    }

    public GetAsyncTaskHelper() {
    }

    public void setType(int type) {
        this.type = type;
    }

    public GetAsyncTaskHelper(int type){
        this.type = type;
        getInstance();
    }

    public class ClassroomListAsyncTask extends AsyncTask<Void, Void, Integer>{
        @Override
        protected Integer doInBackground(Void... voids) {
            return null;
        }
    }

    public class NoticeListTask extends AsyncTask<Void, Void, ArrayList<NoticeItem>> {
        @Override
        protected ArrayList<NoticeItem> doInBackground(Void... voids) {
            try{
                URL url = new URL(BASE_URL+"/NoticeList.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();

                while((temp=bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp+"\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                ArrayList<NoticeItem> list = new ArrayList<>();

                try{
                    String result = stringBuilder.toString().trim();
                    JSONObject jsonObject=new JSONObject(result);
                    JSONArray jsonArray=jsonObject.getJSONArray("response");
                    int count=0;
                    String noticeContent, noticeName, noticeDate;
                    while(count < jsonArray.length()){
                        JSONObject object=jsonArray.getJSONObject(count);
                        noticeContent=object.getString("noticeContent");
                        noticeName=object.getString("noticeName");
                        noticeDate=object.getString("noticeDate");
                        NoticeItem noticeItem =new NoticeItem(noticeContent,noticeName, noticeDate);
                        list.add(noticeItem);
                        count++;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                return list;
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}