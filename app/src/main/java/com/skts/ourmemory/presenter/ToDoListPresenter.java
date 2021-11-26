package com.skts.ourmemory.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.ToDoListContract;
import com.skts.ourmemory.database.DBConst;
import com.skts.ourmemory.database.DBToDoListHelper;
import com.skts.ourmemory.model.todolist.EachToDoListPostResult;
import com.skts.ourmemory.model.todolist.ToDoListData;
import com.skts.ourmemory.model.todolist.ToDoListModel;
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;
import com.skts.ourmemory.util.ToDoListDialog;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ToDoListPresenter implements ToDoListContract.Presenter {
    private final String TAG = ToDoListPresenter.class.getSimpleName();

    private final ToDoListContract.Model mModel;
    private ToDoListContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ToDoListDialog mToDoListDialog;
    private DBToDoListHelper mHelper;
    private List<Integer> mSavedToDoListId;

    public ToDoListPresenter() {
        this.mModel = new ToDoListModel(this);
    }

    @Override
    public List<Integer> getSavedToDoListId() {
        return mSavedToDoListId;
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
        getSQLiteData();        // 내장 DB 에서 ToDoList 데이터 조회
        getToDoListData();      // 서버에서 ToDoList 데이터 조회
    }

    @Override
    public void getSQLiteData() {
        mHelper = new DBToDoListHelper(mView.getAppContext(), DBConst.DB_NAME, null, DBConst.DB_VERSION);
        SQLiteDatabase database = mHelper.getWritableDatabase();
        mHelper.onCreate(database);

        Cursor cursor = mHelper.selectData();
        mSavedToDoListId = new ArrayList<>();

        final int toDoId = 1;               // 1번째 index 가 toDoId
        while (cursor.moveToNext()) {
            mSavedToDoListId.add(cursor.getInt(toDoId));
        }

        cursor.close();
    }

    @Override
    public void getToDoListData() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.getToDoListData(userId, mCompositeDisposable);
    }

    @Override
    public void getToDoListResult(ToDoListPostResult toDoListPostResult) {
        if (toDoListPostResult == null) {
            mView.showToast("To-Do List 조회 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (toDoListPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "To-Do List 조회 성공");
            mView.getToDoListResult(toDoListPostResult);
        } else {
            mView.showToast(toDoListPostResult.getMessage());
        }
    }

    @Override
    public void setToDoListData(ToDoListDialog toDoListDialog, String contents, String date) {
        this.mToDoListDialog = toDoListDialog;
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.setToDoListData(userId, contents, date, mCompositeDisposable);
    }

    @Override
    public void setToDoListResult(EachToDoListPostResult eachToDoListPostResult) {
        if (eachToDoListPostResult == null) {
            mView.showToast("To-Do List 추가 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (eachToDoListPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "To-Do List 추가 성공");
            this.mToDoListDialog.addTodoListResult(eachToDoListPostResult);
        } else {
            mView.showToast(eachToDoListPostResult.getMessage());
        }
    }

    @Override
    public void putToDoListData(ToDoListDialog toDoListDialog, int todoId, String contents, String date) {
        this.mToDoListDialog = toDoListDialog;
        mModel.putToDoListData(todoId, contents, date, mCompositeDisposable);
    }

    @Override
    public void putToDoListResult(EachToDoListPostResult eachToDoListPostResult) {
        if (eachToDoListPostResult == null) {
            mView.showToast("To-Do List 수정 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (eachToDoListPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "To-Do List 수정 성공");
            this.mToDoListDialog.putToDoListResult();
        } else {
            mView.showToast(eachToDoListPostResult.getMessage());
        }
    }

    @Override
    public void deleteToDoListData(ToDoListDialog toDoListDialog, int todoId) {
        this.mToDoListDialog = toDoListDialog;
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.deleteToDoListData(userId, todoId, mCompositeDisposable);
    }

    @Override
    public void deleteToDoListResult(EachToDoListPostResult eachToDoListPostResult) {
        if (eachToDoListPostResult == null) {
            mView.showToast("To-Do List 삭제 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (eachToDoListPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "To-Do List 삭제 성공");
            this.mToDoListDialog.deleteToDoListResult();
        } else {
            mView.showToast(eachToDoListPostResult.getMessage());
        }
    }

    @Override
    public void setSQLiteData(ToDoListData listData) {
        boolean state = listData.isFinishState();
        if (state) {
            // 추가
            mHelper.insertData(listData.getToDoListId());
        } else {
            // 삭제
            mHelper.deleteData(listData.getToDoListId());
        }
    }

    @Override
    public void deleteSQLiteData(int toDoId) {
        // 삭제
        mHelper.deleteData(toDoId);
    }
}
