package com.skts.ourmemory.model.main;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.contract.MyMemoryContract;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.util.DebugLog;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MyMemoryModel implements MyMemoryContract.Model {
    private final String TAG = MyMemoryModel.class.getSimpleName();
    private final MyMemoryContract.Presenter mPresenter;

    public MyMemoryModel(MyMemoryContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}
