package com.skts.ourmemory.presenter;

import android.os.SystemClock;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.model.signup.SignUpModel;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.signup.SignUpPost;
import com.skts.ourmemory.util.DebugLog;

import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpPresenter implements SignUpContract.Presenter {

    private final String TAG = SignUpPresenter.class.getSimpleName();

    private final SignUpContract.Model mSignUpModel;
    private SignUpContract.View mView;

    private long mLastClickTime = 0;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*사용자 정보*/
    public String mUserID;                     // id
    public String mUserName;                   // 이름(=별명)
    public String mUserBirthday;               // 생일
    public boolean mUserBirthdayType;          // 양/음력
    public boolean mUserBirthdayOpen;          // 생일 공개 여부
    public int mUserLoginType;                 // 로그인 형식(1: 카카오, 2: 구글, 3: 네이버)

    public SignUpPresenter(String userID, String userName, String userBirthday, int userLoginType) {
        this.mSignUpModel = new SignUpModel(this);
        this.mUserID = userID;
        this.mUserName = userName;
        this.mUserBirthday = userBirthday;
        this.mUserLoginType = userLoginType;

        DebugLog.i(TAG, "아이디 : " + mUserID + ", 이름 : " + mUserName + ", 생일 : " + mUserBirthday + ", 로그인 유형 : " + mUserLoginType);
    }

    @Override
    public void setView(SignUpContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
    }

    @Override
    public boolean isDuplicate() {
        // 중복 발생x
        if (SystemClock.elapsedRealtime() - mLastClickTime > 500) {
            mLastClickTime = SystemClock.elapsedRealtime();
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        // 중복 발생o
        return true;
    }

    @Override
    public void init() {
        if (mUserName != null) {
            mView.setText(mUserName);
        }

        if (mUserBirthday != null) {
            if (mUserBirthday.contains("-")) {
                //네이버는 - 포함, "-" 제거
                mUserBirthday = mUserBirthday.replaceAll("-", "");
            }
            final Calendar calendar = Calendar.getInstance();
            int birthdayYear = calendar.get(Calendar.YEAR);
            int birthdayMonth = Integer.parseInt(mUserBirthday.substring(0, 2)) - 1;        // 월은 -1 해줘야 함
            int birthdayDayOfMonth = Integer.parseInt(mUserBirthday.substring(2));

            mView.initBirthday(birthdayYear, birthdayMonth, birthdayDayOfMonth);
        }
    }

    @Override
    public void checkUserData(String userName, DatePicker dpUserBirthday, RadioGroup rgUserBirthdayType, RadioGroup rgUserBirthdayOpen, int solarID, int publicID) {
        if (userName.equals("")) {
            mView.showToast("이름을 입력해주세요.");
            return;
        }

        mUserBirthday = String.format("%02d", dpUserBirthday.getMonth() + 1);
        mUserBirthday += String.format("%02d", dpUserBirthday.getDayOfMonth());
        if (rgUserBirthdayType.getCheckedRadioButtonId() == solarID) {
            // 양력
            mUserBirthdayType = true;
        } else {
            // 음력
            mUserBirthdayType = false;
        }
        if (rgUserBirthdayOpen.getCheckedRadioButtonId() == publicID) {
            // 공개
            mUserBirthdayOpen = true;
        } else {
            // 비공개
            mUserBirthdayOpen = false;
        }

        mView.showAlertDialog();
    }

    /**
     * 회원가입 요청
     */
    @Override
    public void serverTask() {
        if (!isDuplicate()) {
            // 중복 클릭 x
            mSignUpModel.setSignUpData(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType, mCompositeDisposable);
        }
    }

    /**
     * 서버 응답 결과 받는 함수
     */
    @Override
    public void getServerResult(int result) {
        mView.dismissProgressDialog();
        if (result == ServerConst.ON_NEXT) {
            // onNext

        } else if (result == ServerConst.ON_COMPLETE) {
            // success
            mView.startMainActivity();
        } else {
            // Error
            mView.showToast("서버와 통신이 실패했습니다. 다시 시도해주세요.");
        }
    }
}
