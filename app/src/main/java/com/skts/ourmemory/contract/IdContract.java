package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class IdContract {
    public interface Model extends BaseContract.Model {
        void getUserData(int userId, int findId, CompositeDisposable compositeDisposable);

        void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable);

        void cancelFriendData(int userId, int friendId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);

        void showUserList(int userId, FriendPostResult friendPostResult);

        Context getAppContext();

        void setMySelf();                       // 본인 표시 UI

        void setFriendRequest();                // 친구 요청 UI

        void setFriendWait();                   // 친구 요청 대기 UI

        void setFriendRequestedBy();            // 친구 요청 받은 상태 UI

        void setFriendBlock();                  // 차단 친구 UI
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void getUserId(int userId);

        void getUserIdResult(FriendPostResult friendPostResult);

        void requestFriend(int friendId);

        void getRequestFriendResult(UserPostResult userPostResult);

        void cancelFriend(int friendId);

        void getCancelFriendResult(BasicResponsePostResult basicResponsePostResult);
    }
}
