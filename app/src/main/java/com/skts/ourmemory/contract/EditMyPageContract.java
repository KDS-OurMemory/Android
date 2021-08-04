package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.adapter.RequestFriendListAdapter;
import com.skts.ourmemory.model.UpdatePostResult;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.model.user.MyPageDAO;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class EditMyPageContract {
    public interface Model extends BaseContract.Model {
        void putMyPageData(int userId, CompositeDisposable compositeDisposable, MyPageDAO myPageDAO);        // 내 정보 수정
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void initSet();
        void initValueSaved();                      // 초기값 저장
        boolean isDataChanged();                    // 데이터 변경 여부 체크
        boolean checkBirthday();                    // 생일 입력 양식 체크
        void requestEditMyData();                   // 데이터 변경 요청
        void finishView(boolean changed);           // 변경 후 화면 처리
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        MySharedPreferences getMySharedPreferences();

        void initSet();                 // 초기 설정

        void editMyData(String name, String birthday, boolean birthdaySolar, boolean birthdayOpen, boolean pushAlarm);              // 마이페이지 데이터 수정

        void getMyPageDataResult(UpdatePostResult updatePostResult, MyPageDAO myPageDAO);            // 내 정보 수정 결과
    }
}
