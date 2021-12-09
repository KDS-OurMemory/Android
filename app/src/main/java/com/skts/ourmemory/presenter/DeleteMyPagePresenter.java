package com.skts.ourmemory.presenter;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.nhn.android.naverlogin.OAuthLogin;
import com.skts.ourmemory.api.NaverApiDeleteToken;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.DeleteMyPageContract;
import com.skts.ourmemory.model.BasicResponsePostResult;
import com.skts.ourmemory.model.DeleteMyPageModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class DeleteMyPagePresenter implements DeleteMyPageContract.Presenter {
    private final String TAG = DeleteMyPagePresenter.class.getSimpleName();

    private final DeleteMyPageContract.Model mModel;
    private DeleteMyPageContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public DeleteMyPagePresenter() {
        this.mModel = new DeleteMyPageModel(this);
    }

    @Override
    public void setView(DeleteMyPageContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public void deleteMyData() {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        mModel.deleteMyPageData(userId, mCompositeDisposable);
    }

    @Override
    public void deleteMyPageDataResult(BasicResponsePostResult basicResponsePostResult) {
        if (basicResponsePostResult == null) {
            mView.showToast("회원 탈퇴 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (basicResponsePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "회원 탈퇴 성공");
            int snsType = mMySharedPreferences.getIntExtra(Const.USER_SNS_TYPE);
            if (snsType == 1) {
                // 카카오
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
            } else if (snsType == 2) {
                // 구글
                FirebaseAuth.getInstance().signOut();
            } else {
                // 네이버
                OAuthLogin oAuthLogin = OAuthLogin.getInstance();
                if (oAuthLogin != null) {
                    Context context = mView.getAppContext();
                    oAuthLogin.logout(context);
                    new NaverApiDeleteToken(context, oAuthLogin).execute();
                }
            }

            // 데이터 삭제
            deleteSharedPreferencesData();

            mView.finishView();
            mView.showToast("회원 탈퇴 완료. 그 동안 이용해주셔서 감사드립니다.");
        } else {
            mView.showToast(basicResponsePostResult.getMessage());
        }
    }

    @Override
    public void deleteSharedPreferencesData() {
        mMySharedPreferences.removePreference(Const.USER_ID);
        mMySharedPreferences.removePreference(Const.USER_NAME);
        mMySharedPreferences.removePreference(Const.USER_BIRTHDAY);
        mMySharedPreferences.removePreference(Const.USER_IS_SOLAR);
        mMySharedPreferences.removePreference(Const.USER_IS_BIRTHDAY_OPEN);
        mMySharedPreferences.removePreference(Const.USER_SNS_TYPE);
        mMySharedPreferences.removePreference(Const.PUSH_ALARM);
        mMySharedPreferences.removePreference(Const.ALARM_COUNT);
        mMySharedPreferences.removePreference(Const.FRIEND_REQUEST_COUNT);
    }
}
