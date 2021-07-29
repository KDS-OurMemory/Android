package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;

import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.user.UserDAO;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;

    private final SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public HomePresenter() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void setView(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void releaseView() {
        mView = null;
    }

    @Override
    public void getRoomListData(RoomPostResult roomPostResult) {
        List<RoomPostResult.ResponseValue> responseValueList = roomPostResult.getResponseValueList();
        ArrayList<String> names = new ArrayList<>();
        List<List<UserDAO>> membersList = new ArrayList<>();

        if (responseValueList != null) {
            for (int i = 0; i < responseValueList.size(); i++) {
                names.add(responseValueList.get(i).getName());
                membersList.add(responseValueList.get(i).getMemberList());
            }
        }

        mView.showRoomList(names, membersList);
    }

    @Override
    public void getCalendarListData(SchedulePostResult schedulePostResult) {
        List<SchedulePostResult.ResponseValue> responseValueList = schedulePostResult.getResponse();

        ArrayList<String> todayList = new ArrayList<>();        // 오늘 일정
        ArrayList<String> nextList = new ArrayList<>();         // 다음 일정

        for (int i = 0; i < responseValueList.size(); i++) {
            String startDate = responseValueList.get(i).getStartDate();
            String endDate = responseValueList.get(i).getEndDate();
            try {
                // 오늘에 날짜를 세팅 해준다
                String today = simpleDateFormat.format(System.currentTimeMillis());

                Date todayDate = simpleDateFormat.parse(today);
                Date date1 = simpleDateFormat.parse(startDate);     // 시작 날짜
                Date date2 = simpleDateFormat.parse(endDate);       // 종료 날짜
                int compare1 = date1.compareTo(todayDate);
                int compare2 = date2.compareTo(todayDate);
                if (compare1 <= 0 && compare2 >= 0) {               // 시작날 비교값이 작거나 같고, 종료날 비교값이 크거나 같을 경우
                    // 오늘 일정
                    todayList.add(responseValueList.get(i).getName());
                } else if (compare1 > 0) {
                    // 다음 일정
                    nextList.add(responseValueList.get(i).getName());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 오늘 일정 표시
        mView.showCalendarList(todayList, nextList);
    }
}
