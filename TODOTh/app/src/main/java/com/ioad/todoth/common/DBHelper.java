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
    private final String GROUP_TABLE_NAME = "LIST_GROUP";
    private final String LIST_TABLE_NAME = "TODO_LIST";
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
        String listGroupQuery = "CREATE TABLE if not exists " + GROUP_TABLE_NAME +
                " (INDEX_NUM INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TYPE, " +
                "TITLE_NAME, " +
                "TYPE_INDEX, " +
                "ADD_DATE, " +
                "UPDATE_DATE, DELETE_DATE)";
        String listQuery = "CREATE TABLE if not exists " + LIST_TABLE_NAME +
                " (INDEX_NUM INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "TYPE, " +
                "TITLE_NAME, " +
                "GROUP_INDEX, " +
                "CONTENT, " +
                "SELECT_DATE, " +
                "SELECT_TIME, " +
                "ADD_DATE, " +
                "UPDATE_DATE, " +
                "DELETE_DATE, " +
                "FINISH, " +
                "RESTART)";
        Log.e(TAG, "create : " + listGroupQuery);
        Log.e(TAG, "create : " + listQuery);
        sqLiteDatabase.execSQL(listGroupQuery);
        sqLiteDatabase.execSQL(listQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertListGroupData(String tableName, String type, String index, String titleName) {
        db = getWritableDatabase();
        String insertQuery = "INSERT INTO " + tableName + " ('TYPE', 'TYPE_INDEX', 'TITLE_NAME', 'ADD_DATE') VALUES ('" + type + "', '" + index + "', '" + titleName + "', '" + getTime() + "');";
        Log.e(TAG, insertQuery);
        db.execSQL(insertQuery);
    }

    public void insertListData(String tableName, String type, String titleName, String content, int groupIndex, String selectDate, String selectTime) {
        db = getWritableDatabase();
        String insertQuery = "INSERT INTO " + LIST_TABLE_NAME +
                " ('TYPE', " +
                "'TITLE_NAME', " +
                "'GROUP_INDEX', " +
                "'CONTENT', " +
                "'FINISH', " +
                "'SELECT_DATE', " +
                "'SELECT_TIME', " +
                "'ADD_DATE') " +
                "VALUES ('" +
                    type + "', '" +
                    titleName + "', '" +
                    groupIndex + "', '" +
                    content + "', " +
                    "'N', '" +
                    selectDate + "', '" +
                    selectTime + "', '" +
                    getTime() + "');";
        Log.e(TAG, insertQuery);
        db.execSQL(insertQuery);
    }


    public Cursor selectListGroupData(String tableName) {
        db = getReadableDatabase();
        String selectQuery = "SELECT INDEX_NUM, TYPE, TYPE_INDEX, TITLE_NAME " +
                "FROM " + tableName +
                " WHERE DELETE_DATE IS NULL;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor selectListData(String tableName, String type, int listSeq) {
        db = getReadableDatabase();
        String selectQuery = "SELECT " +
                "    L1.INDEX_NUM, " +
                "    L1.TYPE, " +
                "    L1.CONTENT, " +
                "    L1.FINISH, " +
                "    L1.SELECT_DATE " +
                "FROM " + LIST_TABLE_NAME + " AS L1 " +
                "JOIN " + GROUP_TABLE_NAME + " AS L2 " +
                "ON L1.TYPE = L2.TYPE " +
                "WHERE L1.TYPE = '" + type + "' " +
                "AND L1.GROUP_INDEX = L2.INDEX_NUM " +
                "AND L1.GROUP_INDEX = '" + listSeq + "'" +
                "AND L1.DELETE_DATE IS NULL " +
                "ORDER by L1.INDEX_NUM DESC;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor selectSearchData(String search) {
        db = getReadableDatabase();
        String selectQuery = "SELECT L2.INDEX_NUM, L2.TYPE, L2.TITLE_NAME, L2.CONTENT, L1.INDEX_NUM, L1.TYPE_INDEX " +
                "FROM LIST_GROUP as L1 " +
                "JOIN TODO_LIST as L2 " +
                "ON L1.TYPE = L2.TYPE " +
                "WHERE (L2.TITLE_NAME || L2.CONTENT) LIKE '%" + search + "%'" +
                "AND L2.GROUP_INDEX = L1.INDEX_NUM " +
                "AND L1.DELETE_DATE IS NULL " +
                "AND L2.DELETE_DATE IS NULL " +
                "ORDER by L2.INDEX_NUM DESC;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor selectSearchFinishData(String search) {
        db = getReadableDatabase();
        String selectQuery = "SELECT L2.INDEX_NUM, L2.TYPE, L2.TITLE_NAME, L2.CONTENT, L1.INDEX_NUM, L1.TYPE_INDEX " +
                "FROM LIST_GROUP as L1 " +
                "JOIN TODO_LIST as L2 " +
                "ON L1.TYPE = L2.TYPE " +
                "WHERE (L2.TITLE_NAME || L2.CONTENT) LIKE '%" + search + "%'" +
                "AND L1.DELETE_DATE IS NULL " +
                "AND L2.DELETE_DATE IS NULL " +
                "AND L2.FINISH = 'N' " +
                "ORDER by L2.INDEX_NUM DESC;";
        Log.e(TAG, selectQuery);
        cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }


    public void updateListData(String tableName, int index) {
        db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + tableName +
                " SET FINISH = 'Y', " +
                "UPDATE_DATE = '" + getTime() + "' " +
                "WHERE INDEX_NUM = " + index + ";";
        Log.e(TAG, updateQuery);
        db.execSQL(updateQuery);
    }

    public void updateListItemData(int index, String content) {
        db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + LIST_TABLE_NAME +
                " SET CONTENT = '" + content + "', " +
                "UPDATE_DATE = '" + getTime() + "' " +
                "WHERE INDEX_NUM = " + index + ";";
        Log.e(TAG, updateQuery);
        db.execSQL(updateQuery);
    }

    public void updateListGroupData(String tableName, String type, String index, String titleName, int listSeq) {
        String[] query = new String[2];
        db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + GROUP_TABLE_NAME +
                " SET TYPE = '" + type + "', " +
                "TITLE_NAME = '" + titleName + "', " +
                "TYPE_INDEX = " + index + ", " +
                "UPDATE_DATE = '" + getTime() + "' " +
                "WHERE INDEX_NUM = " + listSeq + ";";
        query[0] = updateQuery;
        updateQuery = "UPDATE " + LIST_TABLE_NAME +
                " SET TYPE = '" + type + "', " +
                "TITLE_NAME = '" + titleName + "' " +
                "WHERE INDEX_NUM = " + listSeq + ";";
        query[1] = updateQuery;
        for (int i = 0; i < query.length; i++) {
            Log.e(TAG, query[i]);
            db.execSQL(query[i]);
        }
    }

    public void deleteListGroupData(int listSeq) {
        db = this.getWritableDatabase();
        String deleteQuery = "UPDATE LIST_GROUP " +
                "SET DELETE_DATE = '" + getTime() + "' " +
                "WHERE INDEX_NUM = " + listSeq;
        Log.e(TAG, deleteQuery);
        db.execSQL(deleteQuery);
    }

    public void deleteListData(int listSeq) {
        db = this.getWritableDatabase();
        String deleteQuery = "UPDATE " + LIST_TABLE_NAME + " " +
                "SET DELETE_DATE = '" + getTime() + "' " +
                "WHERE INDEX_NUM = " + listSeq;
        Log.e(TAG, deleteQuery);
        db.execSQL(deleteQuery);
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
