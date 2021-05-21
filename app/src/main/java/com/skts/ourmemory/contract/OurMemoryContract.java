package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.friend.FriendPostResult;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class OurMemoryContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void showFriendList(List<FriendPostResult.ResponseValue> responseValueList);
        void startAddFriendActivity();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void getFriendList();

        // 친구 목록 가져오기 실패
        void getFriendListResultFail();

        // 친구 목록 가져오기 성공
        void getFriendListResultSuccess(String resultCode, String message, List<FriendPostResult.ResponseValue> responseValueList);
    }
}
