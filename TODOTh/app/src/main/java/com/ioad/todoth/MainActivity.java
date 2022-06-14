package com.ioad.todoth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    ImageButton btnSearch, btnListAdd;
    RecyclerView rvList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        btnSearch = findViewById(R.id.btn_search);
        btnListAdd = findViewById(R.id.btn_list_add);
        rvList = findViewById(R.id.rv_list);


        btnListAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ListAddActivity.class);
                startActivity(intent);
            }
        });


    }
}