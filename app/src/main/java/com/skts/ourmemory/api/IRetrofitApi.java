package com.skts.ourmemory.api;

import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.friend.FriendDTO;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.login.LoginPostResult;
import com.skts.ourmemory.model.notice.NoticePostResult;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.model.room.RoomDTO;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.EachSchedulePostResult;
import com.skts.ourmemory.model.schedule.ScheduleDTO;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.todolist.EachToDoListPostResult;
import com.skts.ourmemory.model.todolist.ToDoListDTO;
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.model.user.UserDTO;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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
     * @param snsType sns 종류 (1: 카카오, 2: 구글, 3: 네이버)
     * @param snsId   sns 번호
     */
    @GET("users/snsType/{snsType}/snsId/{snsId}")
    Observable<LoginPostResult> getIntroData(@Path("snsType") int snsType, @Path("snsId") String snsId);

    /**
     * Fcm 푸시 토큰 요청, 푸시 토큰 수정
     *
     * @param userId 사용자 번호
     */
    @PATCH("users/{userId}/token")
    Observable<MyPagePostResult> patchIntroData(@Path("userId") int userId, @Body UserDTO userDTO);

    /**
     * 회원가입
     */
    @POST("users")
    Observable<MyPagePostResult> postSignUpData(@Body UserDTO userDTO);

    /**
     * 방 생성
     */
    @POST("rooms")
    Observable<EachRoomPostResult> postRoomData(@Body RoomDTO roomDTO);

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
     * 일정 생성, 일정 추가
     */
    @POST("memories")
    Observable<EachSchedulePostResult> postAddScheduleData(@Body ScheduleDTO scheduleDTO);

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
    Observable<EachRoomPostResult> putRoomData(@Path("roomId") int roomId, @Body RoomDTO roomDTO);

    /**
     * 방장 양도
     */
    @PATCH("rooms/{roomId}/owner/{userId}")
    Observable<EachRoomPostResult> patchMandateData(@Path("roomId") int roomId, @Path("userId") int userId);

    /**
     * 일정 개별 조회
     */
    @GET("memories/{memoryId}/room/{roomId}")
    Observable<EachSchedulePostResult> getEachScheduleData(@Path("memoryId") int memoryId, @Path("roomId") int roomId);

    /**
     * 일정 수정
     */
    @PUT("memories/{memoryId}/writer/{userId}")
    Observable<EachSchedulePostResult> putScheduleData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Body ScheduleDTO scheduleDTO);

    /**
     * 일정 삭제
     */
    //@HTTP(method = "DELETE", hasBody = true, path = "memories/{memoryId}")
    @DELETE("memories/{memoryId}/users/{userId}/rooms/{roomId}")
    Observable<BasicResponsePostResult> deleteScheduleData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Path("roomId") int roomId);

    /**
     * 일정 참석 여부 설정
     */
    @POST("memories/{memoryId}/attendance")
    Observable<EachSchedulePostResult> postSelectAttendanceData(@Path("memoryId") int memoryId, @Body ScheduleDTO scheduleDTO);

    /**
     * 일정 공유
     */
    @POST("memories/{memoryId}/share/{userId}")
    Observable<EachSchedulePostResult> shareScheduleData(@Path("memoryId") int memoryId, @Path("userId") int userId, @Body ScheduleDTO scheduleDTO);

    /**
     * 방 개별 조회, 방 단일 조회
     */
    @GET("rooms/{roomId}")
    Observable<EachRoomPostResult> getEachRoomData(@Path("roomId") int roomId);

    /**
     * 방 삭제
     */
    @DELETE("rooms/{roomId}/users/{userId}")
    Observable<BasicResponsePostResult> deleteRoomData(@Path("roomId") int roomId, @Path("userId") int userId);

    /**
     * 사용자 조회, 사용자 검색
     *
     * @param userId   사용자 번호
     * @param targetId 조회할 번호
     */
    @GET("friends/users/{userId}/search")
    Observable<FriendPostResult> getUserDataId(@Path("userId") int userId, @Query("targetId") int targetId);

    /**
     * 사용자 조회, 사용자 검색
     *
     * @param userId   사용자 번호
     * @param userName 조회할 이름
     */
    @GET("friends/users/{userId}/search")
    Observable<FriendPostResult> getUserDataName(@Path("userId") int userId, @Query("name") String userName);

    /**
     * 사용자 조회, 사용자 검색
     *
     * @param userId 사용자 번호
     * @implNote friendStatus
     * WAIT: 친구 요청 후 대기 상태
     * REQUESTED_BY: 친구 요청 받은 상태
     * FRIEND: 친구
     * BLOCK: 차단
     */
    @GET("friends/users/{userId}/search")
    Observable<FriendPostResult> getUserDataStatus(@Path("userId") int userId, @Query("friendStatus") String friendStatus);

    /**
     * 친구 요청
     */
    @POST("friends/request")
    Observable<UserPostResult> postRequestFriendData(@Body FriendDTO friendDTO);

    /**
     * 친구 요청 취소
     */
    @DELETE("friends/cancel/users/{userId}/friendUsers/{friendUserId}")
    Observable<BasicResponsePostResult> deleteCancelFriendData(@Path("userId") int userId, @Path("friendUserId") int friendUserId);

    /**
     * 친구 요청 수락, 친구 수락
     */
    @POST("friends/accept")
    Observable<UserPostResult> postAcceptFriendData(@Body FriendDTO friendDTO);

    /**
     * 친구 재 추가
     */
    @POST("friends/reAdd")
    Observable<UserPostResult> postReAddFriendData(@Body FriendDTO friendDTO);

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
    Observable<UserPostResult> patchFriendData(@Body FriendDTO friendDTO);

    /**
     * 친구 삭제
     */
    @DELETE("friends/users/{userId}/friendUsers/{friendUserId}")
    Observable<BasicResponsePostResult> postDeleteFriendData(@Path("userId") int userId, @Path("friendUserId") int friendUserId);

    /**
     * ToDoList 항목 추가
     */
    @POST("todo")
    Observable<EachToDoListPostResult> postToDoListData(@Body ToDoListDTO toDoListDTO);

    /**
     * ToDoList 항목 단일 조회
     */
    @GET("todo/{todoId}")
    Observable<EachToDoListPostResult> getToDoData(@Path("todoId") int todoId);

    /**
     * ToDoList 항목 조회, ToDoList 목록 조회
     */
    @GET("todo/user/{userId}")
    Observable<ToDoListPostResult> getToDoListData(@Path("userId") int userId);

    /**
     * ToDoList 항목 수정
     */
    @PUT("todo/{todoId}")
    Observable<EachToDoListPostResult> putToDoListData(@Path("todoId") int todoId, @Body ToDoListDTO toDoListDTO);

    /**
     * ToDoList 항목 삭제
     */
    @DELETE("todo/{todoId}")
    Observable<BasicResponsePostResult> deleteToDoListData(@Path("todoId") int todoId);

    /**
     * 내 정보 조회
     */
    @GET("users/{userId}")
    Observable<MyPagePostResult> getMyPageData(@Path("userId") int userId);

    /**
     * 내 정보 수정
     */
    @PUT("users/{userId}")
    Observable<MyPagePostResult> putMyPageData(@Path("userId") int userId, @Body UserDTO userDTO);

    /**
     * 회원 탈퇴, 사용자 삭제
     */
    @DELETE("users/{userId}")
    Observable<BasicResponsePostResult> deleteMyPageData(@Path("userId") int userId);

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
    Observable<BasicResponsePostResult> deleteNoticeData(@Path("noticeId") int noticeId);
}
