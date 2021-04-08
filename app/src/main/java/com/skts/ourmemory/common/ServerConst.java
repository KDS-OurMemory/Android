package com.skts.ourmemory.common;

public class ServerConst {

    /*실서버*/
    //public static final String TEST_URL = "http://13.125.232.48:8080/OurMemory/v1/";
    //public static final String TEST_URL = "http://13.125.146.53:8080/OurMemory/v1/";
    //public static final String TEST_URL = "dykim.ddns.net:8080/SignUp";
    public static final String SERVER_BASE_URL = "http://13.125.146.53:8080/v1/";

    /*구글*/
    public static final int RC_SIGN_IN = 900;          // 구글로그인 result 상수
    public static final String FIREBASE_PUSH_TOKEN = "firebase_push_token";

    /*네이버*/
    public static final String NAVER_RESULT_CODE = "resultcode";
    public static final String NAVER_RESPONSE = "response";
    public static final String NAVER_LOGIN_API_URL = "https://openapi.naver.com/v1/nid/me";
    public static final String NAVER_LOGIN_API_HEADER = "Bearer ";

    /*네이버 HTTP 에러 코드*/
    public static final String NAVER_HTTP_ERROR_CODE_401 = "024";
    public static final String NAVER_HTTP_ERROR_CODE_402 = "028";
    public static final String NAVER_HTTP_ERROR_CODE_403 = "403";
    public static final String NAVER_HTTP_ERROR_CODE_404 = "404";
    public static final String NAVER_HTTP_ERROR_CODE_405 = "500";

    /*공통 에러 코드*/
    public static final String SUCCESS = "00";

    /*로그인 에러 코드*/

    /*회원가입 에러 코드*/
    public static final String SERVER_ERROR_CODE_U404 = "U404";
}
