package com.skts.ourmemory.presenter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.skts.ourmemory.api.NaverApiDeleteToken;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.MyPageContract;
import com.skts.ourmemory.model.UploadProfilePostResult;
import com.skts.ourmemory.model.main.MyPageModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.io.File;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class MyPagePresenter implements MyPageContract.Presenter {
    private final String TAG = MyPagePresenter.class.getSimpleName();

    private final MyPageContract.Model mModel;
    private MyPageContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MyPagePresenter() {
        mModel = new MyPageModel(this);
    }

    @Override
    public MySharedPreferences getMySharedPreferences() {
        return mMySharedPreferences;
    }

    @Override
    public void setView(MyPageContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }

    @Override
    public void setLogout() {
        // 토큰 삭제
        int loginType = mMySharedPreferences.getIntExtra(Const.USER_SNS_TYPE);
        if (loginType == 1) {
            // 카카오
            kakaoLogout();
        } else if (loginType == 2) {
            // 구글
            googleLogout();
        } else {
            // 네이버
            naverLogout();
        }

        // 로그인 Type 삭제
        mMySharedPreferences.removePreference(Const.USER_SNS_TYPE);
    }

    @Override
    public void kakaoLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
            }
        });
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(Long result) {
            }
        });
    }

    @Override
    public void googleLogout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void naverLogout() {
        NaverApiDeleteToken naverApiDeleteToken = new NaverApiDeleteToken(mView.getAppContext(), OAuthLogin.getInstance());
        naverApiDeleteToken.execute();
    }

    /**
     * 절대 경로 찾는 함수
     */
    @Override
    public String getPath(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }

    @Override
    public void putUploadProfile(File file) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.putUploadProfile(userId, mCompositeDisposable, file);
    }

    @Override
    public void deleteUploadProfile() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);

        mModel.deleteUploadProfile(userId, mCompositeDisposable);
    }

    @Override
    public void getUploadProfileResult(UploadProfilePostResult uploadProfilePostResult) {
        if (uploadProfilePostResult == null) {
            mView.showToast("프로필 데이터 저장 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (uploadProfilePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "프로필 데이터 저장 성공");
            mView.setProfileImage(uploadProfilePostResult.getResponse().getUrl());
        } else {
            mView.showToast(uploadProfilePostResult.getMessage());
        }
    }

    @Override
    public void getDeleteUploadProfileResult(UploadProfilePostResult uploadProfilePostResult) {
        if (uploadProfilePostResult == null) {
            mView.showToast("프로필 데이터 삭제 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (uploadProfilePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            if (uploadProfilePostResult.getResponse().getUrl().equals("")) {
                DebugLog.i(TAG, "프로필 데이터 삭제 성공");
            }
        } else {
            mView.showToast(uploadProfilePostResult.getMessage());
        }
    }
}
