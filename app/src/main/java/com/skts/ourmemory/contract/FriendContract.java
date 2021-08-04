package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.model.friend.AcceptFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.UserDAO;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendContract {
    public interface Model extends BaseContract.Model {
        void getFriendListData(int userId, CompositeDisposable compositeDisposable);                // 친구 데이터
        void postAcceptFriend(FriendPost friendPost, CompositeDisposable compositeDisposable, UserDAO userDAO);      // 친구 요청 수락
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void initAdapter(RequestFriendListAdapter adapter, FriendListAdapter friendListAdapter);
        void setRequestAdapter(RequestFriendListAdapter adapter, FriendListAdapter friendListAdapter);               // 친구 요청 어댑터 설정
        void showNoFriend(boolean status);                                      // 친구 목록 없음 표시
        void showRequestFriendNumber(int count);                                // 요청 친구 수 표시
        void hideRequestFriend();                                               // 친구 요청 목록 숨기기
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정

        boolean isRequestArrowCollapsible();

        void setRequestArrowCollapsible(boolean requestArrowCollapsible);

        RequestFriendListAdapter getAdapter();

        void startFriendPolling();      // 친구 폴링 데이터 받기 시작

        void getPollingData();          // 폴링 데이터 받기

        void getFriendListResult(FriendPostResult friendPostResult);            // 친구 목록 가져오기

        void setFriendData(FriendPostResult friendPostResult);                  // 친구 목록 데이터 설정

        void requestAcceptFriend(UserDAO userDAO);     // 친구 요청 수락
        
        void getAcceptFriendResult(AcceptFriendPostResult acceptFriendPostResult, UserDAO userDAO);      // 친구 요청 수락 결과

        void setAcceptFriend(UserDAO userDAO);          // 친구 수락 설정
    }
}
