package com.skts.ourmemory.api;

import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.ShareDAO;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.friend.FriendStatusPost;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.notice.DeleteNoticePostResult;
import com.skts.ourmemory.model.notice.NoticePostResult;
import com.skts.ourmemory.model.room.CreateRoomPost;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.model.room.EditRoomPost;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.DeleteSchedulePost;
import com.skts.ourmemory.model.schedule.EachSchedulePostResult;
import com.skts.ourmemory.model.schedule.EditSchedulePost;
import com.skts.ourmemory.model.schedule.SchedulePost;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.todolist.EachToDoListPostResult;
import com.skts.ourmemory.model.todolist.ToDoListPost;
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.model.user.MyPagePost;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Observable<LoginPostResult> getIntroData(@Path("snsId") String snsId, @Path("snsType") int snsType);

    /**
     * Fcm 푸시 토큰 요청
     *
     * @param userId    사용자 번호
     * @param pushToken Fcm 푸시 토큰
     */
    @PATCH("users/{userId}/token")
    Observable<MyPagePostResult> patchIntroData(@Path("userId") int userId, @Body String pushToken);

    /**
     * 회원가입
     */
    @POST("users")
    Observable<SignUpPostResult> postSignUpData(@Body SignUpPost signUpPost);

    /**
     * 방 생성
     */
    @POST("rooms")
    Observable<EachRoomPostResult> postRoomData(@Body CreateRoomPost createRoomPost);

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
     * 개인 일정 생성
     */
    @POST("memories")
    Observable<EachSchedulePostResult> postAddScheduleData(@Body SchedulePost schedulePost);

    /**
     * 방 내부 일정 생성
     */
    @POST("memories")
    Observable<EachSchedulePostResult> postAddRoomScheduleData(@Body SchedulePost schedulePost);

    /**
     * 일정 목록 조회
     *
     * @param writerId 사용자 번호
     */
    @GET("memories")
    Observable<SchedulePostResult> getScheduleDataId(@Query("writerId") int writerId);

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
    @PUT("rooms/{roomId}")
    Observable<BasicResponsePostResult> putRoomData(@Path("roomId") int roomId, @Body EditRoomPost editRoomPost);

    /**
     * 방장 양도
     */
    @PATCH("rooms/{roomId}/owner/{userId}")
    Observable<BasicResponsePostResult> patchMandateData(@Path("roomId") int roomId, @Path("userId") int userId);

    /**
     * 일정 개별 조회
     */

    /**
     * 일정 수정
     */
    @PUT("memories/{memoryId}/writer/{userId}")
    Observable<EachSchedulePostResult> putScheduleData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Body EditSchedulePost editSchedulePost);

    /**
     * 일정 삭제
     */
    @HTTP(method = "DELETE", hasBody = true, path = "memories/{memoryId}")
    Observable<EachSchedulePostResult> deleteScheduleData(@Path("memoryId") int memoryId, @Body DeleteSchedulePost deleteSchedulePost);

    /**
     * 일정 참석 여부 설정
     */
    @POST("memories/{memoryId}/attendance/{userId}/{status}")
    Observable<EachSchedulePostResult> postSelectAttendanceData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Path("status") String status);

    /**
     * 일정 공유
     */
    @POST("memories/{memoryId}/share/{userId}")
    Observable<EachSchedulePostResult> shareScheduleData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Body ShareDAO shareDAO);

    /**
     * 방 개별 조회
     */
    @GET("rooms/{roomId}")
    Observable<EachRoomPostResult> getEachRoomData(@Path("roomId") int roomId);

    /**
     * 방 삭제
     */

    /**
     * 사용자 조회
     *
     * @param userId 사용자 번호
     * @param targetId 조회할 번호
     */
    @GET("friends/users/{userId}/search")
    Observable<UserPostResult> getUserDataId(@Path("userId") int userId, @Query("targetId") int targetId);

    /**
     * 사용자 조회
     *
     * @param userId   사용자 번호
     * @param userName 조회할 이름
     */
    @GET("friends/users/{userId}/search")
    Observable<UserPostResult> getUserDataName(@Path("userId") int userId, @Query("name") String userName);

    /**
     * 사용자 조회
     *
     * @param userId 사용자 번호
     * @implNote friendStatus
     * WAIT: 친구 요청 후 대기 상태
     * REQUESTED_BY: 친구 요청 받은 상태
     * FRIEND: 친구
     * BLOCK: 차단
     */
    @GET("friends/users/{userId}/search")
    Observable<UserPostResult> getUserDataStatus(@Path("userId") int userId, @Query("friendStatus") String friendStatus);

    /**
     * 친구 요청
     */
    @POST("friends/request")
    Observable<BasicResponsePostResult> postRequestFriendData(@Body FriendPost friendPost);

    /**
     * 친구 요청 취소
     */
    @HTTP(method = "DELETE", hasBody = true, path = "friends/cancel")
    Observable<BasicResponsePostResult> deleteCancelFriendData(@Body FriendPost friendPost);

    /**
     * 친구 요청 수락
     */
    @POST("friends/accept")
    Observable<BasicResponsePostResult> postAcceptFriendData(@Body FriendPost friendPost);

    /**
     * 친구 재 추가
     */
    @POST("friends/reAdd")
    Observable<BasicResponsePostResult> postReAddFriendData(@Body FriendPost friendPost);

    /**
     * 친구 목록 조회
     *
     * @param userId 사용자 번호
     */
    @GET("friends/{userId}")
    Observable<FriendPostResult> getFriendData(@Path("userId") int userId);

    /**
     * 친구 상태 변경
     */
    @PATCH("friends/status")
    Observable<BasicResponsePostResult> patchFriendData(@Body FriendStatusPost friendStatusPost);

    /**
     * 친구 삭제
     */
    @POST("friends/{userId}/{friendUserId}")
    Observable<BasicResponsePostResult> postDeleteFriendData(@Path("userId") int userId, @Path("friendUserId") int friendUserId);

    /**
     * ToDoList 항목 추가
     */
    @POST("todo")
    Observable<EachToDoListPostResult> postToDoListData(@Body ToDoListPost toDoListPost);

    /**
     * ToDoList 항목 단일 조회
     */
    @GET("todo/{todoId}")
    Observable<EachToDoListPostResult> getToDoData(@Path("todoId") int todoId);

    /**
     * ToDoList 항목 조회
     */
    @GET("todo/user/{userId}")
    Observable<ToDoListPostResult> getToDoListData(@Path("userId") int userId);

    /**
     * ToDoList 항목 수정
     */
    @PUT("todo/{todoId}")
    Observable<EachToDoListPostResult> putToDoListData(@Path("todoId") int todoId, @Body ToDoListPost toDoListPost);

    /**
     * ToDoList 항목 삭제
     */
    @DELETE("todo/{todoId}")
    Observable<EachToDoListPostResult> deleteToDoListData(@Path("todoId") int todoId);

    /**
     * 내 정보 조회
     */
    @GET("users/{userId}")
    Observable<MyPagePostResult> getMyPageData(@Path("userId") int userId);

    /**
     * 내 정보 수정
     */
    @PUT("users/{userId}")
    Observable<MyPagePostResult> putMyPageData(@Path("userId") int userId, @Body MyPagePost myPagePost);

    /**
     * 회원 탈퇴
     */
    @DELETE("users/{userId}")
    Observable<MyPagePostResult> deleteMyPageData(@Path("userId") int userId);

    /**
     * 프로필 사진 업로드
     */
    @Multipart
    @POST("users/{userId}/profileImage")
    Observable<MyPagePostResult> putProfileData(@Path("userId") int userId, @Part MultipartBody.Part img);

    /**
     * 프로필 사진 삭제
     */
    @DELETE("users/{userId}/profileImage")
    Observable<MyPagePostResult> deleteProfileData(@Path("userId") int userId);

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
    @DELETE("notices/{noticeId}")
    Observable<DeleteNoticePostResult> deleteNoticeData(@Path("noticeId") int noticeId);
}
