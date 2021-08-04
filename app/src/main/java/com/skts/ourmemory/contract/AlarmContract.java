package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.adapter.FriendListAdapter;
import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.model.friend.AcceptFriendPostResult;
import com.skts.ourmemory.model.friend.FriendPost;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.UserDAO;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AlarmContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정
    }
}
