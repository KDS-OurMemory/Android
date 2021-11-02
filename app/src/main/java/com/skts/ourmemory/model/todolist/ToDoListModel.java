package com.skts.ourmemory.model.todolist;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ToDoListModel implements ToDoListContract.Model {
    private final String TAG = ToDoListModel.class.getSimpleName();
    private final ToDoListContract.Presenter mPresenter;

    public ToDoListModel(ToDoListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void getToDoListData(int userId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<ToDoListPostResult> observable = service.getToDoListData(userId);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ToDoListPostResult>() {
                    ToDoListPostResult toDoListPostResultData;

                    @Override
                    public void onNext(@NonNull ToDoListPostResult toDoListPostResult) {
                        DebugLog.i(TAG, toDoListPostResult.toString());
                        toDoListPostResultData = toDoListPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.getToDoListResult(toDoListPostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "getToDoListData Success");
                        mPresenter.getToDoListResult(toDoListPostResultData);           // Success
                    }
                }));
    }

    @Override
    public void setToDoListData(int userId, String contents, String date, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        ToDoListPost toDoListPost = new ToDoListPost(userId, contents, date);
        Observable<AddToDoListPostResult> observable = service.postToDoListData(toDoListPost);

        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<AddToDoListPostResult>() {
                    AddToDoListPostResult addToDoListPostResultData;

                    @Override
                    public void onNext(@NonNull AddToDoListPostResult addToDoListPostResult) {
                        DebugLog.i(TAG, addToDoListPostResult.toString());
                        addToDoListPostResultData = addToDoListPostResult;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                        mPresenter.setToDoListResult(addToDoListPostResultData);           // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "setToDoListData Success");
                        mPresenter.setToDoListResult(addToDoListPostResultData);           // Success
                    }
                }));
    }

    @Override
    public void putToDoListData(int todoId, String contents, String date, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        EditToDoListPost editToDoListPost = new EditToDoListPost(contents, date);
        Observable<BasicResponsePostResult> observable = service.putToDoListData(todoId, editToDoListPost);

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
                        mPresenter.putToDoListResult(basicResponsePostResultData);       // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "putToDoListData Success");
                        mPresenter.putToDoListResult(basicResponsePostResultData);       // Success
                    }
                }));
    }

    @Override
    public void deleteToDoListData(int userId, int todoId, CompositeDisposable compositeDisposable) {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        Observable<BasicResponsePostResult> observable = service.deleteToDoListData(todoId);

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
                        mPresenter.deleteToDoListResult(basicResponsePostResultData);       // Fail
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.d(TAG, "deleteToDoListData Success");
                        mPresenter.deleteToDoListResult(basicResponsePostResultData);       // Success
                    }
                }));
    }
}
