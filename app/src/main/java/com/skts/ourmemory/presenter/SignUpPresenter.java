package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.model.signup.SignUpModel;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.Calendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SignUpPresenter implements SignUpContract.Presenter {
    private final String TAG = SignUpPresenter.class.getSimpleName();

    private final SignUpContract.Model mSignUpModel;
    private SignUpContract.View mView;

    private long mLastClickTime = 0;

    MySharedPreferences mMySharedPreferences;
    String mFirebaseToken;

    /*RxJava*/
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*사용자 정보*/
    public String mSnsId;                       // sns id
    public String mUserName;                   // 이름(=별명)
    public String mUserBirthday;               // 생일
    public boolean mUserBirthdayType;          // 양/음력
    public boolean mUserBirthdayOpen;          // 생일 공개 여부
    public int mUserLoginType;                 // 로그인 형식(1: 카카오, 2: 구글, 3: 네이버)

    public SignUpPresenter(String snsId, String userName, String userBirthday, int userLoginType) {
        this.mSignUpModel = new SignUpModel(this);
        this.mSnsId = snsId;
        this.mUserName = userName;
        this.mUserBirthday = userBirthday;
        this.mUserLoginType = userLoginType;

        DebugLog.i(TAG, "아이디 : " + mSnsId + ", 이름 : " + mUserName + ", 생일 : " + mUserBirthday + ", 로그인 유형 : " + mUserLoginType);
    }

    @Override
    public String getFirebaseToken() {
        return mFirebaseToken;
    }

    @Override
    public void setView(SignUpContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
        mFirebaseToken = mMySharedPreferences.getStringExtra(ServerConst.FIREBASE_PUSH_TOKEN);       // 토근 넣어줌
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

        if (!mUserBirthday.equals("")) {
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

    @SuppressLint("DefaultLocale")
    @Override
    public void checkUserData(String userName, DatePicker dpUserBirthday, RadioGroup rgUserBirthdayType, RadioGroup rgUserBirthdayOpen, int solarID, int publicID) {
        if (userName.equals("")) {
            mView.showToast("이름을 입력해주세요.");
            return;
        }

        mUserName = userName;

        mUserBirthday = String.format("%02d", dpUserBirthday.getMonth() + 1);
        mUserBirthday += String.format("%02d", dpUserBirthday.getDayOfMonth());
        // 양음력
        mUserBirthdayType = rgUserBirthdayType.getCheckedRadioButtonId() == solarID;
        // 생일 공개 여부
        mUserBirthdayOpen = rgUserBirthdayOpen.getCheckedRadioButtonId() == publicID;

        mView.showAlertDialog();
    }

    /**
     * 회원가입 요청
     */
    @Override
    public void serverTask() {
        if (!isDuplicate()) {
            // 중복 클릭 x
            mSignUpModel.setSignUpData(mSnsId, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType, mCompositeDisposable);
        }
    }

    /**
     * Failed sign up
     */
    @Override
    public void getSignUpResultFail() {
        mView.dismissProgressDialog();
        mView.showToast("회원가입 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
    }

    /**
     * Success sign up
     *
     * @param resultCode result code
     * @param message    message
     * @param userId     user id
     * @param joinDate   join date
     */
    @Override
    public void getSignUpResultSuccess(String resultCode, String message, int userId, String joinDate) {
        mView.dismissProgressDialog();

        if (resultCode.equals(ServerConst.SUCCESS)) {
            // Success
            // save user id
            mMySharedPreferences.putIntExtra(Const.USER_ID, userId);
            mView.showToast("회원 가입 성공");
            mView.startMainActivity();
        } else {
            // fail
            mView.showToast(message);
        }
    }
}
