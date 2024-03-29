package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.icu.util.ChineseCalendar;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.RoomContract;
import com.skts.ourmemory.model.RoomModel;
import com.skts.ourmemory.model.room.EachRoomPostResult;
import com.skts.ourmemory.model.room.RoomResponseValue;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.Calendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class RoomPresenter implements RoomContract.Presenter {
    private final String TAG = RoomPresenter.class.getSimpleName();

    private final RoomContract.Model mModel;
    private RoomContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    // Room information
    private int mRoomId;

    // Calendar
    private int mYear;
    private int mMonth;
    private int mDay;

    public RoomPresenter() {
        this.mModel = new RoomModel(this);
    }

    @Override
    public void setDate(int year, int month, int day) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
    }

    @Override
    public int getYear() {
        return this.mYear;
    }

    @Override
    public int getMonth() {
        return this.mMonth;
    }

    @Override
    public int getDay() {
        return this.mDay;
    }

    @Override
    public void setDay(int day) {
        this.mDay = day;
    }

    @Override
    public int getRoomId() {
        return mRoomId;
    }

    @Override
    public void setRoomId(int roomId) {
        this.mRoomId = roomId;
    }

    @Override
    public void setView(RoomContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        initSet();
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void initSet() {
    }

    @Override
    public void getRoomData(int roomId) {
        mModel.getRoomData(roomId, mCompositeDisposable);
    }

    @Override
    public void getRoomDataResult(EachRoomPostResult eachRoomPostResult) {
        if (eachRoomPostResult == null) {
            mView.showToast("방 데이터 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (eachRoomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            // Success
            DebugLog.i(TAG, "방 데이터 조회 성공");
            RoomResponseValue responseValue = eachRoomPostResult.getResponseValue();
            mView.showCalendar(responseValue);
        } else {
            mView.showToast(eachRoomPostResult.getResultMessage());
        }
    }

    @Override
    public int getLastWeek(int year, int month) {
        int lastWeek;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        lastWeek = calendar.get(Calendar.WEEK_OF_MONTH);
        return lastWeek;
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String convertSolarToLunar() {
        ChineseCalendar calendar = new ChineseCalendar();
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.DAY_OF_MONTH, mDay);

        calendar.setTimeInMillis(cal.getTimeInMillis());

        int y = calendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
        int m = calendar.get(ChineseCalendar.MONTH) + 1;
        int d = calendar.get(ChineseCalendar.DAY_OF_MONTH);

        return m + "." + String.format("%02d", d);
    }
}
