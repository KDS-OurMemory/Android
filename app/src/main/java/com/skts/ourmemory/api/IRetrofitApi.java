package com.skts.ourmemory.api;

import com.skts.ourmemory.model.addschedule.AddSchedulePost;
import com.skts.ourmemory.model.addschedule.AddSchedulePostResult;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.signup.SignUpPost;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitApi {
    @GET("user/{snsId}")
    Observable<LoginPostResult> getIntroData(@Path("snsId") String snsId);

    @POST("user")
    Observable<SignUpPostResult> postSignUpData(@Body SignUpPost signUpPost);

    @POST("memory")
    Observable<AddSchedulePostResult> postAddScheduleData(@Body AddSchedulePost addSchedulePost);
}
