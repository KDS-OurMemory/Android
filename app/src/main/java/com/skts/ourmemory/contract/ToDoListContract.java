package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.util.AddToDoListDialog;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ToDoListContract {
    public interface Model extends BaseContract.Model {
        void setToDoListData(int userId, String contents, String date, CompositeDisposable compositeDisposable);    // ToDoList 데이터 추가
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void setRecycler();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                 // 초기 설정
        
        void setToDoListData(AddToDoListDialog addToDoListDialog, String contents, String date);     // ToDoList 데이터 넘기기

        void setToDoListResult(ToDoListPostResult toDoListResult);                                  // ToDoList 데이터 추가 결과
    }
}
