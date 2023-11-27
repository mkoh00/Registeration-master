package com.cookandroid.registeration.ui;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.cookandroid.registeration.R;

public class PopActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pop);

        TextView textViewAppVersion = findViewById(R.id.tv_app_version);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            textViewAppVersion.setText(" 어플리케이션 정보 : " + versionName + " Ver");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*0.9),(int)(height*0.85));
    }
}
