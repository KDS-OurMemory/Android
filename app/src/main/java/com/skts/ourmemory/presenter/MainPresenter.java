package com.skts.ourmemory.presenter;

import android.database.sqlite.SQLiteDatabase;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.database.DBConst;
import com.skts.ourmemory.database.DBRoomHelper;
import com.skts.ourmemory.model.main.MainModel;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = MainPresenter.class.getSimpleName();

    private final MainContract.Model mModel;
    private MainContract.View mView;
    private MySharedPreferences mMySharedPreferences;

    // DB
    private DBRoomHelper mDbRoomHelper;
    private SQLiteDatabase mSqLiteDatabase;

    /*RxJava*/
    private CompositeDisposable mCompositeDisposable;

    public MainPresenter() {
        this.mModel = new MainModel(this);
    }

    // Thread
    private RoomThread mRoomThread;
    private boolean threadFlag;

    @Override
    public void setView(MainContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
        mDbRoomHelper = new DBRoomHelper(view.getAppContext(), DBConst.DB_NAME, null, DBConst.DB_VERSION);
        mSqLiteDatabase = mDbRoomHelper.getWritableDatabase();
        mDbRoomHelper.onCreate(mSqLiteDatabase);
    }

    @Override
    public void releaseView() {
        if (mRoomThread != null) {
            threadFlag = false;
        }

        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void getRoomList() {
        // TODO
        threadFlag = true;
        mRoomThread = new RoomThread();
        mRoomThread.start();
    }

    @Override
    public void getRoomListResult(RoomPostResult roomPostResult) {
        if (roomPostResult == null) {
            mView.showToast("방 생성 요청 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (roomPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "방 목록 조회 성공");

            // 내장 DB 저장
            mDbRoomHelper.onInsertRoomData(roomPostResult, mSqLiteDatabase);

        } else {
            mView.showToast(roomPostResult.getMessage());
        }
    }


    public class RoomThread extends Thread {
        public RoomThread() {
        }

        @Override
        public void run() {
            int count = 0;
            mCompositeDisposable = new CompositeDisposable();
            int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

            while (threadFlag) {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DebugLog.e("testtt", "" + count);
                if (count % 30 == 0) {
                    mModel.getRoomListData(userId, mCompositeDisposable);
                }
            }
        }
    }
}
