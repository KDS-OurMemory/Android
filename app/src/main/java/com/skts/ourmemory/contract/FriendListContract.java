package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.friend.FriendPostResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class FriendListContract {
    public interface Model extends BaseContract.Model {
    }

    public interface View extends BaseContract.View {
        void showToast(String message);
        Context getAppContext();
        void showFriendList(List<FriendPostResult.ResponseValue> responseValueList);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();
    }
}
