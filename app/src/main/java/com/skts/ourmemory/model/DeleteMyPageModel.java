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
        Observable<DeletePostResult> observable = service.deleteMyPageData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<DeletePostResult>() {
                    DeletePostResult deletePostResultData;

                    @Override
                    public void onNext(@NonNull DeletePostResult deletePostResult) {
                        DebugLog.i(TAG, deletePostResult.toString());
                        deletePostResultData = deletePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.deleteMyPageDataResult(deletePostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "deleteMyPageData Success");
                        mPresenter.deleteMyPageDataResult(deletePostResultData);           // Success
                    }
                }));
    }
}