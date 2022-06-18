package com.ioad.todoth.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.ioad.todoth.R;
import com.ioad.todoth.adapter.ListAdapter;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    Context mContext;
    ImageButton btnSearch, btnListAdd;
    RecyclerView rvList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List list;
    ArrayList<List> lists;
    DBHelper helper;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;
        btnSearch = findViewById(R.id.btn_search);
        btnListAdd = findViewById(R.id.btn_list_group_add);
        rvList = findViewById(R.id.rv_list);

        lists = new ArrayList<>();
        helper = new DBHelper(mContext);

        btnSearch.setOnClickListener(btnOnClickListener);
        btnListAdd.setOnClickListener(btnOnClickListener);


    }


    @Override
    protected void onResume() {
        super.onResume();
        showList();
    }

    private void showList() {
        cursor = helper.selectListGroupData("LIST_GROUP");
        lists.clear();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String type = cursor.getString(1);
                int typeIndex = Integer.parseInt(cursor.getString(2));
                String titleName = cursor.getString(3);
                Log.e(TAG, "seq " + seq);
                Log.e(TAG, "type " + type);
                Log.e(TAG, "typeIndex " + typeIndex);
                Log.e(TAG, "titleName " + titleName);
                list = new List(seq, type, typeIndex, titleName);
                lists.add(list);

            }
        }

        layoutManager = new LinearLayoutManager(mContext);
        rvList.setLayoutManager(layoutManager);
        adapter = new ListAdapter(mContext, R.layout.group_list_item, lists);
        rvList.setAdapter(adapter);
    }


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.btn_search:
                    intent = new Intent(mContext, ListSearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_list_group_add:
                    intent = new Intent(mContext, ListAddActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}