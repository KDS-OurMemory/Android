package com.skts.ourmemory.api;

import com.skts.ourmemory.model.addschedule.AddSchedulePost;
import com.skts.ourmemory.model.addschedule.AddSchedulePostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.login.PatchPostResult;
import com.skts.ourmemory.model.main.HomeRoomPostResult;
import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.model.signup.SignUpPostResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitApi {
    @GET("user")
    Observable<LoginPostResult> getIntroData(@Query("snsId") String snsId, @Query("snsType") int snsType);

    @PATCH("user/{userId}")
    Observable<PatchPostResult> patchIntroData(@Path("userId") int userId, @Body String pushToken);

    @POST("user")
    Observable<SignUpPostResult> postSignUpData(@Body SignUpPost signUpPost);

    @GET("rooms/{userId}")
    Observable<HomeRoomPostResult> getHomeRoomData(@Path("userId") int userId);

    @POST("memory")
    Observable<AddSchedulePostResult> postAddScheduleData(@Body AddSchedulePost addSchedulePost);

    @GET("friends/{userId}")
    Observable<FriendPostResult> getFriendData(@Path("userId") int userId);
}
