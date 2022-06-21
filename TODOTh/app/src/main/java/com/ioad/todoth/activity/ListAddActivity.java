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
import com.ioad.todoth.adapter.ListAdapter;
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
    private String status;
    ClickCallbackListener listener;
    String type, titleName;
    int listSeq, typeIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add);

        etTitleName = findViewById(R.id.et_title_name);
        rvListAdd = findViewById(R.id.rv_add_list_group);
        btnListAdd = findViewById(R.id.btn_list_group);
        helper = new DBHelper(ListAddActivity.this);
        gridLayoutManager = new GridLayoutManager(ListAddActivity.this, 2);
        rvListAdd.setLayoutManager(gridLayoutManager);

        Intent intent = getIntent();
        if (intent != null) {
            listSeq = intent.getIntExtra("LIST_SEQ", 0);
            type = intent.getStringExtra("LIST_TYPE");
            typeIndex = intent.getIntExtra("TYPE_INDEX", 0);
            titleName = intent.getStringExtra("TITLE_NAME");
            status = intent.getStringExtra("STATUS");
            index = typeIndex;
            Log.e(TAG, "typeIndex " + typeIndex);
            Log.e(TAG, "status " + status);
            Log.e(TAG, "index " + index);
            if (status == null) {
                status = "insert";
                Log.e(TAG, "status " + status);
            } else if (status.equals("update")) {
                etTitleName.setText(titleName);
                btnListAdd.setText("수정");
            }
        }

        btnListAdd.setOnClickListener(btnOnClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        listAdd();
    }

    private void listAdd() {
        items = new ArrayList<>();


        for (int i = 0; i < Util.listType.length; i++) {
            item = new Item(Util.listType[i], Util.listImage[i]);
            items.add(item);
        }


        listener = new ClickCallbackListener() {
            @Override
            public void callBack(int position, String status) {
                Log.d(TAG, "position :: " + position);
                index = position;
//                status = status;
            }

            @Override
            public void callBackList(ArrayList<String> numbers) {

            }
        };

        if (status != null) {
            if (status.equals("insert")) {
                adapter = new ListAddAdapter(ListAddActivity.this, R.layout.group_list_add_item, items, listener);
            } else {
                adapter = new ListAddAdapter(ListAddActivity.this, R.layout.group_list_add_item, items, listener, typeIndex);
            }
            rvListAdd.setAdapter(adapter);
        }

    }


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            titleName = etTitleName.getText().toString();
            if (status.equals("insert")) {
                if (titleName.length() != 0) {
                    helper.insertListGroupData("LIST_GROUP", Util.listType[index], String.valueOf(index), titleName);
                    Intent intent = new Intent(ListAddActivity.this, MainActivity.class);
                    intent.putExtra("CALLBACK_INDEX", index);
                    startActivity(intent);
                    finish();
                } else {
                    Util.showToast(ListAddActivity.this, "리스트 이름을 작성해주세요!");
                }
            } else {
                if (titleName.length() != 0) {
                    helper.updateListGroupData("LIST_GROUP", Util.listType[index], String.valueOf(index), titleName, listSeq);
                    Intent intent = new Intent(ListAddActivity.this, ListItemActivity.class);
                    intent.putExtra("LIST_SEQ", listSeq);
                    intent.putExtra("LIST_TYPE", Util.listType[index]);
                    intent.putExtra("TYPE_INDEX", typeIndex);
                    intent.putExtra("TITLE_NAME", titleName);
                    Log.e(TAG, "LIST_SEQ " + listSeq);
                    Log.e(TAG, "LIST_TYPE " + Util.listType[index]);
                    Log.e(TAG, "TYPE_INDEX " + index);
                    Log.e(TAG, "TITLE_NAME " + titleName);
                    startActivity(intent);
                    finish();
                }
            }
        }
    };

}