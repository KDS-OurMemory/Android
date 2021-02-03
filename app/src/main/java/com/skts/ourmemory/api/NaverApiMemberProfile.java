package com.skts.ourmemory.api;

import android.content.Context;
import android.os.AsyncTask;

import com.nhn.android.naverlogin.OAuthLogin;
import com.skts.ourmemory.common.ServerConst;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NaverApiMemberProfile extends AsyncTask<Void, Void, StringBuffer> {

    private final String TAG = NaverApiMemberProfile.class.getSimpleName();

    private Context mContext;
    private OAuthLogin mOAuthLogin;
    private String mToken;

    public NaverApiMemberProfile(Context context, OAuthLogin oAuthLogin, String accessToken) {
        this.mContext = context;
        this.mOAuthLogin = oAuthLogin;
        this.mToken = accessToken;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(StringBuffer result) {
        super.onPostExecute(result);
    }

    @Override
    protected StringBuffer doInBackground(Void... voids) {
        try {
            URL url = new URL(ServerConst.NAVER_LOGIN_API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", ServerConst.NAVER_LOGIN_API_HEADER + mToken);
            int responseCode = con.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? con.getInputStream() : con.getErrorStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
