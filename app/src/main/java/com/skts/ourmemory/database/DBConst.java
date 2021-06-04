package com.skts.ourmemory.database;

public class DBConst {
    // Common
    public static final String DB_NAME = "ourMemory.db";
    public static final int DB_VERSION = 2;
    public static final String DB_INDEX = "_id";


    // User
    public static final String TABLE_NAME_USERS = "users";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "name";
    public static final String USER_BIRTHDAY = "birthday";
    public static final String USER_BIRTHDAY_OPEN = "birthdayOpen";
    public static final String USER_SOLAR = "solar";

    // Room
    public static final String TABLE_NAME_ROOMS = "rooms";
    public static final String ROOM_ID = "roomId";
    public static final String ROOM_NAME = "roomName";
    public static final String ROOM_OPENED = "roomOpened";
    public static final String ROOM_REG_DATE = "roomRegDate";
    public static final String ROOM_OWNER_ID = "roomOwnerId";

    // User-Room
    public static final String TABLE_NAME_USERS_ROOMS = "users_rooms";

    // Friend
    public static final String TABLE_NAME_FRIENDS = "friends";
    public static final String FRIEND_ID = "friendId";
}
