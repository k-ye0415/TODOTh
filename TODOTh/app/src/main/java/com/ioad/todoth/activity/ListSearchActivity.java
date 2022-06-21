package com.ioad.todoth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ioad.todoth.R;
import com.ioad.todoth.adapter.ListSearchAdapter;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.DBHelper;
import com.ioad.todoth.common.Shared;
import com.ioad.todoth.common.Util;

import java.util.ArrayList;

public class ListSearchActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    EditText etSearch;
    ImageView btnSearch;
    CheckBox checkBox;
    RecyclerView rvSearchList;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    InputMethodManager methodManager;

    DBHelper helper;
    Cursor cursor;
    ArrayList<List> lists;
    List list;
    String search;
    boolean cbIsCheck;
    boolean btnIsClick;

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

        cbIsCheck = Shared.getCheckBoxPref(ListSearchActivity.this, "NO_FINISH");
        checkBox.setChecked(cbIsCheck);

        methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        methodManager.showSoftInput(etSearch, 0);

        etSearch.postDelayed(new Runnable() {
            @Override
            public void run() {
                etSearch.requestFocus();
                methodManager.showSoftInput(etSearch, 0);
            }
        }, 100);

    }

    CompoundButton.OnCheckedChangeListener checkBoxOnClickListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isCheck) {
            if (isCheck) {
                cbIsCheck = true;
                Shared.setCheckBoxPref(ListSearchActivity.this, "NO_FINISH", true);
                if (btnIsClick) {
                    getNoFinishSearchList();
                }
            } else {
                cbIsCheck = false;
                Shared.setCheckBoxPref(ListSearchActivity.this, "NO_FINISH", false);
                if (btnIsClick) {
                    getSearchList();
                }
            }
        }
    };


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            search = etSearch.getText().toString();
            if (!cbIsCheck) {
                if (search.length() == 0) {
                    Util.showToast(ListSearchActivity.this, "검색어를 입력해주세요");
                    btnIsClick = false;
                } else {
                    getSearchList();
                    btnIsClick = true;
                }
            } else {
                getNoFinishSearchList();
                btnIsClick = true;
            }
            methodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    };

    private void getSearchList() {

        cursor = helper.selectSearchData(search);
        lists.clear();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String titleName = cursor.getString(2);
                String content = cursor.getString(3);
                int listGroupSeq = Integer.parseInt(cursor.getString(4));
                int typeIndex = Integer.parseInt(cursor.getString(5));

                list = new List(seq, title, titleName, content, listGroupSeq, typeIndex);
                lists.add(list);
            }


            layoutManager = new LinearLayoutManager(ListSearchActivity.this);
            rvSearchList.setLayoutManager(layoutManager);
            adapter = new ListSearchAdapter(ListSearchActivity.this, R.layout.search_list_item, lists);
            rvSearchList.setAdapter(adapter);

        } else {
            Util.showToast(ListSearchActivity.this, "검색 내용이 없습니다");
        }

    }

    private void getNoFinishSearchList() {
        cursor = helper.selectSearchFinishData(search);
        lists.clear();

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String titleName = cursor.getString(2);
                String content = cursor.getString(3);
                int listGroupSeq = Integer.parseInt(cursor.getString(4));
                int typeIndex = Integer.parseInt(cursor.getString(5));

                list = new List(seq, title, titleName, content, listGroupSeq, typeIndex);
                lists.add(list);
            }


            layoutManager = new LinearLayoutManager(ListSearchActivity.this);
            rvSearchList.setLayoutManager(layoutManager);
            adapter = new ListSearchAdapter(ListSearchActivity.this, R.layout.search_list_item, lists);
            rvSearchList.setAdapter(adapter);

        } else {
            Util.showToast(ListSearchActivity.this, "검색 내용이 없습니다");
        }
    }

}