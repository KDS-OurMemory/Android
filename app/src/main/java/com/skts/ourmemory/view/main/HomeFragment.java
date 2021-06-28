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
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.room.RoomData;
import com.skts.ourmemory.presenter.HomePresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private HomeRoomAdapter mHomeRoomAdapter;
    private final HomeContract.Presenter mPresenter;
    private Context mContext;
    private TextView mTodayCalendarText;
    private TextView mNextCalendarText;

    // 다가오는 일정
    private TextView mCalendarDay1;
    private TextView mCalendarDay2;
    private TextView mCalendarDay3;
    private TextView mCalendarDay4;
    private TextView mCalendarDay5;
    private TextView mCalendarDay6;
    private TextView mCalendarDay7;
    private TextView mCalendar1;
    private TextView mCalendar2;
    private TextView mCalendar3;
    private TextView mCalendar4;
    private TextView mCalendar5;
    private TextView mCalendar6;
    private TextView mCalendar7;

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
        RecyclerView recyclerView = view.findViewById(R.id.rv_fragment_main_home_recyclerview);
        mTodayCalendarText = view.findViewById(R.id.tv_fragment_main_home_today_calendar_text);
        mNextCalendarText = view.findViewById(R.id.tv_fragment_main_home_next_calendar_text);

        mCalendarDay1 = view.findViewById(R.id.tv_fragment_main_home_1day);
        mCalendarDay2 = view.findViewById(R.id.tv_fragment_main_home_2day);
        mCalendarDay3 = view.findViewById(R.id.tv_fragment_main_home_3day);
        mCalendarDay4 = view.findViewById(R.id.tv_fragment_main_home_4day);
        mCalendarDay5 = view.findViewById(R.id.tv_fragment_main_home_5day);
        mCalendarDay6 = view.findViewById(R.id.tv_fragment_main_home_6day);
        mCalendarDay7 = view.findViewById(R.id.tv_fragment_main_home_7day);

        mCalendar1 = view.findViewById(R.id.tv_fragment_main_home_1day_calendar);
        mCalendar2 = view.findViewById(R.id.tv_fragment_main_home_2day_calendar);
        mCalendar3 = view.findViewById(R.id.tv_fragment_main_home_3day_calendar);
        mCalendar4 = view.findViewById(R.id.tv_fragment_main_home_4day_calendar);
        mCalendar5 = view.findViewById(R.id.tv_fragment_main_home_5day_calendar);
        mCalendar6 = view.findViewById(R.id.tv_fragment_main_home_6day_calendar);
        mCalendar7 = view.findViewById(R.id.tv_fragment_main_home_7day_calendar);

        // Init layoutManager
        assert container != null;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        // Set layoutManager
        recyclerView.setLayoutManager(linearLayoutManager);

        mHomeRoomAdapter = new HomeRoomAdapter();
        recyclerView.setAdapter(mHomeRoomAdapter);

        mContext = container.getContext();

        mPresenter.setView(this);

        // 일주일 표시
        showWeek();

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
    public void onHiddenChanged(boolean hidden) {
        mPresenter.getData(hidden);
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
    public void showRoomList(ArrayList<String> names, List<List<UserDAO>> membersList) {
        for (int i = 0; i < names.size(); i++) {
            StringBuilder members = new StringBuilder();
            for (int j = 0; j < membersList.get(i).size(); j++) {
                members.append(membersList.get(i).get(j).getName()).append(" ");
            }
            RoomData roomData = new RoomData(names.get(i), members.toString());
            mHomeRoomAdapter.addItem(roomData);
        }
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
    public void showWeek() {
        showWeekHeader();       // 날짜 표시
        showWeekCalendar();     // 일정 표시
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
    public void showWeekCalendar() {
    }
}
