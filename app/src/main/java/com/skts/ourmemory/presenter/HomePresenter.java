package com.skts.ourmemory.presenter;

import android.content.Context;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.model.main.HomeRoomModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class HomePresenter implements HomeContract.Presenter {
    private final HomeContract.Model mModel;
    private HomeContract.View mView;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable;

    private MySharedPreferences mMySharedPreferences;

    public HomePresenter() {
        mCompositeDisposable = new CompositeDisposable();
        mModel = new HomeRoomModel(this);
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
}
