package com.cookandroid.registeration.ui.main.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.registeration.R;
import com.cookandroid.registeration.User;
import com.cookandroid.registeration.data.EmptyRoomItem;
import com.cookandroid.registeration.ui.adapter.ReservedRoomAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReservedListFragment extends Fragment {
    private ArrayList<EmptyRoomItem> reservedRoomList;
    private ReservedRoomAdapter adapter = new ReservedRoomAdapter();
    private RecyclerView rcv;

    public ReservedListFragment() {
        // Required empty public constructor
    }

    public static ReservedListFragment newInstance(String param1, String param2) {
        ReservedListFragment fragment = new ReservedListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_reserved_list, container, false);
        rcv = mView.findViewById(R.id.rcv_reserved_room);

        new ReservedListTask().execute();
        return mView;
    }

    class ReservedListTask extends AsyncTask<Void, Void, String> {
        String target;

        ReservedListTask() {
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            target = "https://jaohoo.cafe24.com/ReservedRoomList.php";
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
                reservedRoomList = new Gson().fromJson(result, new TypeToken<ArrayList<EmptyRoomItem>>() {}.getType());

                // sort기준: 1. 내 거 2. 남 거
                Collections.sort(reservedRoomList, new Comparator<EmptyRoomItem>(){
                    @Override
                    public int compare(EmptyRoomItem o1, EmptyRoomItem o2) {
                        if(o1.getUid().equals(User.getInstance().getId())){
                            return -1;
                        }
                        return 0;
                    }
                });
                adapter.setData(reservedRoomList);
                Log.d("test", reservedRoomList.toString());
                rcv.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}