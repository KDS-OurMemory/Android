package com.skts.ourmemory.contract;

import android.content.Context;

import com.skts.ourmemory.model.BasicResponsePostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DeleteMyPageContract {
    public interface Model extends BaseContract.Model {
        void deleteMyPageData(int userId, CompositeDisposable compositeDisposable);        // 회원 탈퇴
    }

    public interface View extends BaseContract.View {
        Context getAppContext();

        void showToast(String message);

        void initSet();

        void finishView();          // 회원 탈퇴 후 화면 변경
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        void deleteMyData();                    // 마이페이지 데이터 수정

        void deleteMyPageDataResult(BasicResponsePostResult basicResponsePostResult);   // 내 정보 수정 결과

        void deleteSharedPreferencesData();     // Shared Preferences 데이터 삭제
    }
}
