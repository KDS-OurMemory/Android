package com.skts.ourmemory.model.friend;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.contract.FriendListContract;
import com.skts.ourmemory.model.addschedule.AddSchedulePost;
import com.skts.ourmemory.model.addschedule.AddSchedulePostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FriendListModel implements FriendListContract.Model {
    private final String TAG = FriendListModel.class.getSimpleName();

    private final FriendListContract.Presenter mPresenter;

    /*Constructor*/
    public FriendListModel(FriendListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 친구 데이터 요청
     *
     * @param userId User id
     */
    @Override
    public void getFriendListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<FriendPostResult> observable = service.getFriendData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<FriendPostResult>() {
                                   String resultCode;
                                   String message;
                                   final ArrayList<Integer> userIds = new ArrayList<>();
                                   final ArrayList<String> names = new ArrayList<>();

                                   @Override
                                   public void onNext(@NonNull FriendPostResult friendPostResult) {
                                       DebugLog.i(TAG, friendPostResult.toString());
                                       resultCode = friendPostResult.getResultCode();
                                       message = friendPostResult.getMessage();
                                       List<FriendPostResult.ResponseValue> responseValueList = friendPostResult.getResponse();
                                       if (responseValueList != null) {
                                           for (int i = 0; i < responseValueList.size(); i++) {
                                               userIds.add(responseValueList.get(i).getUserId());
                                               names.add(responseValueList.get(i).getName());
                                           }
                                       }
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getFriendListResultFail();       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getFriendListResultSuccess(resultCode, message, userIds, names);
                                   }
                               }

                ));
    }
}
