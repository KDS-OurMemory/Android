package com.skts.ourmemory.server;

import android.content.Context;
import android.os.AsyncTask;

import com.nhn.android.naverlogin.OAuthLogin;
import com.skts.ourmemory.util.DebugLog;

public class NaverApiDeleteToken extends AsyncTask<Void, Void, Void> {

    private final String TAG = NaverApiDeleteToken.class.getSimpleName();

    private Context mContext;
    private OAuthLogin mOAuthLogin;

    public NaverApiDeleteToken(Context context, OAuthLogin oAuthLogin) {
        this.mContext = context;
        this.mOAuthLogin = oAuthLogin;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        boolean isSuccessDeleteToken = mOAuthLogin.logoutAndDeleteToken(mContext);

        if (!isSuccessDeleteToken) {
            // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
            // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
            DebugLog.d(TAG, "errorCode:" + mOAuthLogin.getLastErrorCode(mContext));
            DebugLog.d(TAG, "errorDesc:" + mOAuthLogin.getLastErrorDesc(mContext));
        }

        return null;
    }
}
