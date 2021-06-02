package com.skts.ourmemory.presenter;

import android.database.sqlite.SQLiteDatabase;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.OurMemoryContract;
import com.skts.ourmemory.database.DBConst;
import com.skts.ourmemory.database.DBFriendHelper;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.ourmemory.OurMemoryModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OurMemoryPresenter implements OurMemoryContract.Presenter {
    private final String TAG = OurMemoryPresenter.class.getSimpleName();

    private final OurMemoryContract.Model mModel;
    private OurMemoryContract.View mView;
    private MySharedPreferences mMySharedPreferences;

    /*DB*/
    private DBFriendHelper mDbFriendHelper;
    private SQLiteDatabase mSqLiteDatabase;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public OurMemoryPresenter() {
        mModel = new OurMemoryModel(this);
    }

    @Override
    public void setView(OurMemoryContract.View view) {
        mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
        mDbFriendHelper = new DBFriendHelper(view.getAppContext(), DBConst.DB_NAME, null, DBConst.DB_VERSION);
        mSqLiteDatabase = mDbFriendHelper.getWritableDatabase();
        mDbFriendHelper.onCreate(mSqLiteDatabase);
    }

    @Override
    public void releaseView() {
        mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getFriendList() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getFriendListData(userId, mCompositeDisposable);
    }

    @Override
    public void getFriendListResultFail() {
        mView.showToast("친구 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getFriendListResultSuccess(String resultCode, String message, List<FriendPostResult.ResponseValue> responseValueList) {
        DebugLog.i(TAG, "친구 목록 조회 성공");
        mView.showFriendList(responseValueList);

        // 친구 데이터 삽입
        mDbFriendHelper.onInsertFriendData(responseValueList, mSqLiteDatabase);
    }

    @Override
    public void getRoomList() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getRoomListData(userId, mCompositeDisposable);
    }

    @Override
    public void getRoomListResultFail() {
        mView.showToast("방 목록 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    @Override
    public void getRoomListResultSuccess(String resultCode, String message, List<RoomPostResult.ResponseValue> responseValueList) {
        DebugLog.i(TAG, "방 목록 조회 성공");
        mView.showRoomList(responseValueList);
    }
}
