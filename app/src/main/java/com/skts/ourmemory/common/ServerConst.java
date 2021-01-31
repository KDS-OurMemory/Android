package com.skts.ourmemory.common;

public class ServerConst {

    /*실서버*/
    public static final String TEST_URL = "http://dykim.ddns.net:8080";
    //public static final String TEST_URL = "dykim.ddns.net:8080/SignUp";


    /*네이버*/
    public static final String NAVER_RESULT_CODE = "resultcode";
    public static final String NAVER_RESPONSE = "response";
    public static final String NAVER_LOGIN_API_URL = "https://openapi.naver.com/v1/nid/me";
    public static final String NAVER_LOGIN_API_HEADER = "Bearer ";

    //네이버 HTTP 에러 코드
    public static final String NAVER_HTTP_ERROR_CODE_401 = "024";
    public static final String NAVER_HTTP_ERROR_CODE_402 = "028";
    public static final String NAVER_HTTP_ERROR_CODE_403 = "403";
    public static final String NAVER_HTTP_ERROR_CODE_404 = "404";
    public static final String NAVER_HTTP_ERROR_CODE_405 = "500";
}
