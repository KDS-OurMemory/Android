package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.todolist.EachToDoListPostResult;
import com.skts.ourmemory.model.todolist.ToDoListData;
import com.skts.ourmemory.model.todolist.ToDoListPostResult;
import com.skts.ourmemory.util.ToDoListDialog;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ToDoListContract {
    public interface Model extends BaseContract.Model {
        void getToDoListData(int userId, CompositeDisposable compositeDisposable);                                  // ToDoList 데이터 조회

        void setToDoListData(int userId, String contents, String date, CompositeDisposable compositeDisposable);    // ToDoList 데이터 추가

        void putToDoListData(int todoId, String contents, String date, CompositeDisposable compositeDisposable);    // ToDoList 데이터 수정

        void deleteToDoListData(int userId, int todoId, CompositeDisposable compositeDisposable);                   // ToDoList 데이터 삭제
    }

    public interface View extends BaseContract.View {
        Context getAppContext();

        void showToast(String message);

        void initSet();

        void getToDoListResult(ToDoListPostResult toDoListPostResult);                              // ToDoList 데이터 조회 결과

        List<Object> parseObjectData(List<ToDoListData> listData);                                  // ToDOList 데이터 날짜/데이터 각각 파싱

        List<Object> addSetDate(ToDoListData toDoListData);                                         // ToDoList 데이터 추가
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void initSet();                         // 초기 설정

        List<Integer> getSavedToDoListId();     // toDoId 가져오기

        void getSQLiteData();                   // 내장 DB 에서 ToDoList 데이터 가져오기

        void getToDoListData();                 // 서버에서 ToDoList 데이터 조회

        void getToDoListResult(ToDoListPostResult toDoListPostResult);                              // ToDoList 데이터 조회 결과

        void setToDoListData(ToDoListDialog toDoListDialog, String contents, String date);          // ToDoList 데이터 넘기기

        void setToDoListResult(EachToDoListPostResult eachToDoListPostResult);                      // ToDoList 데이터 추가 결과

        void putToDoListData(ToDoListDialog toDoListDialog, int todoId, String contents, String date);          // ToDoList 데이터 수정

        void putToDoListResult(EachToDoListPostResult eachToDoListPostResult);                      // ToDoList 데이터 수정 결과

        void deleteToDoListData(ToDoListDialog toDoListDialog, int todoId);                         // ToDoList 데이터 삭제

        void deleteToDoListResult(EachToDoListPostResult eachToDoListPostResult);                   // ToDoList 데이터 삭제 결과

        void setSQLiteData(ToDoListData listData);                                                  // 내장 DB에 ToDoList 데이터 설정

        void deleteSQLiteData(int toDoId);                                                          // 내장 DB에 해당 ToDoId 데이터 삭제
    }
}
