package com.ioad.todoth.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    public static final String DATABASE = "TODOTh.db";
    private SQLiteDatabase db;
    private Cursor cursor;
    private Context mContext;
    private String tableName;
    private long now = 0;
    private Date date = null;
    private SimpleDateFormat dateFormat = null;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String listGroupQuery = "CREATE TABLE if not exists LIST_GROUP (INDEX_NUM INTEGER PRIMARY KEY AUTOINCREMENT, GROUP_TITLE, ADD_DATE, UPDATE_DATE, DELETE_DATE)";
        String listQuery = "CREATE TABLE if not exists TODO_LIST (INDEX_NUM INTEGER PRIMARY KEY AUTOINCREMENT, TITLE, CONTENT, ADD_DATE, UPDATE_DATE, DELETE_DATE, FINISH, RESTART)";
        Log.e(TAG, "create : " + listGroupQuery);
        Log.e(TAG, "create : " + listQuery);
        sqLiteDatabase.execSQL(listGroupQuery);
        sqLiteDatabase.execSQL(listQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertListGroupData(String tableName, String title) {
        db = getWritableDatabase();
        String insertQuery = "INSERT INTO " + tableName + " ('GROUP_TITLE', 'ADD_DATE') VALUES ('" + title + "', '" + getTime() + "');";
        Log.e(TAG, insertQuery);
        db.execSQL(insertQuery);
    }

    public void insertListData(String tableName, String title, String content) {
        db = getWritableDatabase();
        String insertQuery = "INSERT INTO " + tableName + " ('TITLE', 'CONTENT', 'ADD_DATE') VALUES ('" + title + "', '" + content + "', '" + getTime() + "');";
        Log.e(TAG, insertQuery);
        db.execSQL(insertQuery);
    }



    public Cursor selectListGroupData(String tableName) {
        db = getReadableDatabase();
        String selectQuery = "SELECT INDEX_NUM, GROUP_TITLE " +
                "FROM " + tableName +
                " WHERE DELETE_DATE IS NULL;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor selectListData(String tableName, String title) {
        db = getReadableDatabase();
        String selectQuery = "SELECT INDEX_NUM, TITLE, CONTENT " +
                "FROM " + tableName +
                " WHERE TITLE = '" + title + "'" +
                " ORDER by INDEX_NUM DESC;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }



    private String getTime() {
        String result;
        now = System.currentTimeMillis();
        date = new Date(now);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result = dateFormat.format(date);
        return result;
    }
}
