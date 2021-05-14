package com.skts.ourmemory.contract;

import android.content.Context;
import android.widget.CheckBox;

import androidx.appcompat.app.AlertDialog;

import com.skts.ourmemory.BaseContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddScheduleContract {

    public interface Model extends BaseContract.Model {
        // 일정 추가 요청
        void setAddScheduleData(int userId, String name, List<Integer> members, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, List<Integer> shareRooms, CompositeDisposable compositeDisposable);
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void initDateView();                // 초기 날짜 설정

        void initAlarmView();               // 초기 알람 설정

        void initColor();                   // 초기 색상 설정

        void setDateTextView();             // 스크롤 시 변경 날짜 적용

        void displayDialogAlarmText();      // 다이얼로그에 알람 텍스트 보여주는 함수

        void displayAlarmText();            // 메인에 알람 텍스트 보여주는 함수

        void setColor(AlertDialog mAlertDialog);                    // 선택한 색상 적용함수

        void showToast(String message);

        void onBackPressed();

        Context getAppContext();            // Context 리턴

        void dismissProgressDialog();

        void refreshFriendList(ArrayList<Integer> userIds, ArrayList<String> names);           // 친구 목록 갱신
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        String[] initDate();                // 날짜 계산

        String calcDayOfWeek(String year, String month, String day);            // 요일 계산

        boolean checkDate(String[] startDate, String[] endDate);                // 날짜 유효성 체크

        boolean checkAlarmCheckBox(ArrayList<CheckBox> checkBoxes);             // 알람 체크박스 체크 여부

        String getCheckedAlarmText(ArrayList<CheckBox> checkBoxes);             // 알람 체크박스 텍스트 가져오는 함수

        void createAddScheduleData(String title, List<Integer> members, String contents, String place, String[] startDateList, String[] endDateList, ArrayList<CheckBox> checkBoxes, String color, List<Integer> shareRooms);     // 스케쥴 데이터 클래스 생성 함수

        String calcStringAlarm(String alarmType, String endStr);                // 알람 값 리턴

        // 일정 추가 실패
        void getAddScheduleResultFail();

        // 일정 추가 성공
        void getAddScheduleResultSuccess(String resultCode, String message, int memoryId, int roomId, String addDate);

        // 서버에서 친구 목록 가져오기
        void getFriendList();

        // 친구 목록 가져오기 실패
        void getFriendListResultFail();

        // 친구 목록 가져오기 성공
        void getFriendListResultSuccess(String resultCode, String message, ArrayList<Integer> userIds, ArrayList<String> names);
    }
}
