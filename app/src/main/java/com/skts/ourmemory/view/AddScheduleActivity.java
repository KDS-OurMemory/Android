package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.presenter.AddSchedulePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class AddScheduleActivity extends BaseActivity implements AddScheduleContract.View {
    private AddSchedulePresenter mAddSchedulePresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_schedule)
    Toolbar mToolbar;       // 툴바
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_title)
    TextView mTitleEditText;                        // 일정 제목
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_schedule_start_time)
    TextView mStartTimeTextView;                    // 시작날짜
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_add_schedule_end_time)
    TextView mEndTimeTextView;                      // 종료날짜
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_content)
    EditText mContentEditText;                      // 내용
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_place)
    EditText mPlaceEditText;                        // 장소
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_alarm)
    EditText mAlarmEditText;                        // 알람
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_color)
    EditText mColorEditText;                        // 색상

    // 다이얼로그 View
    private View mDateView;
    private View mAlarmView;
    private View mColorView;
    private View mShareView;

    // 다이얼로그 내 TextView
    private TextView mStartDateTextView;                // 시작일
    private TextView mEndDateTextView;                  // 종료일
    private NumberPicker mYearNumberPicker;             // 년
    private NumberPicker mMonthNumberPicker;            // 월
    private NumberPicker mDayNumberPicker;              // 일
    private NumberPicker mHourNumberPicker;             // 시
    private NumberPicker mMinutesNumberPicker;          // 분
    //private TimePicker mTimeUnitPicker;               // AM/PM

    // 날짜 관련
    String[] mStartDateList;        // 시작 날짜
    String[] mEndDateList;          // 종료 날짜

    /*다이얼로그*/
    AlertDialog mAlertDialog = null;
    ProgressDialog mProgressDialog;

    // 알람 관련
    // 다이얼로그 내 CheckBox
    private CheckBox mOnTimeCheckBox;                           // 정시
    private CheckBox m5MinutesAgoCheckBox;                      // 5분 전
    private CheckBox m10MinutesAgoCheckBox;                     // 10분 전
    private CheckBox m15MinutesAgoCheckBox;                     // 15분 전
    private CheckBox m30MinutesAgoCheckBox;                     // 30분 전
    private CheckBox m1HourAgoCheckBox;                         // 1시간 전
    private CheckBox m2HourAgoCheckBox;                         // 2시간 전
    private CheckBox m3HourAgoCheckBox;                         // 3시간 전
    private CheckBox m12HourAgoCheckBox;                        // 12시간 전
    private CheckBox m1DayAgoCheckBox;                          // 1일 전
    private CheckBox m2DaysAgoCheckBox;                         // 2일 전
    private CheckBox m1WeekAgoCheckBox;                         // 1주 전

    // 다이얼로그 내 TextView
    private TextView mAlarmTextView;                            // 알람 텍스트뷰

    // 체크박스 리스트
    ArrayList<CheckBox> mCheckBoxes;

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
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_activity_add_schedule_share)
    EditText mShareEditText;                            // 공유

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        // Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mAddSchedulePresenter = new AddSchedulePresenter();
        mAddSchedulePresenter.setView(this);

        // 다이얼로그 뷰
        mDateView = getLayoutInflater().inflate(R.layout.dialog_date, null, false);
        mAlarmView = getLayoutInflater().inflate(R.layout.dialog_alarm, null, false);
        mColorView = getLayoutInflater().inflate(R.layout.dialog_color, null, false);
        mShareView = getLayoutInflater().inflate(R.layout.dialog_friend, null, false);

        // 다이얼로그 뷰 내 텍스트뷰
        mStartDateTextView = mDateView.findViewById(R.id.tv_dialog_date_start_date);
        mEndDateTextView = mDateView.findViewById(R.id.tv_dialog_date_end_date);
        mYearNumberPicker = mDateView.findViewById(R.id.np_dialog_date_year);
        mMonthNumberPicker = mDateView.findViewById(R.id.np_dialog_date_month);
        mDayNumberPicker = mDateView.findViewById(R.id.np_dialog_date_day);
        mHourNumberPicker = mDateView.findViewById(R.id.np_dialog_date_hour);
        mMinutesNumberPicker = mDateView.findViewById(R.id.np_dialog_date_minutes);
        //mTimeUnitPicker = mDateView.findViewById(R.id.tp_dialog_date_unit);

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

        mCheckBoxes = new ArrayList<>();
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

        // 공유
        mFriendNumberList = new ArrayList<>();
        mShareRoomNumberList = new ArrayList<>();
        mSelectFriendNumberList = new ArrayList<>();

        // 초기 날짜 설정
        initDateView();

        // 초기 알람 설정
        initAlarmView();

        // 초기 색상 설정
        initColor();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 초기 날짜 설정 함수
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void initDateView() {
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

        String[] dateList = mAddSchedulePresenter.initDate();       // 시작 및 종료 날짜 String 배열 받아오기
        mStartDateList = dateList[0].split(":");
        mEndDateList = dateList[1].split(":");

        setDateTextView();

        // 년
        mYearNumberPicker.setMinValue(MIN_YEAR);
        mYearNumberPicker.setMaxValue(MAX_YEAR);
        mYearNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 월
        mMonthNumberPicker.setMinValue(MIN_MONTH);
        mMonthNumberPicker.setMaxValue(MAX_MONTH);
        mMonthNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 일
        mDayNumberPicker.setMinValue(MIN_DAY);
        mDayNumberPicker.setMaxValue(MAX_DAY);
        mDayNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 시간
        mHourNumberPicker.setMinValue(MIN_HOUR);
        mHourNumberPicker.setMaxValue(MAX_HOUR);
        mHourNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        // 분
        mMinutesNumberPicker.setMinValue(MIN_MINUTE);
        mMinutesNumberPicker.setMaxValue(MAX_MINUTE);
        mMinutesNumberPicker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mStartTimeTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일 " + mStartDateList[5] + "요일 " + mStartDateList[3] + ":" + mStartDateList[4]);
        mEndTimeTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일 " + mEndDateList[5] + "요일 " + mEndDateList[3] + ":" + mEndDateList[4]);
    }

    /**
     * 초기 알람 설정
     */
    @Override
    public void initAlarmView() {
        m10MinutesAgoCheckBox.setChecked(true);     // 기본 값 10분 전 알람 적용
        displayDialogAlarmText();   // 다이얼로그 알람
        displayAlarmText();         // 메인 알람
    }

    /**
     * 초기 색상 설정
     */
    @Override
    public void initColor() {
        mColorEditText.setText("색상");
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

    /**
     * 스크롤 시 변경 날짜 적용 함수
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void setDateTextView() {
        mStartDateTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일\n" + "(" + mStartDateList[5] + ") " + mStartDateList[3] + ":" + mStartDateList[4]);
        mEndDateTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일\n" + "(" + mEndDateList[5] + ") " + mEndDateList[3] + ":" + mEndDateList[4]);
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
        mColorEditText.setText("색상");

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
    public Context getAppContext() {
        return this;
    }

    /**
     * 시작 날짜 다이얼로그
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.ll_activity_add_schedule_start_time)
    void onClickStartTimeDialog() {
        // 시작날짜 선택
        mStartDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
        mEndDateTextView.setBackground(null);

        mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mStartDateList[0] = String.valueOf(mYearNumberPicker.getValue());
            // 요일 반환 함수
            mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
            setDateTextView();
        });

        mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mStartDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
            // 요일 반환 함수
            mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
            setDateTextView();
        });

        mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mStartDateList[2] = String.valueOf(mDayNumberPicker.getValue());
            // 요일 반환 함수
            mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
            setDateTextView();
        });

        mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mStartDateList[3] = String.valueOf(mHourNumberPicker.getValue());
            setDateTextView();
        });

        mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mStartDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
            setDateTextView();
        });

        setDateTextView();

        mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
        mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
        mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
        mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
        mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));

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
                if (mAddSchedulePresenter.checkDate(mStartDateList, mEndDateList)) {
                    mStartTimeTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일 " + mStartDateList[5] + "요일 " + mStartDateList[3] + ":" + mStartDateList[4]);
                    mEndTimeTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일 " + mEndDateList[5] + "요일 " + mEndDateList[3] + ":" + mEndDateList[4]);
                    mAlertDialog.dismiss();
                } else {
                    showToast("올바르지 않은 날짜입니다.");
                }
            });

            button1.setOnClickListener(view -> mAlertDialog.dismiss());
        });

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.show();

        mStartDateTextView.setOnClickListener(view -> {
            mStartDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));

            mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[0] = String.valueOf(mYearNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[2] = String.valueOf(mDayNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[3] = String.valueOf(mHourNumberPicker.getValue());
                setDateTextView();
            });

            mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
                setDateTextView();
            });
        });

        mEndDateTextView.setOnClickListener(view -> {
            mEndDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));

            mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[0] = String.valueOf(mYearNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[2] = String.valueOf(mDayNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[3] = String.valueOf(mHourNumberPicker.getValue());
                setDateTextView();
            });

            mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
                setDateTextView();
            });
        });
    }

    /**
     * 종료 날짜 다이얼로그
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.ll_activity_add_schedule_end_time)
    void onClickEndTimeDialog() {
        // 종료날짜 선택
        mEndDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
        mStartDateTextView.setBackground(null);

        mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mEndDateList[0] = String.valueOf(mYearNumberPicker.getValue());
            // 요일 반환 함수
            mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
            setDateTextView();
        });

        mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mEndDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
            // 요일 반환 함수
            mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
            setDateTextView();
        });

        mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mEndDateList[2] = String.valueOf(mDayNumberPicker.getValue());
            // 요일 반환 함수
            mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
            setDateTextView();
        });

        mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mEndDateList[3] = String.valueOf(mHourNumberPicker.getValue());
            setDateTextView();
        });

        mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
            mEndDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
            setDateTextView();
        });

        setDateTextView();

        mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
        mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
        mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
        mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
        mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));

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
                if (mAddSchedulePresenter.checkDate(mStartDateList, mEndDateList)) {
                    mStartTimeTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일 " + mStartDateList[5] + "요일 " + mStartDateList[3] + ":" + mStartDateList[4]);
                    mEndTimeTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일 " + mEndDateList[5] + "요일 " + mEndDateList[3] + ":" + mEndDateList[4]);
                    mAlertDialog.dismiss();
                } else {
                    showToast("올바르지 않은 날짜입니다.");
                }
            });

            button1.setOnClickListener(view -> mAlertDialog.dismiss());
        });

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.show();

        mStartDateTextView.setOnClickListener(view -> {
            mStartDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));

            mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[0] = String.valueOf(mYearNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[2] = String.valueOf(mDayNumberPicker.getValue());
                // 요일 반환 함수
                mStartDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mStartDateList[0], mStartDateList[1], mStartDateList[2]);
                setDateTextView();
            });

            mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[3] = String.valueOf(mHourNumberPicker.getValue());
                setDateTextView();
            });

            mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mStartDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
                setDateTextView();
            });
        });

        mEndDateTextView.setOnClickListener(view -> {
            mEndDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));

            mYearNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[0] = String.valueOf(mYearNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mMonthNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[1] = String.valueOf(mMonthNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mDayNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[2] = String.valueOf(mDayNumberPicker.getValue());
                // 요일 반환 함수
                mEndDateList[5] = mAddSchedulePresenter.calcDayOfWeek(mEndDateList[0], mEndDateList[1], mEndDateList[2]);
                setDateTextView();
            });

            mHourNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[3] = String.valueOf(mHourNumberPicker.getValue());
                setDateTextView();
            });

            mMinutesNumberPicker.setOnValueChangedListener((numberPicker, i, i1) -> {
                mEndDateList[4] = String.valueOf(mMinutesNumberPicker.getValue());
                setDateTextView();
            });
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
        mFriendList = new ArrayList<>();
        //mFriendList.add("테스트");

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, mFriendList);
        mFriendListView.setAdapter(mArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        if (mShareView.getParent() != null) {
            ((ViewGroup) mShareView.getParent()).removeView(mShareView);
        }

        mAlertDialog.setView(mShareView);
        mAlertDialog.show();

        // 친구목록 불러오기
        mRefreshBtn.setOnClickListener(view -> mAddSchedulePresenter.getFriendList());

        // 닫기
        mCloseBtn.setOnClickListener(view -> mAlertDialog.dismiss());

        // 공유 대상 추가
        mCheckBtn.setOnClickListener(view -> {
            StringBuilder sharedString = new StringBuilder();
            SparseBooleanArray sparseBooleanArray = mFriendListView.getCheckedItemPositions();
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                if (sparseBooleanArray.get(i)) {
                    if(sharedString.toString().equals("")) {
                        sharedString = new StringBuilder(mFriendList.get(i));
                    } else {
                        sharedString.append(", ").append(mFriendList.get(i));
                    }
                    mSelectFriendNumberList.add(mFriendNumberList.get(i));
                }
            }
            mShareEditText.setText(sharedString);
            mAlertDialog.dismiss();
        });
    }

    /**
     * 일정 등록 함수
     */
    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @OnClick(R.id.iv_activity_add_schedule_enroll)
    void onClickScheduleEnroll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        mAlertDialog.setTitle("일정 등록");
        mAlertDialog.setMessage("일정을 등록하시겠습니까?");
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
            dialogInterface.dismiss();

            String title = mTitleEditText.getText().toString();
            String content = mContentEditText.getText().toString();
            String place = mPlaceEditText.getText().toString();

            if (title.equals("")) {
                showToast("일정 제목을 입력해주세요");
                return;
            }

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("일정 등록 중...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
            mProgressDialog.show();

            mAddSchedulePresenter.createAddScheduleData(title, mSelectFriendNumberList, content, place, mStartDateList, mEndDateList, mCheckBoxes, mColorStr, mShareRoomNumberList);
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        mAlertDialog.show();
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
}


