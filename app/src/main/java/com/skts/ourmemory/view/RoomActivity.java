package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.room.AddRoomPostResult;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.presenter.RoomPresenter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomActivity extends BaseActivity implements RoomContract.View {
    private RoomContract.Presenter mPresenter;

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
    @BindView(R.id.cv_activity_room_calendar_view)
    CalendarView mCalendarView;

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
