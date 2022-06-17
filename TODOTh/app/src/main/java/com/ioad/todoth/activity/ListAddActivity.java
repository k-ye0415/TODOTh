package com.ioad.todoth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ioad.todoth.R;
import com.ioad.todoth.adapter.ListAddAdapter;
import com.ioad.todoth.bean.Item;
import com.ioad.todoth.common.ClickCallbackListener;
import com.ioad.todoth.common.DBHelper;
import com.ioad.todoth.common.Util;

import java.util.ArrayList;

public class ListAddActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private ArrayList<Item> items;
    private Item item;

    private EditText etTitleName;
    private Button btnListAdd;

    private RecyclerView rvListAdd;
    private RecyclerView.Adapter adapter;
    private GridLayoutManager gridLayoutManager;
    private DBHelper helper;
    private int index;
    ClickCallbackListener listener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);

        etTitleName = findViewById(R.id.et_title);
        rvListAdd = findViewById(R.id.rv_add_list);
        btnListAdd = findViewById(R.id.btn_list_add);
        helper = new DBHelper(ListAddActivity.this);
        gridLayoutManager = new GridLayoutManager(ListAddActivity.this, 2);
        rvListAdd.setLayoutManager(gridLayoutManager);

        listAdd();

        btnListAdd.setOnClickListener(btnOnClickListener);

    }

    private void listAdd() {
        items = new ArrayList<>();



        for (int i = 0; i < Util.listName.length; i++) {
            item = new Item(Util.listName[i], Util.listImage[i]);
            items.add(item);
        }


        listener = new ClickCallbackListener() {
            @Override
            public void callBack(int position) {
                Log.e(TAG, "position :: " + position);
                index = position;
            }

            @Override
            public void callBackList(ArrayList<String> numbers) {

            }
        };

        adapter = new ListAddAdapter(ListAddActivity.this, R.layout.group_list_add_item, items, listener);
        rvListAdd.setAdapter(adapter);
    }


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String titleName = etTitleName.getText().toString();
            if (titleName.length() != 0) {
                helper.insertListGroupData("LIST_GROUP", Util.listName[index], String.valueOf(index), titleName);
//            helper.insertListData("TODO_LIST", Util.listName[index]);
                Intent intent = new Intent(ListAddActivity.this, MainActivity.class);
                intent.putExtra("INDEX", intent);
                startActivity(intent);
            } else {
                Toast.makeText(ListAddActivity.this, "Title Check!", Toast.LENGTH_SHORT).show();
            }
        }
    };


}