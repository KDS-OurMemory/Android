package com.skts.ourmemory.contract;

import android.content.Context;
import android.net.Uri;

import com.skts.ourmemory.model.UploadProfilePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.util.MySharedPreferences;

import java.io.File;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MyPageContract {
    public interface Model extends BaseContract.Model {
        void putUploadProfile(int userId, CompositeDisposable compositeDisposable, File file);
    }

    public interface View extends BaseContract.View {
        Context getAppContext();
        void showToast(String message);
        void showMyPageData(MyPagePostResult myPagePostResult);
        void setMyPageData();
        void setProfileImage(String url);
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View view);

        @Override
        void releaseView();

        MySharedPreferences getMySharedPreferences();

        void setLogout();           // 로그아웃 설정

        void kakaoLogout();         // 카카오 로그아웃

        void googleLogout();        // 구글 로그아웃

        void naverLogout();         // 네이버 로그아웃

        String getPath(Context context, Uri uri);       // 절대 경로 찾는 함수

        void putUploadProfile(File file);               // 프로필 업로드

        void getUploadProfileResult(UploadProfilePostResult uploadProfilePostResult);       // 업로드 프로필 결과
    }
}
