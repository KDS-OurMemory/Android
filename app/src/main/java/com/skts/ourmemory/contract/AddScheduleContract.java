package com.skts.ourmemory.contract;

import android.content.Context;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.schedule.EachSchedulePostResult;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddScheduleContract {
    public interface Model extends BaseContract.Model {
        // 개인 일정 추가 요청
        void setAddScheduleData(int userId, Integer roomId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable);

        // 방 일정 추가 요청
        void setAddRoomScheduleData(int userId, Integer roomId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable);

        // 일정 수정 요청
        void putScheduleData(int memoryId, int userId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable);

        // 친구 데이터 요청
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);

        // 일정 삭제 요청
        void deleteScheduleData(int memoryId, int userId, int targetRoomId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        Context getAppContext();            // Context 리턴

        void showToast(String message);

        void initSet();

        void initBinding();

        void initDateView(String startDate, String endDate, int selectYear, int selectMonth, int selectDay);        // 초기 날짜 설정

        void setTimeTextView();                             // 선택 날짜 적용

        void setDateTextView();                             // 스크롤 시 변경 날짜 적용

        void setAlarmView(String startDate, String firstAlarm, String secondAlarm);     // 선택 알람 설정

        String calcAlarm(String date1, String date2);       // 알람 계산

        void setColorView(String color);                    // 선택 색상 설정

        void initAlarmView();                               // 기본 알람 설정

        void initColor();                                   // 기본 색상 설정

        void setStartCalendarValue();                       // 시작 날짜 설정

        void setEndCalendarValue();                         // 종료 날짜 설정

        void displayDialogAlarmText();                      // 다이얼로그에 알람 텍스트 보여주는 함수

        void displayAlarmText();                            // 메인에 알람 텍스트 보여주는 함수

        void setColor(AlertDialog mAlertDialog);            // 선택한 색상 적용함수

        void dismissProgressDialog();

        void refreshFriendList(ArrayList<Integer> userIds, ArrayList<String> names);    // 친구 목록 갱신

        void sendAddScheduleData(MemoryDAO memoryDAO);       // 일정 추가 데이터 전달

        void sendEditScheduleData(EachSchedulePostResult eachSchedulePostResult);       // 일정 수정 데이터 전달

        void sendDeleteScheduleData(EachSchedulePostResult eachSchedulePostResult);     // 일정 삭제 데이터 전달
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        GregorianCalendar getStartCalendar();                                           // 시작 날짜

        void setStartCalendar(int year, int month, int day, int hour, int minute);      // 시작 날짜 설정

        GregorianCalendar getEndCalendar();                                             // 종료 날짜

        void setEndCalendar(int year, int month, int day, int hour, int minute);        // 종료 날짜 설정

        String getCalendarMode();                                                       // 캘린더 모드

        void setCalendarMode(String calendarMode);                                      // 캘린더 모드 설정 (추가/편집)

        void setMemoryId(int mMemoryId);                                                // 일정 id 저장

        void setRoomId(int mRoomId);                                                    // 방 id 저장

        void initDate(String startDate, String endDate, int year, int month, int day);  // 날짜 계산

        int checkLastDay(int year, int month);                                          // 해당 월 마지막 날짜 계산

        boolean checkDate(GregorianCalendar startCalendar, GregorianCalendar endCalendar);  // 날짜 유효성 체크

        boolean checkAlarmCheckBox(ArrayList<CheckBox> checkBoxes);                     // 알람 체크박스 체크 여부

        String getCheckedAlarmText(ArrayList<CheckBox> checkBoxes);                     // 알람 체크박스 텍스트 가져오는 함수

        void createAddScheduleData(String title, String contents, String place, ArrayList<CheckBox> checkBoxes, String color);     // 스케쥴 데이터 클래스 생성 함수

        String calcStringAlarm(String alarmType, String startStr);                      // 알람 값 리턴

        void getAddScheduleResult(EachSchedulePostResult eachSchedulePostResult);       // 일정 추가 결과

        void getPutScheduleResult(EachSchedulePostResult eachSchedulePostResult);       // 일정 수정 결과

        void getDeleteScheduleData();                                                   // 일정 삭제 요청

        void getDeleteScheduleResult(EachSchedulePostResult eachSchedulePostResult);    // 일정 삭제 결과

        // 서버에서 친구 목록 가져오기
        void getFriendList();

        // 친구 목록 가져오기 결과
        void getFriendListResult(FriendPostResult friendPostResult);
    }
}
