package com.skts.ourmemory.model;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.DeleteMyPageContract;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DeleteMyPageModel implements DeleteMyPageContract.Model {
    private final String TAG = DeleteMyPageModel.class.getSimpleName();
    private final DeleteMyPageContract.Presenter mPresenter;

    public DeleteMyPageModel(DeleteMyPageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 회원 탈퇴
     */
    @Override
    public void deleteMyPageData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<BasicResponsePostResult> observable = service.deleteMyPageData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<BasicResponsePostResult>() {
                    BasicResponsePostResult basicResponsePostResultData;

                    @Override
                    public void onNext(@NonNull BasicResponsePostResult basicResponsePostResult) {
                        DebugLog.i(TAG, basicResponsePostResult.toString());
                        basicResponsePostResultData = basicResponsePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.deleteMyPageDataResult(basicResponsePostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "deleteMyPageData Success");
                        mPresenter.deleteMyPageDataResult(basicResponsePostResultData);           // Success
                    }
                }));
    }
}