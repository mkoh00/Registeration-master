package com.cookandroid.registeration.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.cookandroid.registeration.User;
import com.cookandroid.registeration.data.NoticeItem;
import com.cookandroid.registeration.helper.network.DeleteRequest;
import com.cookandroid.registeration.helper.network.DeleteUserRequest;
import com.cookandroid.registeration.ui.GoogleMapActivity;
import com.cookandroid.registeration.ui.main.fragment.CourseFragment;
import com.cookandroid.registeration.ui.main.fragment.FloorListFragment;
import com.cookandroid.registeration.ui.LoginActivity;
import com.cookandroid.registeration.ui.main.fragment.ReservedListFragment;
import com.cookandroid.registeration.ui.main.fragment.ScheduleFragment;
import com.cookandroid.registeration.ui.adapter.NoticeListAdapter;
import com.cookandroid.registeration.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.cookandroid.registeration.ui.main.fragment.FloorListFragment.PASS_BUILDING;

public class MainActivity extends AppCompatActivity {
    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<NoticeItem> noticeItemList;
    private LinearLayout noticeLayout;
    public static String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        // 필요 시 uncomment
//        showNoticeList();

        userID = getIntent().getStringExtra("userID");

        hideNoticeView();

        String building = getIntent().getStringExtra(PASS_BUILDING);

        if (building != null) {
            goToEmptyFragment(building);
        } else {
            goToReservedListFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.lectureListOption) {
            goToCourseFragment();
        } else if (item.getItemId() == R.id.timeTableOption) {
            goToScheduleFragment();
        } else if (item.getItemId() == R.id.emptyLectureFindOption) {
            goToEmptyFragment(null);
        } else if (item.getItemId() == R.id.fastFindOption) {
            goToFastFindActivity();
        } else if (item.getItemId() == R.id.HomeOption) {
            goToMainActivity();
        } else if (item.getItemId() == R.id.logoutOption) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        }else if (item.getItemId() == R.id.removeUser) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("회원탈퇴").setMessage("탈퇴 하시겠습니까?")
                    .setPositiveButton("회원탈퇴", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            removeUser();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();



        }
        return super.onOptionsItemSelected(item);
    }

    private void removeUser(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(new DeleteUserRequest(User.getInstance().getId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Toast.makeText(MainActivity.this, "계정 탈퇴 성공", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "계정 탈퇴 실패", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }));
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
            target = "https://jaohoo.cafe24.com/NoticeList.php";
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
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;
                String noticeContent, noticeName, noticeDate;
                while (count < jsonArray.length()) {
                    JSONObject object = jsonArray.getJSONObject(count);
                    noticeContent = object.getString("noticeContent");
                    noticeName = object.getString("noticeName");
                    noticeDate = object.getString("noticeDate");
                    NoticeItem noticeItem = new NoticeItem(noticeContent, noticeName, noticeDate);
                    noticeItemList.add(noticeItem);
                    adapter.notifyDataSetChanged();
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한 번 더 눌러 종료합니다.", Toast.LENGTH_SHORT);
        lastTimeBackPressed = System.currentTimeMillis();
    }

    private void goToReservedListFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment reservedListFragment = new ReservedListFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, reservedListFragment);
        fragmentTransaction.commit();
    }

    private void goToEmptyFragment(String building) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment floorLIstFragment = new FloorListFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (building != null) {
            Bundle bundle = new Bundle();
            bundle.putString(PASS_BUILDING, building);
            floorLIstFragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.fragment, floorLIstFragment);
        fragmentTransaction.commit();
    }

    private void goToCourseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new CourseFragment());
        fragmentTransaction.commit();
    }

    private void goToScheduleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment, new ScheduleFragment());
        fragmentTransaction.commit();
    }

    private void goToFastFindActivity() {
        Intent intent = new Intent(this, GoogleMapActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainActivity(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void showNoticeList() {
        noticeListView = findViewById(R.id.noticeListView);
        noticeLayout = findViewById(R.id.ll_notice);

        noticeListView.setVisibility(View.VISIBLE);
        noticeItemList = new ArrayList<>();
        adapter = new NoticeListAdapter(getApplicationContext(), noticeItemList);
        noticeListView.setAdapter(adapter);
        new BackgroundTask().execute();
    }

    private void hideNoticeView() {
        if(noticeLayout !=null) noticeLayout.setVisibility(View.GONE);//공지사항부분이 보이지 않도록, fragment로 바꿔줄수 있게
    }
}
