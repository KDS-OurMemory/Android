package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.model.todolist.ToDoListModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ToDoListPresenter implements ToDoListContract.Presenter {
    private final String TAG = ToDoListPresenter.class.getSimpleName();

    private final ToDoListContract.Model mModel;
    private ToDoListContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public ToDoListPresenter() {
        this.mModel = new ToDoListModel(this);
    }

    @Override
    public void setView(ToDoListContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());

        initSet();
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void initSet() {
    }
}
