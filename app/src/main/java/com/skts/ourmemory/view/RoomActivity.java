package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.RoomCalendarAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.room.AddRoomPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
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
        mCalendarList = new ArrayList<>();

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new RoomCalendarAdapter(mCalendarList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setCalendarList(GregorianCalendar cal) {
        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
        int today = cal.get(Calendar.DAY_OF_MONTH);

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

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_room_close_btn)
    void onClickCloseBtn() {
        onBackPressed();
    }
}
