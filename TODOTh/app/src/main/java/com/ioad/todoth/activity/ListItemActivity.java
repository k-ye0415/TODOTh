package com.ioad.todoth.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ioad.todoth.R;
import com.ioad.todoth.adapter.ListItemAdapter;
import com.ioad.todoth.bean.List;
import com.ioad.todoth.common.ClickCallbackListener;
import com.ioad.todoth.common.DBHelper;

import java.util.ArrayList;

public class ListItemActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    TextView tvItemTitle;
    RecyclerView rvListItem;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnItemAdd, btnTitleUpdate;

    ArrayList<List> lists;
    List list;

    EditText etItem;
    Button btnAddCancel, btnAdd;

    String type, titleName;
    int groupIndex, titleIndex;

    Dialog dialog;
    DBHelper helper;
    Cursor cursor;

    ClickCallbackListener listener;
    ArrayList<String> seqs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        tvItemTitle = findViewById(R.id.tv_list_item_title);
        rvListItem = findViewById(R.id.rv_list_item);
        btnItemAdd = findViewById(R.id.btn_list_item_add);
        btnTitleUpdate = findViewById(R.id.btn_update_title_name);

        helper = new DBHelper(ListItemActivity.this);
        lists = new ArrayList<>();
        seqs = new ArrayList<>();

        Intent intent = getIntent();
        groupIndex = Integer.parseInt(intent.getStringExtra("GROUP_INDEX"));
        type = intent.getStringExtra("LIST_TYPE");
        titleName = intent.getStringExtra("TITLE_NAME");
        titleIndex = intent.getIntExtra("LIST_INDEX", 0);

        Log.e(TAG, "index " + titleIndex);
        tvItemTitle.setText(titleName);

        btnItemAdd.setOnClickListener(btnOnClickListener);
        btnTitleUpdate.setOnClickListener(btnOnClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void getList() {
        cursor = helper.selectListData("TODO_LIST", type);
        lists.clear();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String finish = cursor.getString(3) == null ? "N" : cursor.getString(3);
                boolean isChecked = false;


                Log.e(TAG, cursor.getString(3));

                if (finish.equals("N")) {
                    isChecked = false;
                } else {
                    isChecked = true;
                }

                list = new List(seq, content, finish, isChecked);
                lists.add(list);
            }


            listener = new ClickCallbackListener() {
                @Override
                public void callBack(int position, String status) {

                }

                @Override
                public void callBackList(ArrayList<String> numbers) {
                    seqs = numbers;
                    Log.e(TAG, "Activity : " + seqs.toString());
                }
            };


            layoutManager = new LinearLayoutManager(ListItemActivity.this);
            rvListItem.setLayoutManager(layoutManager);
            adapter = new ListItemAdapter(ListItemActivity.this, R.layout.list_item, lists, listener);
            rvListItem.setAdapter(adapter);
        }
    }


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_list_item_add:
                    dialog = new Dialog(ListItemActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_layout);

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    Window window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();

                    etItem = dialog.findViewById(R.id.et_item);
                    btnAddCancel = dialog.findViewById(R.id.btn_item_add_cancel);
                    btnAdd = dialog.findViewById(R.id.btn_item_add);

                    btnAddCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnAdd.setOnClickListener(dialogBtnOnClickListener);
                    break;
                case R.id.btn_update_title_name:
                    Intent intent = new Intent(ListItemActivity.this, ListAddActivity.class);
                    intent.putExtra("GROUP_INDEX", groupIndex);
                    intent.putExtra("TITLE_NAME", titleName);
                    intent.putExtra("UPDATE_TITLE", true);
                    intent.putExtra("LIST_INDEX", titleIndex);
                    startActivity(intent);
                    break;
            }
        }
    };


    View.OnClickListener dialogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_item_add_cancel:
                    dialog.dismiss();
                    break;
                case R.id.btn_item_add:
                    String content = etItem.getText().toString();
                    if (content.length() != 0) {
                        helper.insertListData("TODO_LIST", type, content);
                        dialog.dismiss();
                        onResume();
                    } else {
                        Toast.makeText(ListItemActivity.this, "Writ TODO", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Activity onPause : " + seqs.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Activity onDestroy : " + seqs.toString());
        if (seqs.size() != 0) {
            for (int i = 0; i < seqs.size(); i++) {
                helper.updateListData("TODO_LIST", Integer.parseInt(seqs.get(i)));
            }
        }
    }
}