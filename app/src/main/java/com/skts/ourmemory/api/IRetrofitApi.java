package com.skts.ourmemory.api;

import com.skts.ourmemory.model.friend.AcceptFriendPostResult;
import com.skts.ourmemory.model.friend.CancelFriendPostResult;
import com.skts.ourmemory.model.friend.DeleteFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.friend.ReAddFriendPostResult;
import com.skts.ourmemory.model.friend.RequestFriendPostResult;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.login.PatchPostResult;
import com.skts.ourmemory.model.notice.NoticePostResult;
import com.skts.ourmemory.model.room.CreateRoomPost;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.AddSchedulePost;
import com.skts.ourmemory.model.schedule.AddSchedulePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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
    Observable<RoomPostResult> postRoomData(@Body CreateRoomPost createRoomPost);

    /**
     * 방 목록 조회
     *
     * @param userId 사용자 번호
     */
    @GET("rooms")
    Observable<RoomPostResult> getRoomDataId(@Query("userId") int userId);

    /**
     * 방 목록 조회
     *
     * @param name 방 이름
     */
    @GET("rooms")
    Observable<RoomPostResult> getRoomDataName(@Query("name") String name);

    /**
     * 일정 목록 조회
     *
     * @param userId 사용자 번호
     */
    @GET("memories")
    Observable<SchedulePostResult> getScheduleDataId(@Query("userId") int userId);

    /**
     * 일정 목록 조회
     *
     * @param name 일정 제목
     */
    @GET("memories")
    Observable<SchedulePostResult> getScheduleDataName(@Query("name") String name);

    /**
     * 방 정보 수정
     */

    /**
     * 방장 위임
     */

    /**
     * 일정 생성
     */
    @POST("memories")
    Observable<AddSchedulePostResult> postAddScheduleData(@Body AddSchedulePost addSchedulePost);

    /**
     * 일정 개별 조회
     */

    /**
     * 일정 수정
     */

    /**
     * 일정 삭제
     */

    /**
     * 방 개별 조회
     */

    /**
     * 방 삭제
     */

    /**
     * 사용자 조회
     *
     * @param userId 사용자 번호
     * @param findId 조회할 번호
     */
    @GET("users/{userId}/search")
    Observable<UserPostResult> getUserDataId(@Path("userId") int userId, @Query("findId") int findId);

    /**
     * 사용자 조회
     *
     * @param userId   사용자 번호
     * @param userName 조회할 이름
     */
    @GET("users/{userId}/search")
    Observable<UserPostResult> getUserDataName(@Path("userId") int userId, @Query("name") String userName);

    /**
     * 사용자 조회
     *
     * @param userId       사용자 번호
     * @param friendStatus 조회할 친구 상태
     * @implNote WAIT: 친구 요청 후 대기 상태
     * REQUESTED_BY: 친구 요청 받은 상태
     * FRIEND: 친구
     * BLOCK: 차단
     */
    @GET("users/{userId}/search")
    Observable<UserPostResult> getUserDataStatus(@Path("userId") int userId, @Query("friendStatus") String friendStatus);

    /**
     * 친구 요청
     */
    @POST("friends/request")
    Observable<RequestFriendPostResult> postRequestFriendData(@Body FriendPost friendPost);

    /**
     * 친구 요청 취소
     */
    @HTTP(method = "DELETE", hasBody = true, path = "friends/cancel")
    Observable<CancelFriendPostResult> deleteCancelFriendData(@Body FriendPost friendPost);

    /**
     * 친구 요청 수락
     */
    @POST("friends/accept")
    Observable<AcceptFriendPostResult> postAcceptFriendData(@Body FriendPost friendPost);

    /**
     * 친구 재 추가
     */
    @POST("friends/reAdd")
    Observable<ReAddFriendPostResult> postReAddFriendData(@Body FriendPost friendPost);

    /**
     * 친구 목록 조회
     *
     * @param userId 사용자(친구) 번호
     */
    @GET("friends/{userId}")
    Observable<FriendPostResult> getFriendData(@Path("userId") int userId);

    /**
     * 친구 상태 변경
     */

    /**
     * 친구 삭제
     */
    @POST("friends/{userId}")
    Observable<DeleteFriendPostResult> postDeleteFriendData(@Path("userId") int userId);

    /**
     * 내 정보 조회
     */

    /**
     * 내 정보 수정
     */

    /**
     * 회원 탈퇴
     */

    /**
     * 알림 조회
     *
     * @param userId 사용자 번호
     */
    @GET("notices/{userId}")
    Observable<NoticePostResult> getNoticeData(@Path("userId") int userId);

    /**
     * 알림 삭제
     */
}
