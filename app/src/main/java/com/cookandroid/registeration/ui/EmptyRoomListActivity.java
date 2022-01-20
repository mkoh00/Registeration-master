package com.cookandroid.registeration.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.registeration.R;
import com.cookandroid.registeration.data.EmptyRoomItem;
import com.cookandroid.registeration.ui.adapter.EmptyRoomAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static com.cookandroid.registeration.ui.main.fragment.FloorListFragment.PASS_BUILDING;
import static com.cookandroid.registeration.ui.main.fragment.FloorListFragment.PASS_DAY;
import static com.cookandroid.registeration.ui.main.fragment.FloorListFragment.PASS_FLOOR;

public class EmptyRoomListActivity extends AppCompatActivity {
    private ArrayList<EmptyRoomItem> emptyRoomItemArrayList;
    private EmptyRoomAdapter adapter = new EmptyRoomAdapter();
    private RecyclerView rcv;
    String building;
    int floor;
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_lecture_room);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        rcv = findViewById(R.id.rcv_reserved_room);
        building = getIntent().getStringExtra(PASS_BUILDING);
        floor = getIntent().getIntExtra(PASS_FLOOR, -1);
        today = getIntent().getStringExtra(PASS_DAY);

        Log.d("test", "building: "+building + ", floor: "+floor+ ", day: "+today);
        new BackgroundTask(building, floor, today).execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        String building;
        String today;
        int floor;

        BackgroundTask(String building, int floor, String today) {
            this.building = building;
            this.floor = floor;
            this.today = today;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            target = "https://jaohoo.cafe24.com/EmptyRoomList.php?name="+building+"&floor="+floor+"&day="+today;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                emptyRoomItemArrayList = new Gson().fromJson(result, new TypeToken<ArrayList<EmptyRoomItem>>() {}.getType());

                Log.d("test", "before: "+emptyRoomItemArrayList.toString());
                emptyRoomItemArrayList = findEmptyRooms(proceedsRoomList(emptyRoomItemArrayList));
                Log.d("test", "after: "+emptyRoomItemArrayList.toString());

                new ReservedListTask(building, floor).execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayList<EmptyRoomItem> findEmptyRooms(HashMap<String, ArrayList<Integer>> map){
            ArrayList<EmptyRoomItem> emptyList = new ArrayList<>();

            Iterator<String> it = map.keySet().iterator();
            while(it.hasNext()){
                String key = it.next();
                for(int i = 1 ; i < 13 ; i++){
                    if(!map.get(key).contains(i)){
                        emptyList.add(new EmptyRoomItem(true, null, null, building, floor, key, today, Integer.toString(i)));
                    }
                }
            }

            return emptyList;
        }

        /**
         * 같은 호실이 있으면 하나로 묶는다.
         * 타입에 따라 숫자를 넣는다. (항상은 time값이 없으므로 처리하지 않아도 된다.)
         * 주간: 1~8
         * 야간: 9~13
         * 항상: 없음
         * @param roomList
         * @return
         */
        HashMap<String, ArrayList<Integer>> proceedsRoomList(ArrayList<EmptyRoomItem> roomList){
            ArrayList<EmptyRoomItem> list = new ArrayList<EmptyRoomItem>();
            HashMap<String, ArrayList<Integer>> map = new HashMap<>();

            for(int i = 0 ; i < roomList.size() ; i++){
                String rn = roomList.get(i).getRoom_number();
                String type = roomList.get(i).getType();
                String[] arr = roomList.get(i).getTime().split(",");

                if(!map.containsKey(rn)){
                    map.put(rn, new ArrayList<Integer>());
                }

                switch (type){
                    case "주간":
                        for (String s : arr) {
                            map.get(rn).add(Integer.parseInt(s));
                        }
                        break;
                    case "야간":
                        for (String s : arr) {
                            map.get(rn).add(Integer.parseInt(s)+8);
                        }
                        break;
                }
            }

            return map;
        }
    }

    class ReservedListTask extends AsyncTask<Void, Void, String> {
        String target;
        String building;
        int floor;

        ReservedListTask(String building, int floor) {
            this.building = building;
            this.floor = floor;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            target = "https://jaohoo.cafe24.com/ReservedRoomList.php?name="+building+"&floor="+floor;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values) {
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result) {
            try {
                ArrayList<EmptyRoomItem> reservedList = new Gson().fromJson(result, new TypeToken<ArrayList<EmptyRoomItem>>() {}.getType());

                for(int i = 0 ; i < emptyRoomItemArrayList.size() ; i++){
                    EmptyRoomItem ei = emptyRoomItemArrayList.get(i);

                    for(int j = 0 ; j < reservedList.size() ; j++){
                        EmptyRoomItem ej = reservedList.get(j);
                        if(ei.getRoom_number().equals(ej.getRoom_number())
                        && ei.getTime().equals(ej.getTime())){
                            emptyRoomItemArrayList.get(i).setUid(ej.getUid());
                            emptyRoomItemArrayList.get(i).setAvailable(false);
                        }
                    }
                }

                // sort기준: 1. 시간, 2. 호실 > 같은 시간인 경우 호실이 크면 나중
                adapter.setData(emptyRoomItemArrayList);
                Log.d("test", emptyRoomItemArrayList.toString());
                rcv.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}