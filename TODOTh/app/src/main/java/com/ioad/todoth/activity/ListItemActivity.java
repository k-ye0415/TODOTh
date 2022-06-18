package com.ioad.todoth.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
    Button btnItemAdd, btnTitleUpdate, btnGroupDelete;

    ArrayList<List> lists;
    List list;

    Dialog dialog;
    WindowManager.LayoutParams layoutParams;
    Window window;
    // write dialog
    EditText etItem;
    Button btnAddCancel, btnAdd;

    // delete dialog
    Button btnDeleteCancel, btnDelete;

    String type, titleName;
    int listSeq, typeIndex;

    DBHelper helper;
    Cursor cursor;

    ClickCallbackListener listener;
    ArrayList<String> seqs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        tvItemTitle = findViewById(R.id.tv_list_group_name);
        rvListItem = findViewById(R.id.rv_list);
        btnItemAdd = findViewById(R.id.btn_list_item_add);
        btnTitleUpdate = findViewById(R.id.btn_update_list_group);
        btnGroupDelete = findViewById(R.id.btn_delete_list_group);

        helper = new DBHelper(ListItemActivity.this);
        lists = new ArrayList<>();
        seqs = new ArrayList<>();

        Intent intent = getIntent();
        listSeq = intent.getIntExtra("LIST_SEQ", 0);
        type = intent.getStringExtra("LIST_TYPE");
        titleName = intent.getStringExtra("TITLE_NAME");
        typeIndex = intent.getIntExtra("TYPE_INDEX", 0);
        Log.e(TAG, "onCreated ------------------------- ");
        Log.e(TAG, "LIST_SEQ " + listSeq);
        Log.e(TAG, "LIST_TYPE " + type);
        Log.e(TAG, "TYPE_INDEX " + typeIndex);
        Log.e(TAG, "TITLE_NAME " + titleName);
        tvItemTitle.setText(titleName);

        btnItemAdd.setOnClickListener(btnOnClickListener);
        btnTitleUpdate.setOnClickListener(btnOnClickListener);
        btnGroupDelete.setOnClickListener(btnOnClickListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        listSeq = intent.getIntExtra("LIST_SEQ", 0);
        type = intent.getStringExtra("LIST_TYPE");
        titleName = intent.getStringExtra("TITLE_NAME");
        typeIndex = intent.getIntExtra("TYPE_INDEX", 0);
        Log.d(TAG, "onResume ------------------------- ");
        Log.d(TAG, "LIST_SEQ " + listSeq);
        Log.d(TAG, "LIST_TYPE " + type);
        Log.d(TAG, "TYPE_INDEX " + typeIndex);
        Log.d(TAG, "TITLE_NAME " + titleName);
        tvItemTitle.setText(titleName);
        getList(type, listSeq);
    }

    private void getList(String type, int listSeq) {
        cursor = helper.selectListData("TODO_LIST", type, listSeq);
        lists.clear();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String seq = String.valueOf(cursor.getInt(0));
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String finish = cursor.getString(3) == null ? "N" : cursor.getString(3);
                boolean isChecked = false;

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
                    dialog.setContentView(R.layout.write_dialog_layout);

                    layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();

                    etItem = dialog.findViewById(R.id.et_content);
                    btnAddCancel = dialog.findViewById(R.id.btn_item_add_cancel);
                    btnAdd = dialog.findViewById(R.id.btn_item_add);

                    btnAddCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnAdd.setOnClickListener(dialogBtnOnClickListener);
                    break;
                case R.id.btn_update_list_group:
                    Intent intent = new Intent(ListItemActivity.this, ListAddActivity.class);
                    intent.putExtra("LIST_SEQ", listSeq);
                    intent.putExtra("LIST_TYPE", type);
                    intent.putExtra("TYPE_INDEX", typeIndex);
                    intent.putExtra("TITLE_NAME", titleName);
                    intent.putExtra("STATUS", "update");
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_delete_list_group:
                    dialog = new Dialog(ListItemActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.delete_dialog_layout);

                    layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

                    window = dialog.getWindow();
                    window.setAttributes(layoutParams);
                    dialog.show();

                    btnDeleteCancel = dialog.findViewById(R.id.btn_group_delete_cancel);
                    btnDelete = dialog.findViewById(R.id.btn_group_delete);

                    btnDeleteCancel.setOnClickListener(dialogBtnOnClickListener);
                    btnDelete.setOnClickListener(dialogBtnOnClickListener);

                    break;
            }
        }
    };


    View.OnClickListener dialogBtnOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_item_add_cancel:
                case R.id.btn_group_delete_cancel:
                    dialog.dismiss();
                    break;
                case R.id.btn_item_add:
                    String content = etItem.getText().toString();
                    if (content.length() != 0) {
                        helper.insertListData("TODO_LIST", type, titleName, content);
                        dialog.dismiss();
                        onResume();
                    } else {
                        Toast.makeText(ListItemActivity.this, "Writ TODO", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btn_group_delete:
                    helper.deleteListGroupData(listSeq);
                    dialog.dismiss();
                    Intent intent = new Intent(ListItemActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "Activity onRestart : " + seqs.toString());
        Intent intent = getIntent();
        String test = intent.getStringExtra("TEST");
        Log.e(TAG, "Activity onRestart : " + test);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Activity onStop : " + seqs.toString());
    }

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