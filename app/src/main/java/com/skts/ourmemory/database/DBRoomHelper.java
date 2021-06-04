package com.skts.ourmemory.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.skts.ourmemory.model.room.RoomPostResult;

import java.util.List;

public class DBRoomHelper extends SQLiteOpenHelper {
    public DBRoomHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Make a rooms table
        String sql = "CREATE TABLE if not exists " + DBConst.TABLE_NAME_ROOMS + "("
                + DBConst.ROOM_ID + " integer primary key not null ,"
                + DBConst.ROOM_NAME + " text not null ,"
                + DBConst.ROOM_OPENED + " integer not null ,"
                + DBConst.ROOM_REG_DATE + " text not null ,"
                + DBConst.ROOM_OWNER_ID + " integer"
                + ");";
        sqLiteDatabase.execSQL(sql);

        // Make a users table
        String sql2 = "CREATE TABLE if not exists " + DBConst.TABLE_NAME_USERS + "("
                + DBConst.USER_ID + " integer primary key not null ,"
                + DBConst.USER_NAME + " text ,"
                + DBConst.USER_BIRTHDAY + " text ,"
                + DBConst.USER_BIRTHDAY_OPEN + " integer not null ,"
                + DBConst.USER_SOLAR + " integer not null"
                + ");";
        sqLiteDatabase.execSQL(sql2);

        // Make a users-rooms table
        String sql3 = "CREATE TABLE if not exists " + DBConst.TABLE_NAME_USERS_ROOMS + "("
                + DBConst.USER_ID + " integer not null ,"
                + DBConst.ROOM_ID + " integer not null ,"
                + " PRIMARY KEY " + "(" + DBConst.USER_ID + ", " + DBConst.ROOM_ID + ")"
                + ");";
        sqLiteDatabase.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE if exists " + DBConst.TABLE_NAME_USERS;
        sqLiteDatabase.execSQL(sql);

        String sql2 = "DROP TABLE if exists " + DBConst.TABLE_NAME_ROOMS;
        sqLiteDatabase.execSQL(sql2);

        String sql3 = "DROP TABLE if exists " + DBConst.TABLE_NAME_USERS_ROOMS;
        sqLiteDatabase.execSQL(sql3);

        onCreate(sqLiteDatabase);
    }

    /**
     * 테이블 삭제
     */
    public void onDelete(SQLiteDatabase sqLiteDatabase) {
        String sql = "DROP TABLE " + DBConst.TABLE_NAME_USERS;
        sqLiteDatabase.execSQL(sql);

        String sql2 = "DROP TABLE " + DBConst.TABLE_NAME_ROOMS;
        sqLiteDatabase.execSQL(sql2);

        String sql3 = "DROP TABLE " + DBConst.TABLE_NAME_USERS_ROOMS;
        sqLiteDatabase.execSQL(sql3);
    }

    /**
     * 방 목록 데이터 삽입
     * 중복 데이터 시 데이터 변경
     */
    public void onInsertRoomData(RoomPostResult roomPostResult, SQLiteDatabase sqLiteDatabase) {
        List<RoomPostResult.ResponseValue> responseValueList = roomPostResult.getResponseValueList();
        for (int i = 0; i < responseValueList.size(); i++) {
            RoomPostResult.ResponseValue responseValue = responseValueList.get(i);
            List<RoomPostResult.Member> memberList = responseValue.getMemberList();
            int opened = responseValue.isOpened() ? 1 : 0;

            String sql = "INSERT OR REPLACE INTO " + DBConst.TABLE_NAME_ROOMS + " VALUES " + "("
                    + responseValue.getRoomId() + ", "
                    + "'" + responseValue.getName() + "'" + ", "
                    + opened + ", "
                    + "'" + responseValue.getRegDate() + "'" + ", "
                    + responseValue.getOwner()
                    + ");";
            sqLiteDatabase.execSQL(sql);
            for (int j = 0; j < memberList.size(); j++) {
                RoomPostResult.Member member = memberList.get(j);
                int birthdayOpen = member.isBirthdayOpen() ? 1 : 0;
                int solar = member.isSolar() ? 1 : 0;

                String sql2 = "INSERT OR REPLACE INTO " + DBConst.TABLE_NAME_USERS + " VALUES " + "("
                        + member.getUserId() + ", "
                        + "'" + member.getName() + "'" + ", "
                        + "'" + member.getBirthday() + "'" + ", "
                        + birthdayOpen + ", "
                        + solar
                        + ")";
                sqLiteDatabase.execSQL(sql2);

                String sql3 = "INSERT OR REPLACE INTO " + DBConst.TABLE_NAME_USERS_ROOMS + " VALUES " + "("
                        + member.getUserId() + ", "
                        + responseValue.getRoomId()
                        + ")";
                sqLiteDatabase.execSQL(sql3);
            }
        }
    }
}
