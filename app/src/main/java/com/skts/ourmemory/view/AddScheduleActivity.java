package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.model.room.ShareRoom;
import com.skts.ourmemory.presenter.AddSchedulePresenter;
import com.skts.ourmemory.view.share.ShareActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class AddScheduleActivity extends BaseActivity implements AddScheduleContract.View {
    private AddScheduleContract.Presenter mAddSchedulePresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_schedule)
    Toolbar mToolbar;                                   // 툴바
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_title)
    EditText mTitleEditText;                            // 일정 제목
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_schedule_start_time)
    TextView mStartTimeTextView;                        // 시작날짜
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_schedule_end_time)
    TextView mEndTimeTextView;                          // 종료날짜
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_content)
    EditText mContentEditText;                          // 내용
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_place)
    EditText mPlaceEditText;                            // 장소
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_alarm)
    EditText mAlarmEditText;                            // 알람
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_color)
    EditText mColorEditText;                            // 색상
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_add_room)
    EditText mAddRoomEditText;                          // 공유
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_activity_add_schedule_delete)
    ImageView mDeleteImageBtn;                          // 삭제

    // 다이얼로그 View
    private View mDateView;
    private View mAlarmView;
    private View mColorView;
    private View mShareView;

    // 다이얼로그 내 TextView
    private TextView mStartDateDialogText;              // 시작일
    private TextView mEndDateDialogText;                // 종료일
    private NumberPicker mYearNumberDialogPicker;       // 년
    private NumberPicker mMonthNumberDialogPicker;      // 월
    private NumberPicker mDayNumberDialogPicker;        // 일
    private NumberPicker mHourNumberDialogPicker;       // 시
    private NumberPicker mMinutesNumberDialogPicker;    // 분

    // 다이얼로그
    AlertDialog mAlertDialog = null;
    ProgressDialog mProgressDialog;

    // 알람 관련, 다이얼로그 내 CheckBox
    private CheckBox mOnTimeCheckBox;                   // 정시
    private CheckBox m5MinutesAgoCheckBox;              // 5분 전
    private CheckBox m10MinutesAgoCheckBox;             // 10분 전
    private CheckBox m15MinutesAgoCheckBox;             // 15분 전
    private CheckBox m30MinutesAgoCheckBox;             // 30분 전
    private CheckBox m1HourAgoCheckBox;                 // 1시간 전
    private CheckBox m2HourAgoCheckBox;                 // 2시간 전
    private CheckBox m3HourAgoCheckBox;                 // 3시간 전
    private CheckBox m12HourAgoCheckBox;                // 12시간 전
    private CheckBox m1DayAgoCheckBox;                  // 1일 전
    private CheckBox m2DaysAgoCheckBox;                 // 2일 전
    private CheckBox m1WeekAgoCheckBox;                 // 1주 전

    // 다이얼로그 내 TextView
    private TextView mAlarmTextView;                    // 알람 텍스트뷰

    // 체크박스 리스트
    private ArrayList<CheckBox> mCheckBoxes;
    private String mColorStr;

    // 다이얼로그 내 ImageButton
    private ImageButton mColorImageBtn1;
    private ImageButton mColorImageBtn2;
    private ImageButton mColorImageBtn3;
    private ImageButton mColorImageBtn4;
    private ImageButton mColorImageBtn5;
    private ImageButton mColorImageBtn6;
    private ImageButton mColorImageBtn7;
    private ImageButton mColorImageBtn8;
    private ImageButton mColorImageBtn9;
    private ImageButton mColorImageBtn10;
    private ImageButton mColorImageBtn11;
    private ImageButton mColorImageBtn12;
    private ImageButton mColorImageBtn13;
    private ImageButton mColorImageBtn14;
    private ImageButton mColorImageBtn15;

    // 친구 관련
    private ArrayList<String> mFriendList;
    private List<Integer> mFriendNumberList;
    private List<Integer> mSelectFriendNumberList;
    private List<Integer> mShareRoomNumberList;
    private ImageView mRefreshBtn;
    private ImageView mCloseBtn;
    private ImageView mCheckBtn;
    private ListView mFriendListView;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        mAddSchedulePresenter.releaseView();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Const.REQUEST_CODE_SHARE_CALENDAR) {
                List<Integer> shareIntList = (List<Integer>) data.getExtras().getSerializable(Const.SHARE_DATA_ID);
                List<String> shareStringList = (List<String>) data.getExtras().getSerializable(Const.SHARE_DATA_NAME);
                String shareType = data.getStringExtra(Const.SHARE_DATA_TYPE);
                StringBuilder sb = new StringBuilder();
                sb.append(shareStringList.get(0));
                for (int i = 1; i < shareStringList.size(); i++) {
                    sb.append(", ").append(shareStringList.get(i));
                }

                // 일정 공유 리스트 id 저장
                mAddSchedulePresenter.setShareList(shareIntList);

                // 공유 타입 저장
                mAddSchedulePresenter.setShareType(shareType);
                mAddRoomEditText.setText(sb);
            }
        }
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void initSet() {
        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mAddSchedulePresenter = new AddSchedulePresenter();
        mAddSchedulePresenter.setView(this);

        mCheckBoxes = new ArrayList<>();
        // 공유
        mFriendNumberList = new ArrayList<>();
        mShareRoomNumberList = new ArrayList<>();
        mSelectFriendNumberList = new ArrayList<>();

        initBinding();

        Intent intent = getIntent();
        MemoryDAO memoryDAO = (MemoryDAO) intent.getSerializableExtra(Const.CALENDAR_DATA);
        int roomId = intent.getIntExtra(Const.ROOM_ID, -1);
        int selectYear = intent.getIntExtra(Const.CALENDAR_YEAR, -1);
        int selectMonth = intent.getIntExtra(Const.CALENDAR_MONTH, -1);
        int selectDay = intent.getIntExtra(Const.CALENDAR_DAY, -1);
        mAddSchedulePresenter.setCalendarMode(intent.getStringExtra(Const.CALENDAR_PURPOSE));

        mAddSchedulePresenter.setRoomId(roomId);                            // 방 id
        if (memoryDAO != null) {
            mAddSchedulePresenter.setMemoryId(memoryDAO.getMemoryId());     // 일정 id
            mAddSchedulePresenter.setMemoryName(memoryDAO.getName());       // 일정 이름
            mTitleEditText.setText(memoryDAO.getName());                    // 일정 제목
            initDateView(memoryDAO.getStartDate(), memoryDAO.getEndDate(), selectYear, selectMonth, selectDay);     // 날짜
            mContentEditText.setText(memoryDAO.getContents());              // 내용
            mPlaceEditText.setText(memoryDAO.getPlace());                   // 장소
            setAlarmView(memoryDAO.getStartDate(), memoryDAO.getFirstAlarm(), memoryDAO.getSecondAlarm());          // 알람
            setColorView(memoryDAO.getBgColor());                           // 색상
            mDeleteImageBtn.setVisibility(View.VISIBLE);

            // 공유
            List<ShareRoom> shareRoomList = memoryDAO.getShareRooms();
            if (shareRoomList != null && shareRoomList.size() != 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(shareRoomList.get(0).getName());
                mShareRoomNumberList.add(shareRoomList.get(0).getRoomId());
                for (int i = 1; i < shareRoomList.size(); i++) {
                    ShareRoom shareRoom = shareRoomList.get(i);
                    sb.append(", ").append(shareRoom.getName());
                    mShareRoomNumberList.add(shareRoom.getRoomId());
                }
                mAddRoomEditText.setText(sb.toString());
            }
        } else {
            initDateView(null, null, selectYear, selectMonth, selectDay);       // 날짜
            initAlarmView();            // 기본 알람 설정
            initColor();                // 기본 색상 설정
            //mDeleteImageBtn.setVisibility(View.GONE);
        }
    }

    @SuppressLint("InflateParams")
    @Override
    public void initBinding() {
        // 다이얼로그 뷰
        mDateView = getLayoutInflater().inflate(R.layout.dialog_date, null, false);
        mAlarmView = getLayoutInflater().inflate(R.layout.dialog_alarm, null, false);
        mColorView = getLayoutInflater().inflate(R.layout.dialog_color, null, false);
        mShareView = getLayoutInflater().inflate(R.layout.dialog_friend, null, false);

        // 다이얼로그 뷰 내 텍스트뷰
        mStartDateDialogText = mDateView.findViewById(R.id.tv_dialog_date_start_date);
        mEndDateDialogText = mDateView.findViewById(R.id.tv_dialog_date_end_date);
        mYearNumberDialogPicker = mDateView.findViewById(R.id.np_dialog_date_year);
        mMonthNumberDialogPicker = mDateView.findViewById(R.id.np_dialog_date_month);
        mDayNumberDialogPicker = mDateView.findViewById(R.id.np_dialog_date_day);
        mHourNumberDialogPicker = mDateView.findViewById(R.id.np_dialog_date_hour);
        mMinutesNumberDialogPicker = mDateView.findViewById(R.id.np_dialog_date_minutes);

        mOnTimeCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_on_time);
        m5MinutesAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_5_minutes_ago);
        m10MinutesAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_10_minutes_ago);
        m15MinutesAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_15_minutes_ago);
        m30MinutesAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_30_minutes_ago);
        m1HourAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_1_hour_ago);
        m2HourAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_2_hours_ago);
        m3HourAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_3_hours_ago);
        m12HourAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_12_hours_ago);
        m1DayAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_1_day_ago);
        m2DaysAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_2_days_ago);
        m1WeekAgoCheckBox = mAlarmView.findViewById(R.id.cb_dialog_alarm_1_week_ago);
        mAlarmTextView = mAlarmView.findViewById(R.id.tv_dialog_alarm_text_view);

        mColorImageBtn1 = mColorView.findViewById(R.id.ib_dialog_color_color1);
        mColorImageBtn2 = mColorView.findViewById(R.id.ib_dialog_color_color2);
        mColorImageBtn3 = mColorView.findViewById(R.id.ib_dialog_color_color3);
        mColorImageBtn4 = mColorView.findViewById(R.id.ib_dialog_color_color4);
        mColorImageBtn5 = mColorView.findViewById(R.id.ib_dialog_color_color5);
        mColorImageBtn6 = mColorView.findViewById(R.id.ib_dialog_color_color6);
        mColorImageBtn7 = mColorView.findViewById(R.id.ib_dialog_color_color7);
        mColorImageBtn8 = mColorView.findViewById(R.id.ib_dialog_color_color8);
        mColorImageBtn9 = mColorView.findViewById(R.id.ib_dialog_color_color9);
        mColorImageBtn10 = mColorView.findViewById(R.id.ib_dialog_color_color10);
        mColorImageBtn11 = mColorView.findViewById(R.id.ib_dialog_color_color11);
        mColorImageBtn12 = mColorView.findViewById(R.id.ib_dialog_color_color12);
        mColorImageBtn13 = mColorView.findViewById(R.id.ib_dialog_color_color13);
        mColorImageBtn14 = mColorView.findViewById(R.id.ib_dialog_color_color14);
        mColorImageBtn15 = mColorView.findViewById(R.id.ib_dialog_color_color15);

        mCheckBoxes.add(mOnTimeCheckBox);
        mCheckBoxes.add(m5MinutesAgoCheckBox);
        mCheckBoxes.add(m10MinutesAgoCheckBox);
        mCheckBoxes.add(m15MinutesAgoCheckBox);
        mCheckBoxes.add(m30MinutesAgoCheckBox);
        mCheckBoxes.add(m1HourAgoCheckBox);
        mCheckBoxes.add(m2HourAgoCheckBox);
        mCheckBoxes.add(m3HourAgoCheckBox);
        mCheckBoxes.add(m12HourAgoCheckBox);
        mCheckBoxes.add(m1DayAgoCheckBox);
        mCheckBoxes.add(m2DaysAgoCheckBox);
        mCheckBoxes.add(m1WeekAgoCheckBox);

        mFriendListView = mShareView.findViewById(R.id.lv_dialog_friend_list_view);
        mRefreshBtn = mShareView.findViewById(R.id.iv_dialog_friend_refresh_button);
        mCloseBtn = mShareView.findViewById(R.id.iv_dialog_friend_close_button);
        mCheckBtn = mShareView.findViewById(R.id.iv_dialog_friend_check_button);
    }

    /**
     * 초기 날짜 설정 함수
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void initDateView(String startDate, String endDate, int selectYear, int selectMonth, int selectDay) {
        final int MIN_YEAR = 1900;
        final int MAX_YEAR = 2100;
        final int MIN_MONTH = 1;
        final int MAX_MONTH = 12;
        final int MIN_DAY = 1;
        final int MAX_DAY = 31;
        final int MIN_HOUR = 0;
        final int MAX_HOUR = 23;
        final int MIN_MINUTE = 0;
        final int MAX_MINUTE = 59;

        // 년
        mYearNumberDialogPicker.setMinValue(MIN_YEAR);
        mYearNumberDialogPicker.setMaxValue(MAX_YEAR);
        mYearNumberDialogPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 월
        mMonthNumberDialogPicker.setMinValue(MIN_MONTH);
        mMonthNumberDialogPicker.setMaxValue(MAX_MONTH);
        mMonthNumberDialogPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 일
        mDayNumberDialogPicker.setMinValue(MIN_DAY);
        mDayNumberDialogPicker.setMaxValue(MAX_DAY);
        mDayNumberDialogPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 시간
        mHourNumberDialogPicker.setMinValue(MIN_HOUR);
        mHourNumberDialogPicker.setMaxValue(MAX_HOUR);
        mHourNumberDialogPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 분
        mMinutesNumberDialogPicker.setMinValue(MIN_MINUTE);
        mMinutesNumberDialogPicker.setMaxValue(MAX_MINUTE);
        mMinutesNumberDialogPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mAddSchedulePresenter.initDate(startDate, endDate, selectYear, selectMonth, selectDay);             // 날짜 설정
        setTimeTextView();
        setDateTextView();
    }

    @Override
    public void setTimeTextView() {
        GregorianCalendar startCalendar = mAddSchedulePresenter.getStartCalendar();
        GregorianCalendar endCalendar = mAddSchedulePresenter.getEndCalendar();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 M월 d일 E요일 HH:mm");

        mStartTimeTextView.setText(simpleDateFormat.format(startCalendar.getTime()));
        mEndTimeTextView.setText(simpleDateFormat.format(endCalendar.getTime()));
    }

    /**
     * 스크롤 시 변경 날짜 적용 함수
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void setDateTextView() {
        GregorianCalendar startCalendar = mAddSchedulePresenter.getStartCalendar();
        GregorianCalendar endCalendar = mAddSchedulePresenter.getEndCalendar();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 M월 d일\n(E) HH:mm");

        mStartDateDialogText.setText(simpleDateFormat.format(startCalendar.getTime()));
        mEndDateDialogText.setText(simpleDateFormat.format(endCalendar.getTime()));
    }

    @Override
    public void setAlarmView(String startDate, String firstAlarm, String secondAlarm) {
        if (firstAlarm == null) {
            mAlarmEditText.setText("알람 없음");
            return;
        }

        String str = calcAlarm(startDate, firstAlarm);        // 첫 번째 알람
        if (secondAlarm != null) {
            str = str + ", " + calcAlarm(startDate, secondAlarm);         // 두 번째 알람
        }
        mAlarmEditText.setText(str);
    }

    @Override
    public String calcAlarm(String date1, String date2) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        int diff = 0;
        try {
            Date firstDate = simpleDateFormat.parse(date1);
            Date secondDate = simpleDateFormat.parse(date2);

            diff = (int) ((firstDate.getTime() - secondDate.getTime()) / 60000);       // 분 차이
        } catch (ParseException e) {
            e.printStackTrace();
        }

        switch (diff) {
            case 0:
                mOnTimeCheckBox.setChecked(true);
                return "정시";
            case 5:
                m5MinutesAgoCheckBox.setChecked(true);
                return "5분 전";
            case 10:
                m10MinutesAgoCheckBox.setChecked(true);
                return "10분 전";
            case 15:
                m15MinutesAgoCheckBox.setChecked(true);
                return "15분 전";
            case 30:
                m30MinutesAgoCheckBox.setChecked(true);
                return "30분 전";
            case 60:
                m1HourAgoCheckBox.setChecked(true);
                return "1시간 전";
            case 120:
                m2HourAgoCheckBox.setChecked(true);
                return "2시간 전";
            case 180:
                m3HourAgoCheckBox.setChecked(true);
                return "3시간 전";
            case 720:
                m12HourAgoCheckBox.setChecked(true);
                return "12시간 전";
            case 1440:
                m1DayAgoCheckBox.setChecked(true);
                return "1일 전";
            case 2880:
                m2DaysAgoCheckBox.setChecked(true);
                return "2일 전";
            case 10080:
                m1WeekAgoCheckBox.setChecked(true);
                return "1주 전";
        }

        return "";
    }

    @Override
    public void setColorView(String color) {
        mColorStr = color;
        mColorEditText.setTextColor(Color.parseColor(color));
    }

    /**
     * 기본 알람 설정
     */
    @Override
    public void initAlarmView() {
        m10MinutesAgoCheckBox.setChecked(true);     // 기본 값 10분 전 알람 적용
        displayDialogAlarmText();                   // 다이얼로그 알람
        displayAlarmText();                         // 메인 알람
    }

    /**
     * 기본 색상 설정
     */
    @Override
    public void initColor() {
        // 랜덤 색상 적용
        Random random = new Random();
        switch (random.nextInt(15)) {
            case 0:
                mColorEditText.setTextColor(getColor(R.color.color1));
                mColorStr = "#ffab91";
                break;
            case 1:
                mColorEditText.setTextColor(getColor(R.color.color2));
                mColorStr = "#f48fb1";
                break;
            case 2:
                mColorEditText.setTextColor(getColor(R.color.color3));
                mColorStr = "#ce93d8";
                break;
            case 3:
                mColorEditText.setTextColor(getColor(R.color.color4));
                mColorStr = "#b39ddb";
                break;
            case 4:
                mColorEditText.setTextColor(getColor(R.color.color5));
                mColorStr = "#9fa8da";
                break;
            case 5:
                mColorEditText.setTextColor(getColor(R.color.color6));
                mColorStr = "#90caf9";
                break;
            case 6:
                mColorEditText.setTextColor(getColor(R.color.color7));
                mColorStr = "#81d4fa";
                break;
            case 7:
                mColorEditText.setTextColor(getColor(R.color.color8));
                mColorStr = "#80deea";
                break;
            case 8:
                mColorEditText.setTextColor(getColor(R.color.color9));
                mColorStr = "#80cbc4";
                break;
            case 9:
                mColorEditText.setTextColor(getColor(R.color.color10));
                mColorStr = "#c5e1a5";
                break;
            case 10:
                mColorEditText.setTextColor(getColor(R.color.color11));
                mColorStr = "#e6ee9c";
                break;
            case 11:
                mColorEditText.setTextColor(getColor(R.color.color12));
                mColorStr = "#ffd700";
                break;
            case 12:
                mColorEditText.setTextColor(getColor(R.color.color13));
                mColorStr = "#ffe082";
                break;
            case 13:
                mColorEditText.setTextColor(getColor(R.color.color14));
                mColorStr = "#ffcc80";
                break;
            case 14:
                mColorEditText.setTextColor(getColor(R.color.color15));
                mColorStr = "#bcaaa4";
                break;
        }
    }

    @Override
    public void setStartCalendarValue() {
        GregorianCalendar startCalendar = mAddSchedulePresenter.getStartCalendar();

        mYearNumberDialogPicker.setValue(startCalendar.get(Calendar.YEAR));
        mMonthNumberDialogPicker.setValue(startCalendar.get(Calendar.MONTH) + 1);
        mDayNumberDialogPicker.setValue(startCalendar.get(Calendar.DAY_OF_MONTH));
        mHourNumberDialogPicker.setValue(startCalendar.get(Calendar.HOUR_OF_DAY));
        mMinutesNumberDialogPicker.setValue(startCalendar.get(Calendar.MINUTE));

        mYearNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            int lastDay = mAddSchedulePresenter.checkLastDay(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue());
            mDayNumberDialogPicker.setMaxValue(lastDay);

            GregorianCalendar calendar = mAddSchedulePresenter.getStartCalendar();
            GregorianCalendar calendar2 = mAddSchedulePresenter.getEndCalendar();

            int yearDiff = mYearNumberDialogPicker.getValue() - calendar.get(Calendar.YEAR);
            int monthDiff = mMonthNumberDialogPicker.getValue() - (calendar.get(Calendar.MONTH) + 1);
            int dayDiff = mDayNumberDialogPicker.getValue() - calendar.get(Calendar.DAY_OF_MONTH);
            int hourDiff = mHourNumberDialogPicker.getValue() - calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDiff = mMinutesNumberDialogPicker.getValue() - calendar.get(Calendar.MINUTE);

            mAddSchedulePresenter.setStartCalendar(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue() - 1, mDayNumberDialogPicker.getValue(), mHourNumberDialogPicker.getValue(), mMinutesNumberDialogPicker.getValue());

            mAddSchedulePresenter.setEndCalendar(calendar2.get(Calendar.YEAR) + yearDiff, calendar2.get(Calendar.MONTH) + monthDiff, calendar2.get(Calendar.DAY_OF_MONTH) + dayDiff, calendar2.get(Calendar.HOUR_OF_DAY) + hourDiff, calendar2.get(Calendar.MINUTE) + minuteDiff);
            setDateTextView();
        });

        mMonthNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            int lastDay = mAddSchedulePresenter.checkLastDay(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue());
            mDayNumberDialogPicker.setMaxValue(lastDay);

            GregorianCalendar calendar = mAddSchedulePresenter.getStartCalendar();
            GregorianCalendar calendar2 = mAddSchedulePresenter.getEndCalendar();

            int yearDiff = mYearNumberDialogPicker.getValue() - calendar.get(Calendar.YEAR);
            int monthDiff = mMonthNumberDialogPicker.getValue() - (calendar.get(Calendar.MONTH) + 1);
            int dayDiff = mDayNumberDialogPicker.getValue() - calendar.get(Calendar.DAY_OF_MONTH);
            int hourDiff = mHourNumberDialogPicker.getValue() - calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDiff = mMinutesNumberDialogPicker.getValue() - calendar.get(Calendar.MINUTE);

            mAddSchedulePresenter.setStartCalendar(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue() - 1, mDayNumberDialogPicker.getValue(), mHourNumberDialogPicker.getValue(), mMinutesNumberDialogPicker.getValue());

            mAddSchedulePresenter.setEndCalendar(calendar2.get(Calendar.YEAR) + yearDiff, calendar2.get(Calendar.MONTH) + monthDiff, calendar2.get(Calendar.DAY_OF_MONTH) + dayDiff, calendar2.get(Calendar.HOUR_OF_DAY) + hourDiff, calendar2.get(Calendar.MINUTE) + minuteDiff);
            setDateTextView();
        });

        mDayNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getStartCalendar();
            GregorianCalendar calendar2 = mAddSchedulePresenter.getEndCalendar();

            int yearDiff = mYearNumberDialogPicker.getValue() - calendar.get(Calendar.YEAR);
            int monthDiff = mMonthNumberDialogPicker.getValue() - (calendar.get(Calendar.MONTH) + 1);
            int dayDiff = mDayNumberDialogPicker.getValue() - calendar.get(Calendar.DAY_OF_MONTH);
            int hourDiff = mHourNumberDialogPicker.getValue() - calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDiff = mMinutesNumberDialogPicker.getValue() - calendar.get(Calendar.MINUTE);

            mAddSchedulePresenter.setStartCalendar(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue() - 1, mDayNumberDialogPicker.getValue(), mHourNumberDialogPicker.getValue(), mMinutesNumberDialogPicker.getValue());

            mAddSchedulePresenter.setEndCalendar(calendar2.get(Calendar.YEAR) + yearDiff, calendar2.get(Calendar.MONTH) + monthDiff, calendar2.get(Calendar.DAY_OF_MONTH) + dayDiff, calendar2.get(Calendar.HOUR_OF_DAY) + hourDiff, calendar2.get(Calendar.MINUTE) + minuteDiff);
            setDateTextView();
        });

        mHourNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getStartCalendar();
            GregorianCalendar calendar2 = mAddSchedulePresenter.getEndCalendar();

            int yearDiff = mYearNumberDialogPicker.getValue() - calendar.get(Calendar.YEAR);
            int monthDiff = mMonthNumberDialogPicker.getValue() - (calendar.get(Calendar.MONTH) + 1);
            int dayDiff = mDayNumberDialogPicker.getValue() - calendar.get(Calendar.DAY_OF_MONTH);
            int hourDiff = mHourNumberDialogPicker.getValue() - calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDiff = mMinutesNumberDialogPicker.getValue() - calendar.get(Calendar.MINUTE);

            mAddSchedulePresenter.setStartCalendar(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue() - 1, mDayNumberDialogPicker.getValue(), mHourNumberDialogPicker.getValue(), mMinutesNumberDialogPicker.getValue());

            mAddSchedulePresenter.setEndCalendar(calendar2.get(Calendar.YEAR) + yearDiff, calendar2.get(Calendar.MONTH) + monthDiff, calendar2.get(Calendar.DAY_OF_MONTH) + dayDiff, calendar2.get(Calendar.HOUR_OF_DAY) + hourDiff, calendar2.get(Calendar.MINUTE) + minuteDiff);
            setDateTextView();
        });

        mMinutesNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getStartCalendar();
            GregorianCalendar calendar2 = mAddSchedulePresenter.getEndCalendar();

            int yearDiff = mYearNumberDialogPicker.getValue() - calendar.get(Calendar.YEAR);
            int monthDiff = mMonthNumberDialogPicker.getValue() - (calendar.get(Calendar.MONTH) + 1);
            int dayDiff = mDayNumberDialogPicker.getValue() - calendar.get(Calendar.DAY_OF_MONTH);
            int hourDiff = mHourNumberDialogPicker.getValue() - calendar.get(Calendar.HOUR_OF_DAY);
            int minuteDiff = mMinutesNumberDialogPicker.getValue() - calendar.get(Calendar.MINUTE);

            mAddSchedulePresenter.setStartCalendar(mYearNumberDialogPicker.getValue(), mMonthNumberDialogPicker.getValue() - 1, mDayNumberDialogPicker.getValue(), mHourNumberDialogPicker.getValue(), mMinutesNumberDialogPicker.getValue());

            mAddSchedulePresenter.setEndCalendar(calendar2.get(Calendar.YEAR) + yearDiff, calendar2.get(Calendar.MONTH) + monthDiff, calendar2.get(Calendar.DAY_OF_MONTH) + dayDiff, calendar2.get(Calendar.HOUR_OF_DAY) + hourDiff, calendar2.get(Calendar.MINUTE) + minuteDiff);
            setDateTextView();
        });
    }

    @Override
    public void setEndCalendarValue() {
        GregorianCalendar endCalendar = mAddSchedulePresenter.getEndCalendar();

        mYearNumberDialogPicker.setValue(endCalendar.get(Calendar.YEAR));
        mMonthNumberDialogPicker.setValue(endCalendar.get(Calendar.MONTH) + 1);
        mDayNumberDialogPicker.setValue(endCalendar.get(Calendar.DAY_OF_MONTH));
        mHourNumberDialogPicker.setValue(endCalendar.get(Calendar.HOUR_OF_DAY));
        mMinutesNumberDialogPicker.setValue(endCalendar.get(Calendar.MINUTE));

        mYearNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getEndCalendar();
            mAddSchedulePresenter.setEndCalendar(mYearNumberDialogPicker.getValue(), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            setDateTextView();
        });

        mMonthNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getEndCalendar();
            mAddSchedulePresenter.setEndCalendar(calendar.get(Calendar.YEAR), mMonthNumberDialogPicker.getValue() - 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            setDateTextView();
        });

        mDayNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getEndCalendar();
            mAddSchedulePresenter.setEndCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), mDayNumberDialogPicker.getValue(), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
            setDateTextView();
        });

        mHourNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getEndCalendar();
            mAddSchedulePresenter.setEndCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), mHourNumberDialogPicker.getValue(), calendar.get(Calendar.MINUTE));
            setDateTextView();
        });

        mMinutesNumberDialogPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            GregorianCalendar calendar = mAddSchedulePresenter.getEndCalendar();
            mAddSchedulePresenter.setEndCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), mMinutesNumberDialogPicker.getValue());
            setDateTextView();
        });
    }

    /**
     * 다이얼로그에 알람 텍스트 보여주는 함수
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void displayDialogAlarmText() {
        String str = mAddSchedulePresenter.getCheckedAlarmText(mCheckBoxes);
        if (str.equals("")) {
            mAlarmTextView.setText("알람이 없습니다.");
        } else {
            mAlarmTextView.setText(str + " 알람이 도착합니다.");
        }
    }

    /**
     * 메인에 알람 텍스트 보여주는 함수
     */
    @Override
    public void displayAlarmText() {
        String str = mAddSchedulePresenter.getCheckedAlarmText(mCheckBoxes);
        if (str.equals("")) {
            mAlarmEditText.setText("알람 없음");
        } else {
            mAlarmEditText.setText(str);
        }
    }

    /**
     * 선택한 색상 적용 함수
     *
     * @param mAlertDialog 다이얼로그 창
     */
    @SuppressLint("NewApi")
    @Override
    public void setColor(AlertDialog mAlertDialog) {
        mColorImageBtn1.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color1));
            mColorStr = "#ffab91";
            mAlertDialog.dismiss();
        });

        mColorImageBtn2.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color2));
            mColorStr = "#f48fb1";
            mAlertDialog.dismiss();
        });

        mColorImageBtn3.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color3));
            mColorStr = "#ce93d8";
            mAlertDialog.dismiss();
        });

        mColorImageBtn4.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color4));
            mColorStr = "#b39ddb";
            mAlertDialog.dismiss();
        });

        mColorImageBtn5.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color5));
            mColorStr = "#9fa8da";
            mAlertDialog.dismiss();
        });

        mColorImageBtn6.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color6));
            mColorStr = "#90caf9";
            mAlertDialog.dismiss();
        });

        mColorImageBtn7.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color7));
            mColorStr = "#81d4fa";
            mAlertDialog.dismiss();
        });

        mColorImageBtn8.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color8));
            mColorStr = "#80deea";
            mAlertDialog.dismiss();
        });

        mColorImageBtn9.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color9));
            mColorStr = "#80cbc4";
            mAlertDialog.dismiss();
        });

        mColorImageBtn10.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color10));
            mColorStr = "#c5e1a5";
            mAlertDialog.dismiss();
        });

        mColorImageBtn11.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color11));
            mColorStr = "#e6ee9c";
            mAlertDialog.dismiss();
        });

        mColorImageBtn12.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color12));
            mColorStr = "#ffd700";
            mAlertDialog.dismiss();
        });

        mColorImageBtn13.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color13));
            mColorStr = "#ffe082";
            mAlertDialog.dismiss();
        });

        mColorImageBtn14.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color14));
            mColorStr = "#ffcc80";
            mAlertDialog.dismiss();
        });

        mColorImageBtn15.setOnClickListener(view -> {
            mColorEditText.setTextColor(getColor(R.color.color15));
            mColorStr = "#bcaaa4";
            mAlertDialog.dismiss();
        });
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void refreshFriendList(ArrayList<Integer> userIds, ArrayList<String> names) {
        // Reset
        mFriendNumberList.clear();
        mFriendList.clear();

        int friendsCount = mFriendList.size();
        if (friendsCount == 0) {
            mFriendNumberList.addAll(userIds);
            mFriendList.addAll(names);
            mArrayAdapter.notifyDataSetChanged();
            return;
        }

        boolean sameNameCheck = false;
        for (int i = 0; i < names.size(); i++) {
            for (int j = 0; j < friendsCount; j++) {
                if (names.get(i).equals(mFriendList.get(j))) {
                    sameNameCheck = true;
                    break;
                }
            }
            if (!sameNameCheck) {
                mFriendNumberList.add(userIds.get(i));
                mFriendList.add(names.get(i));
            }
        }
        mArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendAddScheduleData(MemoryDAO memoryDAO) {
        Intent intent = new Intent();
        if (memoryDAO == null) {            // 값이 없으면
            setResult(Const.RESULT_FAIL, intent);
        } else {
            intent.putExtra(Const.SCHEDULE_DATA, memoryDAO);
            intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_ADD);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void sendEditScheduleData(MemoryDAO memoryDAO) {
        Intent intent = new Intent();
        if (memoryDAO == null) {
            setResult(Const.RESULT_FAIL, intent);
        } else {
            intent.putExtra(Const.SCHEDULE_DATA, memoryDAO);
            intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_EDIT);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void sendDeleteScheduleData(MemoryDAO memoryDAO) {
        Intent intent = new Intent();
        intent.putExtra(Const.SCHEDULE_DATA, memoryDAO);
        intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_REMOVE);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void sendShareScheduleData(RoomResponseValue roomResponseValue, MemoryDAO memoryDAO, String mode) {
        Intent intent = new Intent();
        if (roomResponseValue == null) {            // 값이 없으면
            setResult(Const.RESULT_FAIL, intent);
        } else {
            intent.putExtra(Const.SCHEDULE_DATA, memoryDAO);
            intent.putExtra(Const.ROOM_DATA, roomResponseValue);
            if (mode.equals(Const.CALENDAR_ADD_AND_SHARE)) {
                // 일정 추가 및 공유
                intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_ADD_AND_SHARE);
            } else {
                // 일정 수정 및 공유
                intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_EDIT_AND_SHARE);
            }
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    /**
     * 시작 날짜 다이얼로그
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.ll_activity_add_schedule_start_time)
    void onClickStartTimeDialog() {
        // 시작날짜 선택
        mStartDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
        mEndDateDialogText.setBackground(null);

        setStartCalendarValue();

        setDateTextView();

        // 다이얼로그 창
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(mDateView)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null);
        mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(dialogInterface -> {
            Button button = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button button1 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            button.setOnClickListener(view -> {
                GregorianCalendar startCalendar = mAddSchedulePresenter.getStartCalendar();
                GregorianCalendar endCalendar = mAddSchedulePresenter.getEndCalendar();

                if (mAddSchedulePresenter.checkDate(startCalendar, endCalendar)) {
                    setTimeTextView();
                    mAlertDialog.dismiss();
                } else {
                    showToast("올바르지 않은 날짜입니다.");
                }
            });

            button1.setOnClickListener(view -> mAlertDialog.dismiss());
            button1.setTextColor(Color.GRAY);
        });

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.show();

        mStartDateDialogText.setOnClickListener(view -> {
            mStartDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateDialogText.setBackground(null);

            setStartCalendarValue();
        });

        mEndDateDialogText.setOnClickListener(view -> {
            mEndDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateDialogText.setBackground(null);

            setEndCalendarValue();
        });
    }

    /**
     * 종료 날짜 다이얼로그
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.ll_activity_add_schedule_end_time)
    void onClickEndTimeDialog() {
        // 종료날짜 선택
        mEndDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
        mStartDateDialogText.setBackground(null);

        setEndCalendarValue();

        setDateTextView();

        // 다이얼로그 창
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setView(mDateView)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, null)
                .setNegativeButton(R.string.cancel, null);
        mAlertDialog = builder.create();

        mAlertDialog.setOnShowListener(dialogInterface -> {
            Button button = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button button1 = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            button.setOnClickListener(view -> {
                GregorianCalendar startCalendar = mAddSchedulePresenter.getStartCalendar();
                GregorianCalendar endCalendar = mAddSchedulePresenter.getEndCalendar();

                if (mAddSchedulePresenter.checkDate(startCalendar, endCalendar)) {
                    setTimeTextView();
                    mAlertDialog.dismiss();
                } else {
                    showToast("올바르지 않은 날짜입니다.");
                }
            });

            button1.setOnClickListener(view -> mAlertDialog.dismiss());
            button1.setTextColor(Color.GRAY);
        });

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.show();

        mStartDateDialogText.setOnClickListener(view -> {
            mStartDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateDialogText.setBackground(null);

            setStartCalendarValue();
        });

        mEndDateDialogText.setOnClickListener(view -> {
            mEndDateDialogText.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateDialogText.setBackground(null);

            setEndCalendarValue();
        });
    }

    /**
     * 알람 다이얼로그
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_activity_add_schedule_alarm)
    void onClickAlarmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
            displayAlarmText();     // 알람 텍스트 적용
            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));

        if (mAlarmView.getParent() != null) {
            ((ViewGroup) mAlarmView.getParent()).removeView(mAlarmView);
        }

        mAlertDialog.setView(mAlarmView);
        mAlertDialog.show();

        mOnTimeCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    mOnTimeCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m5MinutesAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m5MinutesAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m10MinutesAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m10MinutesAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m15MinutesAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m15MinutesAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m30MinutesAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m30MinutesAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m1HourAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m1HourAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m2HourAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m2HourAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m3HourAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m3HourAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m12HourAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m12HourAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m1DayAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m1DayAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m2DaysAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m2DaysAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });

        m1WeekAgoCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // true
                if (!mAddSchedulePresenter.checkAlarmCheckBox(mCheckBoxes)) {
                    // 3개 이상 체크되었을 경우
                    showToast("최대 2개의 알람만 선택하실 수 있습니다.");
                    m1WeekAgoCheckBox.setChecked(false);
                    return;
                }
            }
            displayDialogAlarmText();
        });
    }

    /**
     * 색상 다이얼로그
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_activity_add_schedule_color)
    void onClickColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        if (mColorView.getParent() != null) {
            ((ViewGroup) mColorView.getParent()).removeView(mColorView);
        }

        mAlertDialog.setView(mColorView);
        mAlertDialog.show();

        setColor(mAlertDialog);     // 색상 적용 함수
    }

    /**
     * 공유 다이얼로그
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_activity_add_schedule_share)
    void onClickShareDialog() {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivityForResult(intent, Const.REQUEST_CODE_SHARE_CALENDAR);

        /*mFriendList = new ArrayList<>();

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, mFriendList);
        mFriendListView.setAdapter(mArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        if (mShareView.getParent() != null) {
            ((ViewGroup) mShareView.getParent()).removeView(mShareView);
        }

        mAlertDialog.setView(mShareView);
        mAlertDialog.show();

        // 친구 목록 불러오기
        mAddSchedulePresenter.getFriendList();

        // 친구목록 불러오기(수동)
        mRefreshBtn.setOnClickListener(view -> mAddSchedulePresenter.getFriendList());

        // 닫기
        mCloseBtn.setOnClickListener(view -> mAlertDialog.dismiss());

        // 공유 대상 추가
        mCheckBtn.setOnClickListener(view -> {
            StringBuilder sharedString = new StringBuilder();
            SparseBooleanArray sparseBooleanArray = mFriendListView.getCheckedItemPositions();
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                if (sparseBooleanArray.get(i)) {
                    if (sharedString.toString().equals("")) {
                        sharedString = new StringBuilder(mFriendList.get(i));
                    } else {
                        sharedString.append(", ").append(mFriendList.get(i));
                    }
                    mSelectFriendNumberList.add(mFriendNumberList.get(i));
                }
            }
            mAddRoomEditText.setText(sharedString);
            mAlertDialog.dismiss();
        });*/
    }

    /**
     * 일정 등록 함수
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.iv_activity_add_schedule_enroll)
    void onClickScheduleEnroll() {
        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();
        String place = mPlaceEditText.getText().toString();

        if (title.equals("")) {
            title = "제목없음";
        }

        mProgressDialog = new ProgressDialog(this);
        if (mAddSchedulePresenter.getCalendarMode().equals(Const.CALENDAR_ADD)) {
            // 일정 추가일 경우
            mProgressDialog.setMessage("일정 등록 중...");
        } else {
            // 일정 수정일 경우
            mProgressDialog.setMessage("일정 수정 중...");
        }
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        mProgressDialog.show();

        mAddSchedulePresenter.createAddScheduleData(title, content, place, mCheckBoxes, mColorStr);
    }

    /**
     * 일정 삭제
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.iv_activity_add_schedule_delete)
    void onClickScheduleDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        mAlertDialog.setTitle("일정 삭제");
        mAlertDialog.setMessage("일정을 삭제하시겠습니까?");
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialog, which) -> {
            dialog.dismiss();
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("일정 삭제 중...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
            mProgressDialog.show();
            mAddSchedulePresenter.getDeleteScheduleData();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> dialog.dismiss());
        mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
        mAlertDialog.show();
    }
}