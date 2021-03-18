package com.skts.ourmemory.contract;

import android.widget.CheckBox;

import com.skts.ourmemory.BaseContract;

import java.util.ArrayList;

public class AddScheduleContract {

    public interface View extends BaseContract.View {
        void initDateView();                // 초기 날짜 설정
        
        void initAlarmView();               // 초기 알람 설정

        void initColor();                   // 초기 색상 설정
        
        void setDateTextView();             // 스크롤 시 변경 날짜 적용
        
        void displayDialogAlarmText();      // 다이얼로그에 알람 텍스트 보여주는 함수

        void displayAlarmText();            // 메인에 알람 텍스트 보여주는 함수
        
        void setColor();                    // 선택한 색상 적용함수
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        @Override
        boolean isDuplicate();

        String[] initDate();            // 날짜 계산
        
        String calcDayOfWeek(String year, String month, String day);        // 요일 계산

        boolean checkDate(String[] startDate, String[] endDate);            // 날짜 유효성 체크

        boolean checkAlarmCheckBox(ArrayList<CheckBox> checkBoxes);         // 알람 체크박스 체크 여부
        
        String getCheckedAlarmText(ArrayList<CheckBox> checkBoxes);         // 알람 체크박스 텍스트 가져오는 함수
    }
}
