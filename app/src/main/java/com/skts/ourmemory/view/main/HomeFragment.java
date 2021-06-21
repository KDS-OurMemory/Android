package com.skts.ourmemory.view.main;

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
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.presenter.HomePresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    private HomeRoomAdapter mHomeRoomAdapter;
    private final HomeContract.Presenter mPresenter;
    private Context mContext;
    private TextView mTodayCalendarText;

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

        showRoomList(mContext);

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
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setCalendarList(SchedulePostResult schedulePostResult) {
        mPresenter.setCalendarList(schedulePostResult);
    }

    @Override
    public void showRoomList(Context context) {
        mPresenter.getRoomList(context);

        /*HomeRoomData homeRoomData = new HomeRoomData("테스트", "오광석");
        HomeRoomData homeRoomData2 = new HomeRoomData("테스트2", "오광석2");
        mHomeRoomAdapter.addItem(homeRoomData);
        mHomeRoomAdapter.addItem(homeRoomData2);*/
    }

    @Override
    public void addRoomList(ArrayList<String> names, List<List<UserDAO>> membersList) {
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
    public void showCalendarList(ArrayList<String> todayList) {
        if (todayList.size() == 0) {
            mTodayCalendarText.setText("오늘 일정이 없습니다.");
            return;
        }
        StringBuilder today = new StringBuilder();
        for (int i = 0; i < todayList.size() && i < 10; i++) {
            // 일정이 10개 미만인 경우까지만
            today.append(todayList.get(i)).append("\n");
        }
        if (todayList.size() >= 10) {
            today.append("...");
        }
        mTodayCalendarText.setText(today);
    }
}
