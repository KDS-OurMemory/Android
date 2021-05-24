package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.HomeContract;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeRoomModel implements HomeContract.Model {
    private final String TAG = HomeRoomModel.class.getSimpleName();
    private final HomeContract.Presenter mPresenter;

    public HomeRoomModel(HomeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getRoomList(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<RoomPostResult> observable = service.getRoomData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RoomPostResult>() {
                                   String resultCode;
                                   String message;
                                   final ArrayList<Integer> roomIds = new ArrayList<>();
                                   final ArrayList<Integer> owners = new ArrayList<>();
                                   final ArrayList<String> names = new ArrayList<>();
                                   final ArrayList<String> regDates = new ArrayList<>();
                                   final ArrayList<Boolean> openedList = new ArrayList<>();
                                   final List<List<RoomPostResult.Member>> membersList = new ArrayList<>();

                                   @Override
                                   public void onNext(@NonNull RoomPostResult roomPostResult) {
                                       DebugLog.i(TAG, roomPostResult.toString());
                                       resultCode = roomPostResult.getResultCode();
                                       message = roomPostResult.getMessage();
                                       List<RoomPostResult.ResponseValue> responseValueList = roomPostResult.getResponseValueList();
                                       if (responseValueList != null) {
                                           for (int i = 0; i < responseValueList.size(); i++) {
                                               roomIds.add(responseValueList.get(i).getRoomId());
                                               owners.add(responseValueList.get(i).getOwner());
                                               names.add(responseValueList.get(i).getName());
                                               regDates.add(responseValueList.get(i).getRegDate());
                                               openedList.add(responseValueList.get(i).isOpened());
                                               membersList.add(responseValueList.get(i).getMemberList());
                                           }
                                       }
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.i(TAG, "Success");
                                       mPresenter.getRoomListResultSuccess(resultCode, message,
                                               roomIds, owners, names, regDates, openedList, membersList);
                                   }
                               }
                ));
    }
}
