package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.widget.CheckBox;

import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.util.DebugLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private final String TAG = AddSchedulePresenter.class.getSimpleName();

    private AddScheduleContract.View mView;

    private long mLastClickTime = 0;

    /*생성자*/
    public AddSchedulePresenter() {

    }

    @Override
    public void setView(AddScheduleContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 500) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public String[] initDate() {
        long now = System.currentTimeMillis();
        long startTime = now + (3600 * 1000);       // 시작 시간, 1시간을 더해준다
        long endTime = now + (7200 * 1000);         // 종료 시간, 2시간을 더해준다

        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd:kk:mm:E");
        String getStartTime = simpleDateFormat.format(startDate);
        String getEndTime = simpleDateFormat.format(endDate);

        return new String[]{getStartTime, getEndTime};
    }

    /**
     * 요일 계산 함수
     *
     * @return 요일 리턴
     */
    @Override
    public String calcDayOfWeek(String year, String month, String day) {
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);
        int totalDays = 0;
        totalDays += (yearInt - 1900) * 365;
        totalDays += (yearInt - 1900) / 4;

        if ((((yearInt % 4 == 0) && (yearInt % 100 != 0) || (yearInt % 400 == 0)) && (monthInt == 1 || monthInt == 2))) {
            // 윤년이고 1월 또는 2월이면 총일수 -1
            totalDays--;
        }

        switch (monthInt) {
            case 1:
                totalDays += dayInt;
                break;
            case 2:
                totalDays += 31 + dayInt;
                break;
            case 3:
                totalDays += 31 + 28 + dayInt;
                break;
            case 4:
                totalDays += 31 + 28 + 31 + dayInt;
                break;
            case 5:
                totalDays += 31 + 28 + 31 + 30 + dayInt;
                break;
            case 6:
                totalDays += 31 + 28 + 31 + 30 + 31 + dayInt;
                break;
            case 7:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + dayInt;
                break;
            case 8:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + 31 + dayInt;
                break;
            case 9:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + dayInt;
                break;
            case 10:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + dayInt;
                break;
            case 11:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + dayInt;
                break;
            case 12:
                totalDays += 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30 + dayInt;
                break;
        }

        switch (totalDays % 7) {
            case 0:
                return "일";
            case 1:
                return "월";
            case 2:
                return "화";
            case 3:
                return "수";
            case 4:
                return "목";
            case 5:
                return "금";
            case 6:
                return "토";
        }
        return null;
    }

    /**
     * 날짜 유효성 체크 함수
     *
     * @return 유효성 여부
     */
    @Override
    public boolean checkDate(String[] startDate, String[] endDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date1 = null;
        Date date2 = null;

        String startStr = startDate[0] + "-" + startDate[1] + "-" + startDate[2] + " " + startDate[3] + ":" + startDate[4];
        String endStr = endDate[0] + "-" + endDate[1] + "-" + endDate[2] + " " + endDate[3] + ":" + endDate[4];

        try {
            date1 = simpleDateFormat.parse(startStr);
            date2 = simpleDateFormat.parse(endStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int compare = date1.compareTo(date2);
        if (compare < 0) {
            // 유효한 값
            return true;
        } else {
            return false;
        }
    }

    /**
     * 알람 체크박스 체크 여부 확인
     *
     * @param checkBoxes 체크 박스 배열리스트
     * @return 결과값
     */
    @Override
    public boolean checkAlarmCheckBox(ArrayList<CheckBox> checkBoxes) {
        int count = 0;
        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                count++;
            }
        }
        if (count <= 2) {
            return true;
        }
        return false;
    }

    /**
     * 알람 체크박스 텍스트 가져오는 함수
     *
     * @param checkBoxes 체크 박스 배열리스트
     * @return String 값
     */
    @Override
    public String getCheckedAlarmText(ArrayList<CheckBox> checkBoxes) {
        String text = "";

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                if (text == "") {
                    text += checkBoxes.get(i).getText().toString();
                } else {
                    text += (", " + checkBoxes.get(i).getText().toString());
                }
            }
        }

        return text;
    }
}
