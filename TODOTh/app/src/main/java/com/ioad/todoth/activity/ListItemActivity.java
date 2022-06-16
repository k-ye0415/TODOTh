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
import com.ioad.todoth.common.ItemTouchHelperCallback;
import com.ioad.todoth.common.ItemTouchHelperListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItemActivity extends AppCompatActivity {

    TextView tvItemTitle;
    RecyclerView rvListItem;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Button btnItemAdd;

    ArrayList<List> lists;
    List list;

    EditText etItem;
    Button btnAddCancel, btnAdd;

    String title;

    Dialog dialog;
    DBHelper helper;
    Cursor cursor;

    ClickCallbackListener listener;
    ArrayList<String> seqs;

    ItemTouchHelper touchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        tvItemTitle = findViewById(R.id.tv_list_item_title);
        rvListItem = findViewById(R.id.rv_list_item);
        btnItemAdd = findViewById(R.id.btn_list_item_add);

        helper = new DBHelper(ListItemActivity.this);
        lists = new ArrayList<>();
        seqs = new ArrayList<>();

        Intent intent = getIntent();
        title = intent.getStringExtra("LIST_NAME");
        Log.e("TAG", intent.getStringExtra("LIST_NAME"));

        tvItemTitle.setText(title);

        btnItemAdd.setOnClickListener(btnOnClickListener);


    }

    private void setUpRecyclerView() {
        rvListItem.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                touchHelper.onDraw(c, parent, state);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    private void getList() {
        cursor = helper.selectListData("TODO_LIST", title);
        lists.clear();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String finish = cursor.getString(3) == null ? "N" : cursor.getString(3);
                boolean isChecked = false;


                Log.e("TAG", finish);

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
                public void callBack(int position) {

                }

                @Override
                public void callBackList(ArrayList<String> numbers) {
                    seqs = numbers;
                    Log.e("TAG", "Activity : " + seqs.toString());
                }
            };


            layoutManager = new LinearLayoutManager(ListItemActivity.this);
            rvListItem.setLayoutManager(layoutManager);
            adapter = new ListItemAdapter(ListItemActivity.this, R.layout.list_item, lists, listener);
            rvListItem.setAdapter(adapter);
            touchHelper = new ItemTouchHelper(new ItemTouchHelperCallback((ItemTouchHelperListener) adapter));
            touchHelper.attachToRecyclerView(rvListItem);
        }
    }


    View.OnClickListener btnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
                        helper.insertListData("TODO_LIST", title, content);
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
        Log.e("TAG", "Activity onPause : " + seqs.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "Activity onDestroy : " + seqs.toString());
        if (seqs.size() != 0) {
            for (int i = 0; i < seqs.size(); i++) {
                helper.updateListData("TODO_LIST", Integer.parseInt(seqs.get(i)));
            }
        }
    }
}