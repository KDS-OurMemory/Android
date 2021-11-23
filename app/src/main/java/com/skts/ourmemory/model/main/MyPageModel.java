package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MyPageContract;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.util.DebugLog;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyPageModel implements MyPageContract.Model {
    private final String TAG = MyPageModel.class.getSimpleName();
    private final MyPageContract.Presenter mPresenter;

    public MyPageModel(MyPageContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    /**
     * 프로필 업로드
     */
    @Override
    public void putUploadProfile(int userId, CompositeDisposable compositeDisposable, File file) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        //RequestBody requestBody = RequestBody.create(file, MediaType.parse("application/json"));
        MultipartBody.Part body = MultipartBody.Part.createFormData("profileImage", file.getName(), requestBody);
        Observable<MyPagePostResult> observable = service.putProfileData(userId, body);
        //Observable<UploadProfilePostResult> observable = service.putProfileData(userId, requestBody);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyPagePostResult>() {
                    MyPagePostResult myPagePostResultData;

                    @Override
                    public void onNext(@NonNull MyPagePostResult myPagePostResult) {
                        DebugLog.i(TAG, myPagePostResult.toString());
                        myPagePostResultData = myPagePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, "getProfileData: " + e.getMessage());
                        mPresenter.getUploadProfileResult(myPagePostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getProfileData Success");
                        mPresenter.getUploadProfileResult(myPagePostResultData);           // Success
                    }
                }));
    }

    /**
     * 프로필 삭제
     */
    @Override
    public void deleteUploadProfile(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();

        Observable<MyPagePostResult> observable = service.deleteProfileData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyPagePostResult>() {
                    MyPagePostResult myPagePostResultData;

                    @Override
                    public void onNext(@NonNull MyPagePostResult myPagePostResult) {
                        DebugLog.i(TAG, myPagePostResult.toString());
                        myPagePostResultData = myPagePostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, "getProfileData: " + e.getMessage());
                        mPresenter.getDeleteUploadProfileResult(myPagePostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getProfileData Success");
                        mPresenter.getDeleteUploadProfileResult(myPagePostResultData);           // Success
                    }
                }));
    }
}
