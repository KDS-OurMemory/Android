package com.skts.ourmemory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.skts.ourmemory.model.friend.FriendPostResult;

import java.util.List;

public class DBFriendHelper extends SQLiteOpenHelper {

    public DBFriendHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE if not exists " + DBConst.TABLE_NAME_FRIENDS + "("
                + DBConst.FRIEND_ID + " integer primary key not null"
                + ");";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE if exists " + DBConst.TABLE_NAME_FRIENDS;

        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    /**
     * 테이블 삭제
     */
    public void onDelete(SQLiteDatabase sqLiteDatabase) {
        String sql = "DROP TABLE " + DBConst.TABLE_NAME_FRIENDS;
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * 친구 데이터 삽입
     * 중복 데이터 시 데이터 변경
     */
    public void onInsertFriendData(List<FriendPostResult.ResponseValue> responseValueList, SQLiteDatabase sqLiteDatabase) {
        for (int i = 0; i < responseValueList.size(); i++) {
            String sql = "INSERT OR REPLACE INTO " + DBConst.TABLE_NAME_FRIENDS + " VALUES " + "("
                    + responseValueList.get(i).getUserId()
                    + ");";
            sqLiteDatabase.execSQL(sql);

            // INSERT OR REPLACE INTO USERS (userId, userName) VALUES (156, '테스트');
            String sql2 = "INSERT OR REPLACE INTO " + DBConst.TABLE_NAME_USERS
                    + " (" + DBConst.USER_ID + ", "
                    + DBConst.USER_NAME + ")"
                    + " VALUES " + "("
                    + responseValueList.get(i).getUserId() + ", "
                    + "'" + responseValueList.get(i).getName() + "'"
                    + ");";
            sqLiteDatabase.execSQL(sql2);
        }
    }
}
