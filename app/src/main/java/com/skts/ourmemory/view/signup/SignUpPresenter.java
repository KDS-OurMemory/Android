package com.skts.ourmemory.view.signup;

import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.skts.ourmemory.R;
import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.model.signup.ReceiveUserModel;
import com.skts.ourmemory.model.signup.SendUserModel;
import com.skts.ourmemory.util.DebugLog;

import java.util.Calendar;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpPresenter implements SignUpContract.Presenter {

    private final String TAG = SignUpPresenter.class.getSimpleName();

    private SignUpContract.View mView;

    /*RxJava*/
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    /*사용자 정보*/
    String mUserID;                     // id
    String mUserName;                   // 이름(=별명)
    String mUserBirthday;               // 생일
    boolean mUserBirthdayType;          // 양/음력
    boolean mUserBirthdayOpen;          // 생일 공개 여부
    int mUserLoginType;                 // 로그인 형식(1: 카카오, 2: 구글, 3: 네이버)

    public SignUpPresenter(String userID, String userName, String userBirthday, int userLoginType) {
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

    @Override
    public void serverTask() {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        //SendUserModel sendUserModel = new SendUserModel(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType);
        SendUserModel sendUserModel = new SendUserModel(mUserID, mUserName, mUserBirthday, mUserBirthdayType, mUserBirthdayOpen, mUserLoginType, ServerConst.FIREBASE_PUSH_TOKEN);
        Observable<ReceiveUserModel> observable = service.postData(sendUserModel);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ReceiveUserModel>() {
                                   @Override
                                   public void onNext(@NonNull ReceiveUserModel receiveUserModel) {
                                       DebugLog.i(TAG, receiveUserModel.toString());
                                       mView.dismissProgressDialog();
                                   }

                                   @Override
                                   public void onError(@NonNull Throwable e) {
                                       DebugLog.e(TAG, e.getMessage());
                                       mView.dismissProgressDialog();
                                       mView.showToast("서버와 통신이 실패했습니다. 다시 시도해주세요.");
                                   }

                                   @Override
                                   public void onComplete() {
                                       DebugLog.d(TAG, "성공");
                                       mView.dismissProgressDialog();
                                   }
                               }

                ));
    }
}
