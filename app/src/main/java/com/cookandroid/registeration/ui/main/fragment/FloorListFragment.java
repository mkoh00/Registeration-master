package com.cookandroid.registeration.ui.main.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cookandroid.registeration.data.ClassroomItem;
import com.cookandroid.registeration.R;
import com.cookandroid.registeration.ui.EmptyRoomListActivity;
import com.cookandroid.registeration.ui.adapter.FloorListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FloorListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FloorListFragment extends Fragment {
    public static int TOTAL_CREDIT = 0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PASS_BUILDING = "pbidng";
    public static final String PASS_FLOOR = "pclass";
    public static final String PASS_DAY = "dyzdd";

    private ListView floorListView;
    private ListView courseListView;
    private  TextView creditTextView;
    private ListView rankListView;
    private Spinner buildingSpinner;

    private FloorListAdapter floorAdapter;
    private ArrayAdapter buildingAdapter;

    private ArrayList<Integer> floorItemList;
    private ArrayList<ClassroomItem> classroomList = new ArrayList<>();

    private String selectedBuilding;
    private String today;
    private String mParam1;
    private String mParama2;

    public FloorListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FloorListFragment newInstance(String param1, String param2) {
        FloorListFragment fragment = new FloorListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            selectedBuilding = getArguments().getString(PASS_BUILDING, "호천관");
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_floor_list, container, false);
        buildingSpinner = mView.findViewById(R.id.buildSpinner);
        floorListView = mView.findViewById(R.id.floorListView);

        floorItemList = new ArrayList<Integer>();
        buildingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.builName, R.layout.item_spinner);
        buildingSpinner.setAdapter(buildingAdapter);
        buildingSpinner.setPopupBackgroundResource(R.color.colorPrimary);

        floorAdapter = new FloorListAdapter(getContext(), floorItemList);
        floorListView.setAdapter(floorAdapter);
        buildingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBuilding = (String) buildingSpinner.getSelectedItem();
                BackgroundTask task = new BackgroundTask(selectedBuilding);
                task.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        floorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), EmptyRoomListActivity.class);
                intent.putExtra(PASS_BUILDING, selectedBuilding);
                intent.putExtra(PASS_FLOOR, floorItemList.get(i));
                intent.putExtra(PASS_DAY, today);
                startActivity(intent);
            }
        });

        buildingSpinner.setSelection(buildingAdapter.getPosition(selectedBuilding));

        return mView;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;
        String name;

        BackgroundTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            Calendar cal = Calendar.getInstance();
            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    today = "일";
                    break;
                case 2:
                    today = "월";
                    break;
                case 3:
                    today = "화";
                    break;
                case 4:
                    today = "수";
                    break;
                case 5:
                    today = "목";
                    break;
                case 6:
                    today = "금";
                    break;
                case 7:
                    today = "토";
                    break;
                default:
                    today = "";
                    break;
            }
            target = "https://jaohoo.cafe24.com/ClassroomList.php?name="+name+"&day="+today;
            Log.d("test", target);
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
            Log.d("test", result);
            try {
                classroomList = new Gson().fromJson(result, new TypeToken<ArrayList<ClassroomItem>>() {}.getType());
                floorItemList.clear();

                for (int i = 0; i < classroomList.size(); i++) {
                    String roomNum = classroomList.get(i).getRoom_number();
                    try{
                        Integer floor = Integer.parseInt(roomNum.substring(0, roomNum.length() - 2));
                        if (!floorItemList.contains(floor)) {
                            floorItemList.add(floor);
                        }
                    }catch (Exception e){

                    }
                }

                Collections.sort(floorItemList, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer f1, Integer f2) {
                        return f1.compareTo(f2);
                    }
                });

                Log.d("test", classroomList.toString());
                floorAdapter.setData(floorItemList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}