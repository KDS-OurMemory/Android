package com.skts.ourmemory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.navigation.NavigationView;
import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.adapter.RoomCalendarAdapter;
import com.skts.ourmemory.adapter.RoomDescriptionAdapter;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.friend.FriendDAO;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.presenter.RoomPresenter;
import com.skts.ourmemory.util.Keys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class RoomActivity extends BaseActivity implements RoomContract.View {
    private RoomContract.Presenter mPresenter;
    private RoomCalendarAdapter mAdapter;
    private RoomDescriptionAdapter mRoomDescriptionAdapter;
    private FriendListAdapter mFriendListAdapter;
    private ArrayList<Object> mCalendarList;
    private RecyclerView mFriendView;

    /*Dialog*/
    private AlertDialog mAlertDialog;

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
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_description_header)
    TextView mDescriptionHeaderText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_room_description_lunar)
    TextView mDescriptionLunarText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_activity_room_navigation_view)
    NavigationView mNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.dl_activity_room_drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_room);

        initSet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mPresenter.releaseView();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
            mDrawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();

            overridePendingTransition(R.anim.slide_in_left_room, R.anim.slide_out_right_room);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Const.REQUEST_CODE_CALENDAR) {
                // 프래그먼트로 데이터 처리
                assert data != null;
                MemoryDAO memoryDAO = (MemoryDAO) data.getExtras().getSerializable(Const.SCHEDULE_DATA);
                String mode = data.getStringExtra(Const.CALENDAR_PURPOSE);
                updateCalendarData(memoryDAO, mode);
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
        RoomResponseValue responseValue = (RoomResponseValue) intent.getSerializableExtra(Const.ROOM_DATA);

        // 방 정보 받기
        int roomId = responseValue.getRoomId();
        mPresenter.setRoomId(roomId);
        mPresenter.getRoomData(roomId);

        mRoomNameText.setText(responseValue.getName());
        mRoomParticipantsCount.setText(String.valueOf(responseValue.getMemberList().size()));

        GregorianCalendar calendar = new GregorianCalendar();       // 오늘 날짜
        mPresenter.setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        setCalendarList(calendar);
        setRecycler();
        setNavigationView();
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
            // 달력 클릭 시
            String day = mAdapter.getCalendarDay(position);
            mPresenter.setDay(Integer.parseInt(day));

            // 클릭 날짜 저장
            mAdapter.setClickedDay(position);

            mDescriptionHeaderText.setText(day);

            String lunar = mPresenter.convertSolarToLunar();            // 음력 변환
            mDescriptionLunarText.setText(lunar);

            List<MemoryDAO> memoryDAOList = mAdapter.getItem(position);

            if (memoryDAOList.size() == 0) {
                mRoomDescriptionAdapter.clearItem();
                mNoCalendarText.setVisibility(View.VISIBLE);
            } else {
                mRoomDescriptionAdapter.addItem(memoryDAOList);
                mNoCalendarText.setVisibility(View.GONE);
            }
        });

        mRoomDescriptionAdapter.setOnItemClickListener((view, position) -> {
            // 일정 클릭 시
            MemoryDAO memoryDAO = mRoomDescriptionAdapter.getData(position);
            showToast(memoryDAO.getName());
        });
    }

    @Override
    public void setNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(item -> {
            mDrawerLayout.closeDrawer(GravityCompat.END);

            return false;
        });

        View headerView = mNavigationView.getHeaderView(0);
        mFriendView = headerView.findViewById(R.id.rv_layout_navigation_header_recycler_view);
        mFriendView.setLayoutManager(new LinearLayoutManager(this));

        mFriendListAdapter = new FriendListAdapter();
        mFriendView.setAdapter(mFriendListAdapter);
    }

    @Override
    public void setCalendarList(GregorianCalendar cal) {
        mCalendarList = new ArrayList<>();

        String date = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1);
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
    public void showCalendar(RoomResponseValue responseValue) {
        mAdapter.addItem(responseValue);

        // 날짜 표시
        int day = mPresenter.getDay();
        mDescriptionHeaderText.setText(String.valueOf(day));

        // 음력 표시
        String lunar = mPresenter.convertSolarToLunar();            // 음력 변환
        mDescriptionLunarText.setText(lunar);

        // 초기 일정 내역 표시
        GregorianCalendar calendar = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), 1);     // 해당 월의 1일이 몇 요일인지
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;     //  빈 날짜
        int position = dayOfWeek + day - 1;

        List<MemoryDAO> memoryDAOList = mAdapter.getItem(position);
        mRoomDescriptionAdapter.addItem(memoryDAOList);

        if (memoryDAOList.size() == 0) {
            mNoCalendarText.setVisibility(View.VISIBLE);
        } else {
            mNoCalendarText.setVisibility(View.GONE);
        }

        List<FriendDAO> friendDAOList = responseValue.getMemberList();
        mFriendListAdapter = new FriendListAdapter(friendDAOList);
        mFriendView.setAdapter(mFriendListAdapter);
    }

    @Override
    public void updateCalendarData(MemoryDAO memoryDAO, String mode) {
        // 일정이 없습니다 없애기
        mNoCalendarText.setVisibility(View.GONE);
        if (mode.equals(Const.CALENDAR_ADD)) {
            // 일정 추가
            showToast(memoryDAO.getName() + " 일정이 추가되었습니다");
            mAdapter.addPlusItem(memoryDAO);
            Calendar cal = new GregorianCalendar(mPresenter.getYear(), mPresenter.getMonth(), mPresenter.getDay());
            mRoomDescriptionAdapter.addPlusItem(memoryDAO, cal);
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
        intent.putExtra(Const.CALENDAR_DATA, (Serializable) null);

        intent.putExtra(Const.ROOM_ID, mPresenter.getRoomId());         // 방 번호
        intent.putExtra(Const.CALENDAR_YEAR, mPresenter.getYear());
        intent.putExtra(Const.CALENDAR_MONTH, mPresenter.getMonth());
        intent.putExtra(Const.CALENDAR_DAY, mPresenter.getDay());
        intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_ADD);

        startActivityForResult(intent, Const.REQUEST_CODE_CALENDAR);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_room_navigation_button)
    void onClickNavigationBtn() {
        mDrawerLayout.openDrawer(GravityCompat.END);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_room_exit_button)
    void onClickExitBtn() {
        // 나가기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.create();
        mAlertDialog.setTitle("방 나가기");
        mAlertDialog.setMessage("방을 나가시겠습니까?");
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
            showToast("나가기!");
            dialogInterface.dismiss();
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
        mAlertDialog.show();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_room_setting_button)
    void onClickSettingBtn() {
        showToast("설정");
    }
}
