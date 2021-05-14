package com.skts.ourmemory.model.addschedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.util.DebugLog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddScheduleModel implements AddScheduleContract.Model {
    private final String TAG = AddScheduleModel.class.getSimpleName();

    private final AddScheduleContract.Presenter mPresenter;

    /*Constructor*/
    public AddScheduleModel(AddScheduleContract.Presenter addSchedulePresenter) {
        this.mPresenter = addSchedulePresenter;
    }

    /**
     * 일정 추가 요청
     */
    @Override
    public void setAddScheduleData(int userId, String name, List<Integer> members, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, List<Integer> shareRooms, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        AddSchedulePost addSchedulePost = new AddSchedulePost(userId, name, members, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor, shareRooms);
        Observable<AddSchedulePostResult> observable = service.postAddScheduleData(addSchedulePost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddSchedulePostResult>() {
                                   String resultCode;
                                   String message;
                                   int memoryId;
                                   int roomId;
                                   String addDate;

                                   @Override
                                   public void onNext(@NonNull AddSchedulePostResult addSchedulePostResult) {
                                       DebugLog.i(TAG, addSchedulePostResult.toString());
                                       resultCode = addSchedulePostResult.getResultCode();
                                       message = addSchedulePostResult.getMessage();
                                       AddSchedulePostResult.ResponseValue responseValue = addSchedulePostResult.getResponse();
                                       memoryId = responseValue.getMemoryId();
                                       roomId = responseValue.getRoomId();
                                       addDate = responseValue.getAddDate();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mPresenter.getAddScheduleResultFail();       // fail
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "Success");
                                       mPresenter.getAddScheduleResultSuccess(resultCode, message, memoryId, roomId, addDate);
                                   }
                               }

                ));
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
