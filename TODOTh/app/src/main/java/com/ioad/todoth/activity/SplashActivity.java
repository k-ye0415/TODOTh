package com.ioad.todoth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.ioad.todoth.R;
import com.ioad.todoth.common.DBHelper;

public class SplashActivity extends AppCompatActivity {

    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        helper = new DBHelper(SplashActivity.this);
        db = helper.getWritableDatabase();
        helper.onCreate(db);
        moveToMain();


    }


    private void moveToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}