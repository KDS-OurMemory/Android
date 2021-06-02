package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.model.room.AddRoomModel;
import com.skts.ourmemory.model.room.CreateRoomPostResult;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddRoomPresenter implements AddRoomContract.Presenter {
    private final AddRoomContract.Model mModel;
    private AddRoomContract.View mView;

    private MySharedPreferences mMySharedPreferences;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public AddRoomPresenter() {
        this.mModel = new AddRoomModel(this);
    }

    @Override
    public void setView(AddRoomContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void setCreateRoom(String roomName, ArrayList<Integer> friendIdList, boolean openedRoom) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.setCreateRoomData(roomName, userId, friendIdList, openedRoom, mCompositeDisposable);
    }

    @Override
    public void setCreateRoomResult(CreateRoomPostResult createRoomResult) {
        if (createRoomResult == null) {
            mView.showToast("방 생성 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (createRoomResult.getResultCode().equals(ServerConst.SUCCESS)) {
            mView.showToast("방 생성 요청 성공");
            mView.onBackPressed();
        } else {
            mView.showToast(createRoomResult.getMessage());
        }
    }
}
