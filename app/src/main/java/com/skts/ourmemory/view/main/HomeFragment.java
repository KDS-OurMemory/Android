package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.HomeRoomAdapter;
import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.friend.FriendDAO;
import com.skts.ourmemory.model.memory.MemoryDAO;
import com.skts.ourmemory.model.room.RoomData;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.presenter.HomePresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private Unbinder unbinder;

    private HomeRoomAdapter mHomeRoomAdapter;
    private final HomeContract.Presenter mPresenter;
    private Context mContext;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_today_calendar_text)
    TextView mTodayCalendarText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_next_calendar_text)
    TextView mNextCalendarText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_fragment_main_home_recyclerview)
    RecyclerView mRecyclerView;

    // 다가오는 일정
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_1day)
    TextView mCalendarDay1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_2day)
    TextView mCalendarDay2;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_3day)
    TextView mCalendarDay3;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_4day)
    TextView mCalendarDay4;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_5day)
    TextView mCalendarDay5;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_6day)
    TextView mCalendarDay6;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_7day)
    TextView mCalendarDay7;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_1day_calendar)
    TextView mCalendar1;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_2day_calendar)
    TextView mCalendar2;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_3day_calendar)
    TextView mCalendar3;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_4day_calendar)
    TextView mCalendar4;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_5day_calendar)
    TextView mCalendar5;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_6day_calendar)
    TextView mCalendar6;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_home_7day_calendar)
    TextView mCalendar7;

    public HomeFragment() {
        mPresenter = new HomePresenter();
    }

    /**
     * setHasOptionsMenu true : 액티비티보다 프레그먼트의 메뉴가 우선
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_main_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mPresenter.setView(this);
        mContext = container.getContext();

        initView(view);
        initSet();

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mContext = null;
        mPresenter.releaseView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            // 프래그먼트 화면이 보여지면
            showRoomData(((MainActivity) getActivity()).getRoomData());
            showCalendar(((MainActivity) getActivity()).getScheduleData());
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public void initView(View view) {
        // Init layoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set layoutManager
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mHomeRoomAdapter = new HomeRoomAdapter();
        mRecyclerView.setAdapter(mHomeRoomAdapter);
    }

    @Override
    public void initSet() {
    }

    @Override
    public void showRoomList(ArrayList<String> names, List<List<FriendDAO>> membersList) {
        // 데이터 지우기
        mHomeRoomAdapter.clearItem();

        for (int i = 0; i < names.size(); i++) {
            StringBuilder members = new StringBuilder();
            for (int j = 0; j < membersList.get(i).size(); j++) {
                members.append(membersList.get(i).get(j).getName()).append(" ");
            }
            RoomData roomData = new RoomData(names.get(i), members.toString());
            mHomeRoomAdapter.addItem(roomData);
        }
        //mHomeRoomAdapter.notifyDataSetChanged();
    }

    /**
     * 일정 보여주기
     */
    @Override
    public void showCalendarList(ArrayList<String> todayList, ArrayList<String> nextList) {
        // 오늘 일정
        if (todayList.size() == 0) {
            mTodayCalendarText.setText("오늘 일정이 없습니다.");
        } else {
            StringBuilder today = new StringBuilder();
            for (int i = 0; i < todayList.size() && i < 10; i++) {
                // 일정이 10개 이하인 경우까지만
                today.append(todayList.get(i)).append("\n");
            }
            if (todayList.size() >= 11) {
                today.append("...");
            }
            mTodayCalendarText.setText(today);
        }

        // 다음 일정
        if (nextList.size() == 0) {
            mNextCalendarText.setText("다음 일정이 없습니다.");
        } else {
            StringBuilder nextDay = new StringBuilder();
            for (int i = 0; i < nextList.size() && i < 10; i++) {
                // 일정이 10개 이하인 경우까지만
                nextDay.append(nextList.get(i)).append("\n");
            }
            if (nextList.size() >= 11) {
                nextDay.append("...");
            }
            mNextCalendarText.setText(nextDay);
        }
    }

    @Override
    public void showWeekHeader() {
        @SuppressLint("SetTextI18n")
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");

        mCalendarDay1.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay2.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay3.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay4.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay5.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay6.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
        mCalendarDay7.setText(simpleDateFormat.format(date));
        date.setTime(date.getTime() + (24 * 60 * 60 * 1000));
    }

    @Override
    public void showCalendar(SchedulePostResult schedulePostResult) {
        List<MemoryDAO> responseValueList = schedulePostResult.getResponse();
        ArrayList<String> todayList = new ArrayList<>();
        ArrayList<String> nextList = new ArrayList<>();

        if (responseValueList != null) {
            for (int i = 0; i < responseValueList.size(); i++) {
                todayList.add(responseValueList.get(i).getName());
                nextList.add(responseValueList.get(i).getName());
            }
        }

        showCalendarList(todayList, nextList);
    }

    @Override
    public void showRoomData(RoomPostResult roomPostResult) {
        List<RoomResponseValue> responseValueList = roomPostResult.getResponseValueList();
        ArrayList<String> names = new ArrayList<>();
        List<List<FriendDAO>> membersList = new ArrayList<>();

        if (responseValueList != null) {
            for (int i = 0; i < responseValueList.size(); i++) {
                names.add(responseValueList.get(i).getName());
                membersList.add(responseValueList.get(i).getMemberList());
            }
        }

        showRoomList(names, membersList);
    }
}
