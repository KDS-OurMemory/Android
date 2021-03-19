package com.skts.ourmemory.common;

public class ServerConst {

    /*실서버*/
    //public static final String TEST_URL = "http://13.125.232.48:8080/OurMemory/v1/";
    public static final String TEST_URL = "http://13.125.146.53:8080/OurMemory/v1/";
    //public static final String TEST_URL = "dykim.ddns.net:8080/SignUp";

    /*구글*/
    public static final int RC_SIGN_IN = 900;          // 구글로그인 result 상수
    public static final String FIREBASE_PUSH_TOKEN = "eQ_4IW9DSO22WnOfZVr4B6:APA91bEebQhnD8JTfoSHiycYhAssBut7NlJAaV_3CjiFY4xB-lwsEzxGpntda5yof_EWqyhbykOkKSZI2hfLI77rTaSVrONfdF25wPt1YQuvrEgTq_qT5HDDa1Hsm7fAAfvVaGCaMr1e";

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

    /*회원가입 결과 코드*/
    public static final int ON_NEXT = 0;
    public static final int ON_COMPLETE = 1;
    public static final int ON_ERROR = 9;
}
