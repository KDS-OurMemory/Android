package com.skts.ourmemory.api;

import com.skts.ourmemory.model.addschedule.AddSchedulePost;
import com.skts.ourmemory.model.addschedule.AddSchedulePostResult;
import com.skts.ourmemory.model.friend.AddFriendPostResult;
import com.skts.ourmemory.model.friend.DeleteFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.friend.RequestFriendPostResult;
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
    @GET("users/{snsId}/{snsType}")
    //Observable<LoginPostResult> getIntroData(@Query("snsId") String snsId, @Query("snsType") int snsType);
    Observable<LoginPostResult> getIntroData(@Path("snsId") String snsId, @Path("snsType") int snsType);

    /**
     * Fcm 푸시 토큰 요청
     *
     * @param userId    사용자 번호
     * @param pushToken Fcm 푸시 토큰
     */
    @PATCH("users/{userId}/token")
    Observable<PatchPostResult> patchIntroData(@Path("userId") int userId, @Body String pushToken);

    /**
     * 회원가입
     */
    @POST("users")
    Observable<SignUpPostResult> postSignUpData(@Body SignUpPost signUpPost);

    /**
     * 방 생성
     */
    @POST("rooms")
    Observable<CreateRoomPostResult> postRoomData(@Body CreateRoomPost createRoomPost);

    /**
     * 방 목록 조회
     *
     * @param userId 사용자 번호
     */
    @GET("rooms/{userId}")
    Observable<RoomPostResult> getRoomData(@Path("userId") int userId);

    /**
     * 개인 일정 조회
     *
     * @param userId 사용자 번호
     */
    @GET("memories/{userId}")
    Observable<SchedulePostResult> getScheduleData(@Path("userId") int userId);

    /**
     * 일정 생성
     */
    @POST("memories")
    Observable<AddSchedulePostResult> postAddScheduleData(@Body AddSchedulePost addSchedulePost);

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
     * 친구 요청
     */
    @POST("friends/request")
    Observable<RequestFriendPostResult> postRequestFriendData(@Body FriendPost friendPost);

    /**
     * 친구 추가
     */
    @POST("friends")
    Observable<AddFriendPostResult> postAddFriendData(@Body FriendPost friendPost);

    /**
     * 친구 목록 조회
     *
     * @param userId 사용자(친구) 번호
     */
    @GET("friends/{userId}")
    Observable<FriendPostResult> getFriendData(@Path("userId") int userId);

    /**
     * 친구 삭제
     */
    @POST("friends/{userId}")
    Observable<DeleteFriendPostResult> postDeleteFriendData(@Path("userId") int userId);

    // 사용자 정보 수정

    /**
     * 알림 조회
     *
     * @param userId 사용자 번호
     * @return
     */
    @GET("notices/{userId}")
    Observable<NoticePostResult> getNoticeData(@Path("userId") int userId);

    // 알림 삭제
}
