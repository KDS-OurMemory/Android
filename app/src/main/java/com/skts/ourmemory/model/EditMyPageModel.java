package com.skts.ourmemory.model;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.EditMyPageContract;
import com.skts.ourmemory.model.user.MyPageDAO;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditMyPageModel implements EditMyPageContract.Model {
    private final String TAG = EditMyPageModel.class.getSimpleName();
    private final EditMyPageContract.Presenter mPresenter;

    public EditMyPageModel(EditMyPageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 내 정보 수정
     */
    @Override
    public void putMyPageData(int userId, CompositeDisposable compositeDisposable, MyPageDAO myPageDAO) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<UpdatePostResult> observable = service.putMyPageData(userId, myPageDAO);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UpdatePostResult>() {
                    UpdatePostResult updatePostResultData;

                    @Override
                    public void onNext(@NonNull UpdatePostResult updatePostResult) {
                        DebugLog.i(TAG, updatePostResult.toString());
                        updatePostResultData = updatePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getMyPageDataResult(updatePostResultData, myPageDAO);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "putMyPageData Success");
                        mPresenter.getMyPageDataResult(updatePostResultData, myPageDAO);           // Success
                    }
                }));
    }
}