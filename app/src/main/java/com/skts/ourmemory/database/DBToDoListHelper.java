package com.skts.ourmemory.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBToDoListHelper extends SQLiteOpenHelper {

    public DBToDoListHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql = "CREATE TABLE if not exists " + DBConst.TABLE_NAME_TODO_LIST + "("
                + DBConst.DB_INDEX + " integer primary key autoincrement,"
                + DBConst.TODO_ID + " integer not null);";

        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        String sql = "DROP TABLE if exists " + DBConst.TABLE_NAME_TODO_LIST;

        database.execSQL(sql);
        onCreate(database);
    }

    /**
     * 데이터 삽입
     */
    public void insertData(int toDoId) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConst.TODO_ID, toDoId);
        database.insert(DBConst.TABLE_NAME_TODO_LIST, null, values);
    }

    /**
     * 데이터 조회
     */
    public Cursor selectData() {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.query(DBConst.TABLE_NAME_TODO_LIST, null, null, null, null, null, null);
    }

    /**
     * 데이터 삭제
     */
    public void deleteData(int toDoId) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(DBConst.TABLE_NAME_TODO_LIST, DBConst.TODO_ID + "=" + toDoId, null);
    }
}
