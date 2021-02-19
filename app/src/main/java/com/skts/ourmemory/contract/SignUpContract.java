package com.skts.ourmemory.contract;

import android.widget.DatePicker;
import android.widget.RadioGroup;

import com.skts.ourmemory.BaseContract;

public class SignUpContract {

    public interface View extends BaseContract.View {
        void setText(String text);
        void showToast(String message);
        void initBirthday(int birthdayYear, int birthdayMonth, int birthdayDayOfMonth);
        void showAlertDialog();
        void dismissProgressDialog();
        void startMainActivity();
    }

    public interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(SignUpContract.View view);

        @Override
        void releaseView();

        void init();

        void checkUserData(String userName, DatePicker dpUserBirthday, RadioGroup rgUserBirthdayType, RadioGroup rgUserBirthdayOpen, int solarID, int publicID);

        void serverTask();
    }
}
