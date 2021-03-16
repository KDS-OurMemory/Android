package com.skts.ourmemory.api;

import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.model.signup.SignUpPostResult;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitApi {
    @GET("/posts")
    Call<List<SignUpPost>> getData(@Query("result") int result);

    /*@POST("/SignUp")
    Call<ReceiveUserModel> postData(@Body SendUserModel sendUserModel);*/

    @POST("SignUp")
    Observable<SignUpPost> postData(@Body SignUpPostResult signUpPostResult);
}
