package com.skts.ourmemory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBFriendHelper extends SQLiteOpenHelper {

    public DBFriendHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE if not exists " +  DBConst.TABLE_NAME_FRIEND + "("
                + DBConst.DB_INDEX + "integer primary key autoincrement,"
                + DBConst.USER_ID + "integer not null ,"
                + DBConst.USER_NAME + "text not null);";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE if exists " + DBConst.TABLE_NAME_FRIEND;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
