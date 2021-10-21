package com.skts.ourmemory.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.skts.ourmemory.api.IRetrofitApi;
import com.skts.ourmemory.api.RetrofitAdapter;
import com.skts.ourmemory.model.notice.NoticePostResult;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class GlobalApplication extends Application {
    private final String TAG = GlobalApplication.class.getSimpleName();
    private static GlobalApplication mInstance;
    public static boolean DEBUG = false;

    // RxJava
    private CompositeDisposable mCompositeDisposable;

    // Thread
    private NoticeThread mNoticeThread;
    private boolean threadFlag;

    // Alarm data
    private List<NoticePostResult.ResponseValue> mAlarmData;

    public static GlobalApplication getGlobalApplicationContext() {
        if (mInstance == null) {
            throw new IllegalStateException("This Application does not inherit com.kakao.GlobalApplication");
        }

        return mInstance;
    }

    public List<NoticePostResult.ResponseValue> getAlarmData() {
        return mAlarmData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // 디버그모드 여부 체크
        // 디버그모드에 따라서 로그를 남기거나 남기지 않는다
        DEBUG = isDebuggable(this);

        // Kakao Sdk 초기화
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInstance = null;

        if (mNoticeThread != null) {
            threadFlag = false;
        }

        mCompositeDisposable.dispose();
    }

    public static class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    // 로그인을 어떤 방식으로 할지 지정
                    // KAKAO_LOGIN_ALL: 모든 로그인방식을 사용하고 싶을때 지정.

                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                    // SDK 로그인시 사용되는 WebView 에서 pause 와 resume 시에 Timer 를 설정하여 CPU 소모를 절약한다.
                    // True 를 리턴할경우 WebView 로그인을 사용하는 화면서 모든 webview 에 onPause 와 onResume 시에 Timer 를 설정해 주어야 한다.
                    // 지정하지 않을 시 false 로 설정된다.
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                    // 로그인시 access token 과 refresh token 을 저장할 때의 암호화 여부를 결정한다.
                }

                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                    // 일반 사용자가 아닌 Kakao 와 제휴된 앱에서만 사용되는 값으로, 값을 채워주지 않을경우 ApprovalType.INDIVIDUAL 값을 사용하게 된다.
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                    // Kakao SDK 에서 사용되는 WebView 에서 email 입력폼의 데이터를 저장할지 여부를 결정한다.
                    // True 일 경우, 다음번에 다시 로그인 시 email 폼을 누르면 이전에 입력했던 이메일이 나타난다.
                }
            };
        }

        // Application 이 가지고 있는 정보를 얻기 위한 인터페이스
        @Override
        public IApplicationConfig getApplicationConfig() {
            return GlobalApplication::getGlobalApplicationContext;
        }
    }

    /*현재 디버그모드 여부를 리턴*/
    private boolean isDebuggable(Context context) {
        boolean isDebuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(), 0);
            isDebuggable = (0 != (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /*debuggable 변수는 false 로 유지될 것이다.*/
        }

        return isDebuggable;
    }

    public void startThread() {
        mAlarmData = new ArrayList<>();

        threadFlag = true;
        mNoticeThread = new NoticeThread();
        mNoticeThread.start();
    }

    public class NoticeThread extends Thread {
        public NoticeThread() {
            mCompositeDisposable = new CompositeDisposable();
        }

        @Override
        public void run() {
            int count = 0;
            int POLLING_TIME = 300;
            getNoticeData();

            while (threadFlag) {
                count++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count % POLLING_TIME == 0) {
                    getNoticeData();
                    count = 0;
                }
            }
        }
    }

    /**
     * 알림 조회
     */
    private void getNoticeData() {
        IRetrofitApi service = RetrofitAdapter.getInstance().getServiceApi();
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(mInstance);
        int userId = mySharedPreferences.getIntExtra(Const.USER_ID);

        Observable<NoticePostResult> observable = service.getNoticeData(userId);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<NoticePostResult>() {

                    @Override
                    public void onNext(@NonNull NoticePostResult noticePostResult) {
                        DebugLog.i(TAG, noticePostResult.toString());
                        mAlarmData = noticePostResult.getResponse();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        DebugLog.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        DebugLog.i(TAG, "알림 조회 성공");
                        setNoticeData(mySharedPreferences);
                    }
                }));
    }

    /**
     * 알림 조회 설정
     */
    private void setNoticeData(MySharedPreferences mySharedPreferences) {
        int alarmCount = 0;
        int friendCount = 0;


        for (NoticePostResult.ResponseValue data : mAlarmData) {
            if (data.getType() == null) {
                alarmCount++;
            } else if (data.getType().equals(ServerConst.FRIEND_REQUEST)) {
                // 친구 요청일 경우
                friendCount++;
            } else {
                alarmCount++;
            }
        }
        mySharedPreferences.putIntExtra(Const.ALARM_COUNT, alarmCount);
        mySharedPreferences.putIntExtra(Const.FRIEND_REQUEST_COUNT, friendCount);
    }
}