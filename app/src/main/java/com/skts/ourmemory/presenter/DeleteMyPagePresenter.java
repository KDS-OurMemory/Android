package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.DeleteMyPageContract;
import com.skts.ourmemory.model.DeleteMyPageModel;
import com.skts.ourmemory.model.user.MyPagePostResult;
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
    public void deleteMyPageDataResult(MyPagePostResult myPagePostResult) {
        if (myPagePostResult == null) {
            mView.showToast("회원 탈퇴 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (myPagePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "회원 탈퇴 성공");
            // 데이터 삭제
            deleteSharedPreferencesData();

            mView.finishView();
            mView.showToast("회원 탈퇴 완료. 그 동안 이용해주셔서 감사드립니다.");
        } else {
            mView.showToast(myPagePostResult.getMessage());
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
