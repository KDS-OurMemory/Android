package com.skts.ourmemory.api;

import com.skts.ourmemory.model.addschedule.AddSchedulePost;
import com.skts.ourmemory.model.addschedule.AddSchedulePostResult;
import com.skts.ourmemory.model.friend.AddFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.login.PatchPostResult;
import com.skts.ourmemory.model.notice.NoticePostResult;
import com.skts.ourmemory.model.room.CreateRoomPost;
import com.skts.ourmemory.model.room.CreateRoomPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IRetrofitApi {
    /**
     * 로그인, 사용자 정보 조회
     *
     * @param snsId   sns 번호
     * @param snsType sns 종류 (1: 카카오, 2: 구글, 3: 네이버)
     */
    @GET("user")
    Observable<LoginPostResult> getIntroData(@Query("snsId") String snsId, @Query("snsType") int snsType);

    /**
     * Fcm 푸시 토큰 요청
     *
     * @param userId    사용자 번호
     * @param pushToken Fcm 푸시 토큰
     */
    @PATCH("user/{userId}")
    Observable<PatchPostResult> patchIntroData(@Path("userId") int userId, @Body String pushToken);

    /**
     * 회원가입
     */
    @POST("user")
    Observable<SignUpPostResult> postSignUpData(@Body SignUpPost signUpPost);

    /**
     * 방 생성
     */
    @POST("room")
    Observable<CreateRoomPostResult> postRoomData(@Body CreateRoomPost createRoomPost);

    /**
     * 방 목록 조회
     *
     * @param userId User id
     */
    @GET("rooms/{userId}")
    Observable<RoomPostResult> getRoomData(@Path("userId") int userId);

    /**
     * 개인 일정 조회
     *
     * @param userId User id
     */
    @GET("memories/{userId}")
    Observable<SchedulePostResult> getScheduleData(@Path("userId") int userId);

    /**
     * 일정 생성
     */
    @POST("memory")
    Observable<AddSchedulePostResult> postAddScheduleData(@Body AddSchedulePost addSchedulePost);

    /**
     * 친구 목록 조회
     *
     * @param userId 사용자(친구) 번호
     */
    @GET("friends/{userId}")
    Observable<FriendPostResult> getFriendData(@Path("userId") int userId);

    /**
     * 사용자 조회
     *
     * @param userId 사용자(친구) 번호
     */
    @GET("users")
    Observable<UserPostResult> getUserDataId(@Query("userId") int userId);

    /**
     * 사용자 조회
     *
     * @param userName 사용자(친구) 이름
     */
    @GET("users")
    Observable<UserPostResult> getUserDataName(@Query("name") String userName);

    /**
     * 친구 추가
     *
     * @param userId   유저 번호
     * @param friendId 친구 번호
     */
    @POST("friend/{userId}")
    Observable<AddFriendPostResult> postAddFriendData(@Path("userId") int userId, @Body int friendId);

    @GET("notices/{userId}")
    Observable<NoticePostResult> getNoticeData(@Path("userId") int userId);
}
