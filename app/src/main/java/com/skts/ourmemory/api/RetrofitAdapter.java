package com.skts.ourmemory.api;

import com.skts.ourmemory.common.ServerConst;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAdapter {
    public RetrofitAdapter() {
    }

    private static class Singleton {
        // 싱글톤 패턴
        private static final RetrofitAdapter instance = new RetrofitAdapter();
    }

    public static RetrofitAdapter getInstance() {
        return Singleton.instance;
    }

    public IRetrofitApi getServiceApi() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl(ServerConst.TEST_URL)
                .build();

        return retrofit.create(IRetrofitApi.class);
    }
}
