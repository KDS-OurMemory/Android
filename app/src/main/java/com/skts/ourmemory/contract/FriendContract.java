package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.model.friend.FriendPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);        // 친구 데이터
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void setRequestAdapter(RequestFriendListAdapter adapter);               // 친구 요청 어댑터 설정
        void showNoFriend(boolean status);                                      // 친구 목록 없음 표시
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정

        void startFriendPolling();      // 친구 폴링 데이터 받기 시작

        void getPollingData();          // 폴링 데이터 받기

        void getFriendListResult(FriendPostResult friendPostResult);            // 친구 목록 가져오기

        void setFriendData(FriendPostResult friendPostResult);                  // 친구 목록 데이터 설정
    }
}
