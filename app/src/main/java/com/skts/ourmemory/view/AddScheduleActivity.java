package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.BaseActivity;
import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.presenter.AddSchedulePresenter;
import com.skts.ourmemory.util.DebugLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class AddScheduleActivity extends BaseActivity implements AddScheduleContract.View {
    private final String TAG = AddScheduleActivity.class.getSimpleName();

    private AddSchedulePresenter mAddSchedulePresenter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_add_schedule)
    Toolbar mToolbar;       // 툴바
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

    // 다이얼로그 내 TextView
    private TextView mStartDateTextView;                // 시작일
    private TextView mEndDateTextView;                  // 종료일
    private NumberPicker mYearNumberPicker;             // 년
    private NumberPicker mMonthNumberPicker;            // 월
    private NumberPicker mDayNumberPicker;              // 일
    private NumberPicker mHourNumberPicker;             // 시
    private NumberPicker mMinutesNumberPicker;          // 분
    //private TimePicker mTimeUnitPicker;                 // AM/PM

    // 날짜 관련
    String[] mStartDateList;        // 시작 날짜
    String[] mEndDateList;        // 종료 날짜

    /*다이얼로그*/
    AlertDialog mAlertDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        // Toolbar 생성
        setSupportActionBar(mToolbar);

        /// Toolbar 왼쪽에 버튼을 추가한다.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_30);

        // Toolbar 타이틀 없애기
        getSupportActionBar().setTitle("");

        mAddSchedulePresenter = new AddSchedulePresenter();
        mAddSchedulePresenter.setView(this);

        // 다이얼로그 뷰
        mDateView = getLayoutInflater().inflate(R.layout.dialog_date, null, false);

        // 다이얼로그 뷰 내 텍스트뷰
        mStartDateTextView = mDateView.findViewById(R.id.tv_dialog_date_start_date);
        mEndDateTextView = mDateView.findViewById(R.id.tv_dialog_date_end_date);
        mYearNumberPicker = mDateView.findViewById(R.id.np_dialog_date_year);
        mMonthNumberPicker = mDateView.findViewById(R.id.np_dialog_date_month);
        mDayNumberPicker = mDateView.findViewById(R.id.np_dialog_date_day);
        mHourNumberPicker = mDateView.findViewById(R.id.np_dialog_date_hour);
        mMinutesNumberPicker = mDateView.findViewById(R.id.np_dialog_date_minutes);
        //mTimeUnitPicker = mDateView.findViewById(R.id.tp_dialog_date_unit);

        // 초기 날짜 설정
        initDateView();

        mYearNumberPicker.setOnScrollListener((numberPicker, i) -> {
            if (i == NumberPicker.OnScrollListener.SCROLL_STATE_IDLE) {
                
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mAddSchedulePresenter.releaseView();
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

        mStartTimeTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일 " + mStartDateList[5] + "요일 " + mStartDateList[3] + ":" + mStartDateList[4]);     // 시작 날짜
        mEndTimeTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일 " + mEndDateList[5] + "요일 " + mEndDateList[3] + ":" + mEndDateList[4]);                    // 종료 날짜

        // 년
        mYearNumberPicker.setMinValue(MIN_YEAR);
        mYearNumberPicker.setMaxValue(MAX_YEAR);

        // 월
        mMonthNumberPicker.setMinValue(MIN_MONTH);
        mMonthNumberPicker.setMaxValue(MAX_MONTH);

        // 일
        mDayNumberPicker.setMinValue(MIN_DAY);
        mDayNumberPicker.setMaxValue(MAX_DAY);

        // 시간
        mHourNumberPicker.setMinValue(MIN_HOUR);
        mHourNumberPicker.setMaxValue(MAX_HOUR);

        // 분
        mMinutesNumberPicker.setMinValue(MIN_MINUTE);
        mMinutesNumberPicker.setMaxValue(MAX_MINUTE);
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

        mStartDateTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일\n" + "(" + mStartDateList[5] + ") " + mStartDateList[3] + ":" + mStartDateList[4]);
        mEndDateTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일\n" + "(" + mEndDateList[5] + ") " + mEndDateList[3] + ":" + mEndDateList[4]);

        mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
        mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
        mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
        mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
        mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));

        // 다이얼로그 창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {

            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.setView(mDateView);
        mAlertDialog.show();

        mStartDateTextView.setOnClickListener(view -> {
            mStartDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));
        });

        mEndDateTextView.setOnClickListener(view -> {
            mEndDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));
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

        mStartDateTextView.setText(mStartDateList[0] + "년 " + mStartDateList[1] + "월 " + mStartDateList[2] + "일\n" + "(" + mStartDateList[5] + ") " + mStartDateList[3] + ":" + mStartDateList[4]);
        mEndDateTextView.setText(mEndDateList[0] + "년 " + mEndDateList[1] + "월 " + mEndDateList[2] + "일\n" + "(" + mEndDateList[5] + ") " + mEndDateList[3] + ":" + mEndDateList[4]);

        mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
        mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
        mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
        mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
        mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));

        // 다이얼로그 창
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {

            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.setView(mDateView);
        mAlertDialog.show();

        mStartDateTextView.setOnClickListener(view -> {
            mStartDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mEndDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mStartDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mStartDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mStartDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mStartDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mStartDateList[4]));
        });

        mEndDateTextView.setOnClickListener(view -> {
            mEndDateTextView.setBackgroundColor(getColor(R.color.schedule_select_background));
            mStartDateTextView.setBackground(null);

            mYearNumberPicker.setValue(Integer.parseInt(mEndDateList[0]));
            mMonthNumberPicker.setValue(Integer.parseInt(mEndDateList[1]));
            mDayNumberPicker.setValue(Integer.parseInt(mEndDateList[2]));
            mHourNumberPicker.setValue(Integer.parseInt(mEndDateList[3]));
            mMinutesNumberPicker.setValue(Integer.parseInt(mEndDateList[4]));
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

            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        // mDateView 말고 알람 다이얼로그 뷰로 수정해야함!
        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.setView(mDateView);
        mAlertDialog.show();
    }

    /**
     * 색상 다이얼로그
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_activity_add_schedule_color)
    void onClickColorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();

        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {

            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());

        // mDateView 말고 색상 다이얼로그 뷰로 수정해야함!
        if (mDateView.getParent() != null) {
            ((ViewGroup) mDateView.getParent()).removeView(mDateView);
        }

        mAlertDialog.setView(mDateView);
        mAlertDialog.show();
    }
}
