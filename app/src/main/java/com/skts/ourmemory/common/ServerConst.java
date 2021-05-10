package com.skts.ourmemory.common;

public class ServerConst {

    /*Server URL*/
    public static final String SERVER_BASE_URL = "http://13.125.146.53:8080/v1/";

    /*Google*/
    public static final int RC_SIGN_IN = 900;          // 구글로그인 result 상수
    public static final String FIREBASE_PUSH_TOKEN = "firebase_push_token";

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
}
