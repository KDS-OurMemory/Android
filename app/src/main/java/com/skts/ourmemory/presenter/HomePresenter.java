package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.main.HomeRoomModel;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.MySharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.Model mModel;
    private HomeContract.View mView;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable;

    private MySharedPreferences mMySharedPreferences;

    private SimpleDateFormat simpleDateFormat;

    @SuppressLint("SimpleDateFormat")
    public HomePresenter() {
        mCompositeDisposable = new CompositeDisposable();
        mModel = new HomeRoomModel(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Override
    public void setView(HomeContract.View view) {
        mView = view;
        this.mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getRoomList(Context context) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getRoomList(userId, mCompositeDisposable);
    }

    @Override
    public void getRoomListResultFail() {
        mView.showToast("방 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getRoomListResultSuccess(String resultCode, String message, ArrayList<Integer> roomIds, ArrayList<Integer> owners, ArrayList<String> names, ArrayList<String> regDates, ArrayList<Boolean> openedList, List<List<UserDAO>> membersList) {
        if (resultCode.equals(ServerConst.SUCCESS)) {
            // Success
            mView.addRoomList(names, membersList);
        }
    }

    @Override
    public void setCalendarList(SchedulePostResult schedulePostResult) {
        List<SchedulePostResult.ResponseValue> responseValueList = schedulePostResult.getResponse();
        // 오늘에 날짜를 세팅 해준다
        long now = System.currentTimeMillis();
        Date todayDate = new Date(now);
        ArrayList<String> todayList = new ArrayList<>();
        for (int i = 0; i < responseValueList.size(); i++) {
            String startDate = responseValueList.get(i).getStartDate();
            String endDate = responseValueList.get(i).getEndDate();
            try {
                Date date1 = simpleDateFormat.parse(startDate);     // 시작 날짜
                Date date2 = simpleDateFormat.parse(endDate);       // 종료 날짜
                int compare1 = date1.compareTo(todayDate);          // 시작날 비교값이 작거나 같고,
                int compare2 = date2.compareTo(todayDate);          // 종료날 비교값이 크거나 같을 경우
                if (compare1 <= 0 && compare2 >= 0) {
                    // 오늘 일정
                    todayList.add(responseValueList.get(i).getName());
                } else {
                    // 하나라도 시작일 <= 오늘 <= 종료일 이 아닐 경우 break
                    break;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // 오늘 일정 표시
        mView.showCalendarList(todayList);

        // 다음 일정 표시

    }
}
