package com.skts.ourmemory.contract;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.skts.ourmemory.BaseContract;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SignUpContract {

    public interface Model extends BaseContract.Model {
        void setSignUpData(String userId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int userLoginType, CompositeDisposable compositeDisposable);
    }

    public interface View extends BaseContract.View {
        void setText(String text);
        void showToast(String message);
        void initBirthday(int birthdayYear, int birthdayMonth, int birthdayDayOfMonth);
        void showAlertDialog();
        void dismissProgressDialog();
        void startMainActivity();
        Context getAppContext();        // context 리턴
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(SignUpContract.View view);

        @Override
        void releaseView();

        @Override
        boolean isDuplicate();

        String getFirebaseToken();

        void init();

        void checkUserData(String userName, DatePicker dpUserBirthday, RadioGroup rgUserBirthdayType, RadioGroup rgUserBirthdayOpen, int solarID, int publicID);

        void serverTask();
        
        void getServerResult(int result);     // 서버 응답 결과 처리 함수
    }
}
