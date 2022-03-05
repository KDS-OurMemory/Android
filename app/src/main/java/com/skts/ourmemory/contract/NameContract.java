package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.UserPostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class NameContract {
    public interface Model extends BaseContract.Model {
        void getUserData(int userId, String userName, CompositeDisposable compositeDisposable);

        void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);

        void showUserList(int userId, FriendPostResult friendPostResult);

        Context getAppContext();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void getUserName(String name);

        void getUserNameResult(FriendPostResult friendPostResult);

        void requestFriend(int friendId);

        void getRequestFriendResult(UserPostResult userPostResult);
    }
}
