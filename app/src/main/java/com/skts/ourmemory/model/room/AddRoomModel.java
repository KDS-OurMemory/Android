package com.skts.ourmemory.model.room;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddRoomContract;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddRoomModel implements AddRoomContract.Model {
    private final String TAG = AddRoomModel.class.getSimpleName();

    private final AddRoomContract.Presenter mPresenter;

    public AddRoomModel(AddRoomContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setCreateRoomData(String roomName, int userId, ArrayList<Integer> friendIdList, boolean openedRoom, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        CreateRoomPost createRoomPost = new CreateRoomPost(roomName, userId, friendIdList, openedRoom);
        Observable<CreateRoomPostResult> observable = service.postRoomData(createRoomPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<CreateRoomPostResult>() {
                                   CreateRoomPostResult createRoomPostResultData;

                                   @Override
                                   public void onNext(@NonNull CreateRoomPostResult createRoomPostResult) {
                                       DebugLog.i(TAG, createRoomPostResult.toString());
                                       createRoomPostResultData = createRoomPostResult;
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.setCreateRoomResult(createRoomPostResultData);       // Fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.setCreateRoomResult(createRoomPostResultData);
                                   }
                               }
                ));
    }
}
