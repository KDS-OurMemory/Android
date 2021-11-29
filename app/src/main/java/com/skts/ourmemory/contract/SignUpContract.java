package com.skts.ourmemory.contract;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.skts.ourmemory.model.user.MyPagePostResult;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class SignUpContract {

    public interface Model extends BaseContract.Model {
        void setSignUpData(String snsId, String userName, String userBirthday, boolean userBirthdayType, boolean userBirthdayOpen, int userLoginType, String firebaseToken, CompositeDisposable compositeDisposable);
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
        void setView(View view);

        @Override
        void releaseView();

        String getUserName();

        String getUserBirthday();

        boolean isUserBirthdayType();

        boolean isUserBirthdayOpen();

        void init();

        void checkUserData(String userName, DatePicker dpUserBirthday, RadioGroup rgUserBirthdayType, RadioGroup rgUserBirthdayOpen, int solarID, int publicID);

        void serverTask();

        void getSignUpResult(MyPagePostResult myPagePostResult);
    }
}
