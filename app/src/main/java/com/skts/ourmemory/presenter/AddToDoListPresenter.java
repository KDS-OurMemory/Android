package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.AddToDoListContract;
import com.skts.ourmemory.model.todolist.AddToDoListModel;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class AddToDoListPresenter implements AddToDoListContract.Presenter {
    private final String TAG = AddToDoListPresenter.class.getSimpleName();

    private final AddToDoListContract.Model mModel;
    private AddToDoListContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public AddToDoListPresenter() {
        this.mModel = new AddToDoListModel(this);
    }

    @Override
    public void setView(AddToDoListContract.View view) {
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
