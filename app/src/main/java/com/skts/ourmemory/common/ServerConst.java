package com.skts.ourmemory.common;

public class ServerConst {

    /*Server URL*/
    public static final String SERVER_BASE_URL = "https://ourmemory.ddns.net:8443/v1/";

    /*Google*/
    public static final int RC_SIGN_IN = 900;          // 구글로그인 result 상수
    public static final String FIREBASE_PUSH_TOKEN = "FIREBASE_PUSH_TOKEN";

    /*Naver*/
    public static final String NAVER_RESULT_CODE = "resultcode";
    public static final String NAVER_RESPONSE = "response";
    public static final String NAVER_LOGIN_API_URL = "https://openapi.naver.com/v1/nid/me";
    public static final String NAVER_LOGIN_API_HEADER = "Bearer ";

    /*Naver HTTP error code*/
    public static final String NAVER_HTTP_ERROR_CODE_401 = "024";
    public static final String NAVER_HTTP_ERROR_CODE_402 = "028";
    public static final String NAVER_HTTP_ERROR_CODE_403 = "403";
    public static final String NAVER_HTTP_ERROR_CODE_404 = "404";
    public static final String NAVER_HTTP_ERROR_CODE_405 = "500";

    /*Common error code*/
    public static final String SUCCESS = "00";

    /*Login error code*/

    /*Sign up error code*/
    public static final String SERVER_ERROR_CODE_U404 = "U404";

    public static final String ANDROID = "AOS";

    /*Push*/
    public static final String FRIEND_REQUEST = "FRIEND_REQUEST";

    /*Friend*/
    public static final String WAIT = "WAIT";                       // 친구 요청 후 대기 상태
    public static final String REQUESTED_BY = "REQUESTED_BY";       // 친구 요청 받은 상태
    public static final String FRIEND = "FRIEND";                   // 친구
    public static final String BLOCK = "BLOCK";                     // 차단

    /*Share type*/
    public static final String SHARED_TYPE_USERS = "USERS";
    public static final String SHARED_TYPE_USER_GROUP = "USERS_GROUP";
    public static final String SHARED_TYPE_ROOMS = "ROOMS";

    /*Attendance*/
    public static final String STATUS_ATTEND = "ATTEND";
    public static final String STATUS_ABSENCE = "ABSENCE";
}
