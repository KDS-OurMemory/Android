package com.skts.ourmemory.contract;

import android.content.Context;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendListContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void showFriendList(ArrayList<Integer> userIds, ArrayList<String> names);
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
        void getFriendListResultSuccess(String resultCode, String message, ArrayList<Integer> userIds, ArrayList<String> names);
    }
}
