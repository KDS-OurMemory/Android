package com.skts.ourmemory.presenter;

import android.annotation.SuppressLint;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.SignUpContract;
import com.skts.ourmemory.model.signup.SignUpModel;
import com.skts.ourmemory.model.signup.SignUpPostResult;
import com.skts.ourmemory.model.user.UserDAO;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.Calendar;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SignUpPresenter implements SignUpContract.Presenter {
    private final String TAG = SignUpPresenter.class.getSimpleName();

    private final SignUpContract.Model mSignUpModel;
    private SignUpContract.View mView;
    private MySharedPreferences mMySharedPreferences;
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*사용자 정보*/
    public String mSnsId;                       // sns id
    public String mUserName;                    // 이름(=별명)
    public String mUserBirthday;                // 생일
    public boolean mUserBirthdayType;           // 양/음력
    public boolean mUserBirthdayOpen;           // 생일 공개 여부
    public int mUserLoginType;                  // 로그인 형식(1: 카카오, 2: 구글, 3: 네이버)

    @Override
    public String getUserName() {
        return mUserName;
    }

    @Override
    public String getUserBirthday() {
        return mUserBirthday;
    }

    @Override
    public boolean isUserBirthdayType() {
        return mUserBirthdayType;
    }

    @Override
    public boolean isUserBirthdayOpen() {
        return mUserBirthdayOpen;
    }

    public SignUpPresenter(String snsId, String userName, String userBirthday, int userLoginType) {
        this.mSignUpModel = new SignUpModel(this);
        this.mSnsId = snsId;
        this.mUserName = userName;
        this.mUserBirthday = userBirthday;
        this.mUserLoginType = userLoginType;

        DebugLog.i(TAG, "아이디 : " + mSnsId + ", 이름 : " + mUserName + ", 생일 : " + mUserBirthday + ", 로그인 유형 : " + mUserLoginType);
    }

    @Override
    public void setView(SignUpContract.View view) {
        this.mView = view;
        mMySharedPreferences = MySharedPreferences.getInstance(mView.getAppContext());
    }

    @Override
    public void releaseView() {
        this.mView = null;
        this.mCompositeDisposable.dispose();
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
        String firebaseToken = mMySharedPreferences.getStringExtra(ServerConst.FIREBASE_PUSH_TOKEN);
        mSignUpModel.setSignUpData(mSnsId, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType, firebaseToken, mCompositeDisposable);
    }

    /**
     * 회원가입 결과
     */
    @Override
    public void getSignUpResult(SignUpPostResult signUpPostResult) {
        mView.dismissProgressDialog();

        if (signUpPostResult == null) {
            mView.showToast("회원가입 실패. 서버 통신에 실패했습니다. 다시 시도해주세요.");
        } else if (signUpPostResult.getResultCode().equals(ServerConst.SUCCESS)) {
            // Success
            UserDAO responseValue = signUpPostResult.getResponse();

            // 사용자 정보 저장
            mMySharedPreferences.putIntExtra(Const.USER_ID, responseValue.getUserId());                 // 사용자 id 저장
            mMySharedPreferences.putIntExtra(Const.PRIVATE_ROOM_ID, responseValue.getPrivateRoomId());  // 사용자 개인 방 번호 저장
            mMySharedPreferences.putStringExtra(Const.USER_NAME, mUserName);                            // 이름 저장
            mMySharedPreferences.putStringExtra(Const.USER_BIRTHDAY, mUserBirthday);                    // 생일 저장
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_SOLAR, mUserBirthdayType);               // 양력 여부 저장
            mMySharedPreferences.putBooleanExtra(Const.USER_IS_BIRTHDAY_OPEN, mUserBirthdayOpen);       // 생일 공개 여부 저장
            mMySharedPreferences.putIntExtra(Const.USER_SNS_TYPE, mUserLoginType);                      // 로그인 유형 저장

            // 기본적 초기값 저장
            mMySharedPreferences.putBooleanExtra(Const.PUSH_ALARM, true);       // 초기값 true 저장
            mMySharedPreferences.putIntExtra(Const.ALARM_COUNT, 0);             // 초기값 0 저장
            mMySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, 0);    // 초기값 0 저장

            mView.showToast("회원 가입 성공");
            mView.startMainActivity();
        } else {
            // fail
            mView.showToast(signUpPostResult.getMessage());
        }
    }
}
