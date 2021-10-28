package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.model.todolist.ToDoListModel;
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.util.AddToDoListDialog;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ToDoListPresenter implements ToDoListContract.Presenter {
    private final String TAG = ToDoListPresenter.class.getSimpleName();

    private final ToDoListContract.Model mModel;
    private ToDoListContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private AddToDoListDialog mAddToDoListDialog;

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

    @Override
    public void setToDoListData(AddToDoListDialog addToDoListDialog, String contents, String date) {
        this.mAddToDoListDialog = addToDoListDialog;
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.setToDoListData(userId, contents, date, mCompositeDisposable);
    }

    @Override
    public void setToDoListResult(ToDoListPostResult toDoListPostResult) {
        if (toDoListPostResult == null) {
            mView.showToast("To Do List 추가 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (toDoListPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "친구 목록 조회 성공");
            mAddToDoListDialog.todoListResult(toDoListPostResult);
        } else {
            mView.showToast(toDoListPostResult.getMessage());
        }
    }
}
