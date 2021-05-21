package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.UserDAO;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class NameContract {
    public interface Model extends BaseContract.Model {
        void getUserData(String userName, CompositeDisposable compositeDisposable);
        void addFriendData(int userId, int friendId, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        void showUserList(List<UserDAO> userData);
        Context getAppContext();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void getUserName(String name);

        void getUserNameResultFail();

        void getUserNameResultSuccess(String resultCode, String message, List<UserDAO> userData);

        void requestFriend(int friendId);

        void getRequestFriendResultFail();

        void getRequestFriendResultSuccess(String resultCode, String message, String addDate);
    }
}
