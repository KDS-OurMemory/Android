package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.RoomCalendarAdapter;
import com.skts.ourmemory.adapter.RoomDescriptionAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.room.AddRoomPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.presenter.RoomPresenter;
import com.skts.ourmemory.util.Keys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomActivity extends BaseActivity implements RoomContract.View {
    private RoomContract.Presenter mPresenter;
    private RoomCalendarAdapter mAdapter;
    private RoomDescriptionAdapter mRoomDescriptionAdapter;
    private ArrayList<Object> mCalendarList;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_activity_room)
    Toolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_name)
    TextView mRoomNameText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_participants_count)
    TextView mRoomParticipantsCount;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_room_calendar)
    RecyclerView mRecyclerView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_activity_room_description)
    RecyclerView mDescriptionView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_date)
    TextView mDateTextView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_no_calendar_text)
    TextView mNoCalendarText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_room);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.releaseView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left_room, R.anim.slide_out_right_room);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Const.REQUEST_CODE_CALENDAR) {
                // 프래그먼트로 데이터 처리
                SchedulePostResult.ResponseValue responseValue = (SchedulePostResult.ResponseValue) data.getExtras().getSerializable(Const.SCHEDULE_DATA);
                String mode = data.getStringExtra(Const.CALENDAR_PURPOSE);
                updateCalendarData(responseValue, mode);
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

        // Toolbar 타이틀 없애기
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        mPresenter = new RoomPresenter();
        mPresenter.setView(this);

        Intent intent = getIntent();
        RoomPostResult.ResponseValue responseValue = (RoomPostResult.ResponseValue) intent.getSerializableExtra(Const.ROOM_DATA);

        // 방 정보 받기
        mPresenter.getRoomData(responseValue.getRoomId());

        mRoomNameText.setText(responseValue.getName());
        mRoomParticipantsCount.setText(String.valueOf(responseValue.getMemberList().size()));

        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mPresenter.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        setCalendarList(calendar);
        setRecycler();
    }

    @Override
    public void setRecycler() {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new RoomCalendarAdapter(mCalendarList);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density = displayMetrics.densityDpi / 160f;

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = point.y;           // 레이아웃 높이

        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        int lastWeek = mPresenter.getLastWeek(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);

        mAdapter.setCalendarList(mCalendarList, density, height, lastWeek);

        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        mRoomDescriptionAdapter = new RoomDescriptionAdapter();

        mDescriptionView.setLayoutManager(new LinearLayoutManager(this));
        mDescriptionView.setAdapter(mRoomDescriptionAdapter);

        mAdapter.setOnItemClickListener((view, position) -> {
            String day = mAdapter.getCalendarDay(position);
            mPresenter.setDay(Integer.parseInt(day));
        });
    }

    @Override
    public void setCalendarList(GregorianCalendar cal) {
        mCalendarList = new ArrayList<>();

        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
        int today = cal.get(Calendar.DAY_OF_MONTH);

        mDateTextView.setText(date);

        ArrayList<Object> calendarList = new ArrayList<>();

        try {
            GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1, 0, 0, 0);

            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;         // 해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있다
            int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);     // 해당 월에 마지막 요일

            // EMPTY 생성
            for (int j = 0; j < dayOfWeek; j++) {
                calendarList.add(Keys.EMPTY);           // 비어있는 일자 타입
            }
            for (int j = 1; j <= max; j++) {
                calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));      // 일자 타입
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCalendarList = calendarList;
    }

    @Override
    public void showCalendar(AddRoomPostResult.ResponseValue responseValue) {
        mAdapter.addItem(responseValue);
    }

    @Override
    public void updateCalendarData(SchedulePostResult.ResponseValue responseValue, String mode) {
        if (mode.equals(Const.CALENDAR_ADD)) {
            // 일정 추가
            showToast(responseValue.getName() + " 일정이 추가되었습니다");
            //mAdapter.addPlusItem(responseValue.get)
        } else if (mode.equals(Const.CALENDAR_EDIT)) {
            // 일정 편집

        } else {
            // 일정 삭제

        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_room_close_btn)
    void onClickCloseBtn() {
        onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.fab_activity_room_floating_button)
    void onClickAddCalendarBtn() {
        Intent intent = new Intent(this, AddScheduleActivity.class);
        SchedulePostResult.ResponseValue responseValue = null;
        intent.putExtra(Const.CALENDAR_DATA, responseValue);

        intent.putExtra(Const.CALENDAR_YEAR, mPresenter.getYear());
        intent.putExtra(Const.CALENDAR_MONTH, mPresenter.getMonth());
        intent.putExtra(Const.CALENDAR_DAY, mPresenter.getDay());
        intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_ADD);

        startActivityForResult(intent, Const.REQUEST_CODE_CALENDAR);
    }
}
