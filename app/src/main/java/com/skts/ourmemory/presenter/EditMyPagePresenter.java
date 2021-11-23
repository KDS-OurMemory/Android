package com.skts.ourmemory.presenter;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.EditMyPageContract;
import com.skts.ourmemory.model.EditMyPageModel;
import com.skts.ourmemory.model.user.MyPagePost;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class EditMyPagePresenter implements EditMyPageContract.Presenter {
    private final String TAG = EditMyPagePresenter.class.getSimpleName();

    private final EditMyPageContract.Model mModel;
    private EditMyPageContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public EditMyPagePresenter() {
        this.mModel = new EditMyPageModel(this);
    }

    @Override
    public MySharedPreferences getMySharedPreferences() {
        return mMySharedPreferences;
    }

    @Override
    public void setView(EditMyPageContract.View view) {
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
    public void editMyData(String name, String birthday, boolean birthdaySolar, boolean birthdayOpen, boolean pushAlarm) {
        int userId = mMySharedPreferences.getIntExtra(Const.USER_ID);
        MyPagePost myPagePost = new MyPagePost(name, birthday, birthdaySolar, birthdayOpen, pushAlarm);
        mModel.putMyPageData(userId, mCompositeDisposable, myPagePost);
    }

    @Override
    public void getMyPageDataResult(MyPagePostResult myPagePostResult, MyPagePost myPagePost) {
        if (myPagePostResult == null) {
            mView.showToast("정보 수정 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (myPagePostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            DebugLog.i(TAG, "정보 수정 성공");
            mView.showToast("수정 완료");

            mMySharedPreferences.putStringExtra(Const.USER_NAME, myPagePost.getName());
            mMySharedPreferences.putStringExtra(Const.USER_BIRTHDAY, myPagePost.getBirthday());
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_SOLAR, myPagePost.isSolar());
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_BIRTHDAY_OPEN, myPagePost.isBirthdayOpen());
            mMySharedPreferences.putBooleanExtra(Const.PUSH_ALARM, myPagePost.isPush());

            mView.finishView(true);
        } else {
            mView.showToast(myPagePostResult.getMessage());
        }
    }
}
