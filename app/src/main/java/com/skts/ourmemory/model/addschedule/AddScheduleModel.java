package com.skts.ourmemory.model.addschedule;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.AddScheduleContract;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddScheduleModel implements AddScheduleContract.Model {
    private final String TAG = AddScheduleModel.class.getSimpleName();

    private final AddScheduleContract.Presenter mPresenter;

    /*생성자*/
    public AddScheduleModel(AddScheduleContract.Presenter addSchedulePresenter) {
        this.mPresenter = addSchedulePresenter;
    }

    /**
     * 일정 추가 요청
     */
    @Override
    public void setAddScheduleData(int userId, String name, String contents, String place, String startDate, String endDate, String firstAlarm, String secondAlarm, String bgColor, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        AddSchedulePost addSchedulePost = new AddSchedulePost(userId, name, contents, place, startDate, endDate, firstAlarm, secondAlarm, bgColor);
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
}
