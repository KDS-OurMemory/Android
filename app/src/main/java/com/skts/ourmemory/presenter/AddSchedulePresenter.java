package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.widget.CheckBox;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.addschedule.AddScheduleModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddSchedulePresenter implements AddScheduleContract.Presenter {
    private final String TAG = AddSchedulePresenter.class.getSimpleName();

    private final AddScheduleContract.Model mModel;
    private AddScheduleContract.View mView;

    private long mLastClickTime = 0;

    MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*생성자*/
    public AddSchedulePresenter() {
        this.mModel = new AddScheduleModel(this);
    }

    @Override
    public void setView(AddScheduleContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
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
        // 유효한 값
        return compare < 0;
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
        return count <= 2;
    }

    /**
     * 알람 체크박스 텍스트 가져오는 함수
     *
     * @param checkBoxes 체크 박스 배열리스트
     * @return String 값
     */
    @Override
    public String getCheckedAlarmText(ArrayList<CheckBox> checkBoxes) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                if (text.toString().equals("")) {
                    text.append(checkBoxes.get(i).getText().toString());
                } else {
                    text.append(", ").append(checkBoxes.get(i).getText().toString());
                }
            }
        }

        return text.toString();
    }

    /**
     * 스케쥴 데이터 클래스 생성 함수
     *
     * @param title    제목
     * @param contents 내용
     * @param place    장소
     */
    @Override
    public void createAddScheduleData(String title, String contents, String place, String[] startDateList, String[] endDateList, ArrayList<CheckBox> checkBoxes, String color) {
        // 앞에서 필터링
        if (title.equals("")) {
            mView.showToast("일정 제목을 입력해주세요");
            return;
        }

        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        @SuppressLint("DefaultLocale")
        String startDate = startDateList[0] + "-" + String.format("%02d", Integer.parseInt(startDateList[1])) + "-" + String.format("%02d", Integer.parseInt(startDateList[2])) + " " + String.format("%02d", Integer.parseInt(startDateList[3])) + ":" + String.format("%02d", Integer.parseInt(startDateList[4]));
        @SuppressLint("DefaultLocale")
        String endDate = endDateList[0] + "-" + String.format("%02d", Integer.parseInt(endDateList[1])) + "-" + String.format("%02d", Integer.parseInt(endDateList[2])) + " " + String.format("%02d", Integer.parseInt(endDateList[3])) + ":" + String.format("%02d", Integer.parseInt(endDateList[4]));
        String firstAlarm = null;
        String secondAlarm = null;

        ArrayList<Integer> alarmArrayList = new ArrayList<>();

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isChecked()) {
                alarmArrayList.add(i);
            }
        }

        String endStr = endDateList[0] + "-" + endDateList[1] + "-" + endDateList[2] + " " + endDateList[3] + ":" + endDateList[4];

        if (alarmArrayList.size() == 2) {
            // 알람 체크 둘
            firstAlarm = calcStringAlarm(checkBoxes.get(alarmArrayList.get(0)).getText().toString(), endStr);
            secondAlarm = calcStringAlarm(checkBoxes.get(alarmArrayList.get(1)).getText().toString(), endStr);
        } else if (alarmArrayList.size() == 1) {
            // 알람 체크 하나
            firstAlarm = calcStringAlarm(checkBoxes.get(alarmArrayList.get(0)).getText().toString(), endStr);
        } /*else {
            // 알람 체크 없음
        }*/

        mModel.setAddScheduleData(userId, title, contents, place, startDate, endDate, firstAlarm, secondAlarm, color, mCompositeDisposable);
    }

    /**
     * 알람 스트링 값 계산
     *
     * @param alarmType 알람 유형
     * @param endStr    일정 종료 시간
     * @return 알람 String 리턴
     */
    @Override
    public String calcStringAlarm(String alarmType, String endStr) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;

        try {
            date = simpleDateFormat.parse(endStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (alarmType) {
            case Const.ALARM_ON_TIME:
                // 정시
                return simpleDateFormat.format(date.getTime());
            case Const.ALARM_5_MINUTES_AGO:
                // 5분 전
                return simpleDateFormat.format(date.getTime() - 5 * 60 * 1000);
            case Const.ALARM_10_MINUTES_AGO:
                // 10분 전
                return simpleDateFormat.format(date.getTime() - 10 * 60 * 1000);
            case Const.ALARM_15_MINUTES_AGO:
                // 15분 전
                return simpleDateFormat.format(date.getTime() - 15 * 60 * 1000);
            case Const.ALARM_30_MINUTES_AGO:
                // 30분 전
                return simpleDateFormat.format(date.getTime() - 30 * 60 * 1000);
            case Const.ALARM_1_HOUR_AGO:
                // 1시간 전
                return simpleDateFormat.format(date.getTime() - 60 * 60 * 1000);
            case Const.ALARM_2_HOURS_AGO:
                // 2시간 전
                return simpleDateFormat.format(date.getTime() - 2 * 60 * 60 * 1000);
            case Const.ALARM_3_HOURS_AGO:
                // 3시간 전
                return simpleDateFormat.format(date.getTime() - 3 * 60 * 60 * 1000);
            case Const.ALARM_12_HOURS_AGO:
                // 12시간 전
                return simpleDateFormat.format(date.getTime() - 12 * 60 * 60 * 1000);
            case Const.ALARM_1_DAY_AGO:
                // 1일 전
                return simpleDateFormat.format(date.getTime() - 24 * 60 * 60 * 1000);
            case Const.ALARM_2_DAYS_AGO:
                // 2일 전
                return simpleDateFormat.format(date.getTime() - 2 * 24 * 60 * 60 * 1000);
            case Const.ALARM_1_WEEK_AGO:
                // 1주 전
                return simpleDateFormat.format(date.getTime() - 7 * 24 * 60 * 60 * 1000);
            default:
                DebugLog.e(TAG, "ALARM ERROR");
                return "";
        }
    }

    /**
     * 일정 추가 실패 - 서버 통신 실패
     */
    @Override
    public void getAddScheduleResultFail() {
        mView.dismissProgressDialog();
        mView.showToast("일정 추가 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    /**
     * 서버 통신 성공
     *
     * @param resultCode 결과 코드
     * @param message    메시지
     * @param memoryId   일정 번호
     * @param roomId     방 번호
     * @param addDate    일정 추가 날짜
     */
    @Override
    public void getAddScheduleResultSuccess(String resultCode, String message, int memoryId, int roomId, String addDate) {
        mView.dismissProgressDialog();

        if (resultCode.equals(ServerConst.SUCCESS)) {
            // Success
            mView.showToast("일정 등록 성공");
            mView.onBackPressed();
        } else {
            // fail
            mView.showToast(message);
        }
    }
}
