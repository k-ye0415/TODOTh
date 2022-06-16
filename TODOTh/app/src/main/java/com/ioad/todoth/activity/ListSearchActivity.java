package com.ioad.todoth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.ioad.todoth.R;
import com.ioad.todoth.adapter.ListSearchAdapter;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.DBHelper;

import java.util.ArrayList;

public class ListSearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    EditText etSearch;
    Button btnSearch;
    CheckBox checkBox;
    RecyclerView rvSearchList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    DBHelper helper;
    Cursor cursor;
    ArrayList<List> lists;
    List list;
    String search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_search);

        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search_list);
        checkBox = findViewById(R.id.cb_check_box);
        rvSearchList = findViewById(R.id.rv_search_list);

        helper = new DBHelper(ListSearchActivity.this);
        lists = new ArrayList<>();

        btnSearch.setOnClickListener(btnOnClickListener);
        checkBox.setOnCheckedChangeListener(checkBoxOnClickListener);

    }

    CompoundButton.OnCheckedChangeListener checkBoxOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
            if (isCheck) {
                if (search.length() != 0) {
                    getNoFinishSearchList();
                }
            } else {
                getSearchList();
            }
        }
    };


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            search = etSearch.getText().toString();
            if (search.length() == 0) {
                Toast.makeText(ListSearchActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
            } else {
                getSearchList();
            }
        }
    };

    private void getSearchList() {
        cursor = helper.selectSearchData(search);
        lists.clear();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                list = new List(seq, title, content);
                lists.add(list);
            }


            layoutManager = new LinearLayoutManager(ListSearchActivity.this);
            rvSearchList.setLayoutManager(layoutManager);
            adapter = new ListSearchAdapter(ListSearchActivity.this, R.layout.search_list_item, lists);
            rvSearchList.setAdapter(adapter);

        } else {
            Toast.makeText(ListSearchActivity.this, "검색 내용이 없습니다", Toast.LENGTH_SHORT).show();
        }

    }

    private void getNoFinishSearchList() {
        cursor = helper.selectSearchFinishData(search);
        lists.clear();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);

                list = new List(seq, title, content);
                lists.add(list);
            }


            layoutManager = new LinearLayoutManager(ListSearchActivity.this);
            rvSearchList.setLayoutManager(layoutManager);
            adapter = new ListSearchAdapter(ListSearchActivity.this, R.layout.search_list_item, lists);
            rvSearchList.setAdapter(adapter);

        } else {
            Toast.makeText(ListSearchActivity.this, "검색 내용이 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

}