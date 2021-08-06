package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.MyPageContract;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.presenter.MyPagePresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MySharedPreferences;
import com.skts.ourmemory.view.BaseFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyPageFragment extends BaseFragment implements MyPageContract.View {
    private Unbinder unbinder;
    private final MyPageContract.Presenter mPresenter;
    private Context mContext;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_fragment_main_my_page)
    Toolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_activity_user_setting_profile_image)
    ImageView mProfileImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_user_setting_id)
    TextView mUserId;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_user_setting_nickname)
    TextView mUserNickName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_main_my_page_birthday)
    TextView mBirthdayText;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rg_activity_user_setting_birthday_is_solar)
    RadioGroup mBirthdayRadioGroup;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rb_activity_user_setting_solar)
    RadioButton mSolarRadioBtn;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rb_activity_user_setting_lunar)
    RadioButton mLunarRadioBtn;
    @SuppressLint({"NonConstantResourceId", "UseSwitchCompatOrMaterialCode"})
    @BindView(R.id.switch_activity_user_setting_push_alarm)
    Switch mPushAlarmSwitch;
    @SuppressLint({"NonConstantResourceId", "UseSwitchCompatOrMaterialCode"})
    @BindView(R.id.switch_activity_user_setting_request_friend_alarm)
    Switch mRequestFriendAlarmSwitch;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_activity_user_setting_login_type)
    TextView mLoginType;

    /*Dialog*/
    private AlertDialog mAlertDialog;

    public MyPageFragment() {
        mPresenter = new MyPagePresenter();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_my_page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_my_page, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        mContext = Objects.requireNonNull(container).getContext();

        mPresenter.setView(this);

        initView(rootView);
        initSet();

        return rootView;
    }

    @Override
    public void onDestroy() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        mPresenter.releaseView();

        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    private void initView(View view) {
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("");
    }

    private void initSet() {
        showMyPageData(((MainActivity) Objects.requireNonNull(getActivity())).getMyPageData());
    }

    @Override
    public void showMyPageData(MyPagePostResult myPagePostResult) {
        if (myPagePostResult != null) {
            DebugLog.e("testtt", "" + myPagePostResult.getMessage());
        }
        setMyPageData();
    }

    @Override
    public void setMyPageData() {
        MySharedPreferences mySharedPreferences = mPresenter.getMySharedPreferences();
        mUserId.setText(String.valueOf(mySharedPreferences.getIntExtra(Const.USER_ID)));
        mUserNickName.setText(mySharedPreferences.getStringExtra(Const.USER_NAME));
        String loginTypeValue;
        int loginType = mySharedPreferences.getIntExtra(Const.USER_SNS_TYPE);
        if (loginType == 1) {
            // 카카오
            loginTypeValue = "카카오";
        } else if (loginType == 2) {
            // 구글
            loginTypeValue = "구글";
        } else {
            // 네이버
            loginTypeValue = "네이버";
        }
        mLoginType.setText(loginTypeValue);

        StringBuilder stringBuilder = new StringBuilder();
        String birthday = mySharedPreferences.getStringExtra(Const.USER_BIRTHDAY);
        String solar = "양력";
        String birthdayOpen = "공개";
        if (!mySharedPreferences.getBooleanExtra(Const.USER_IS_SOLAR)) {
            solar = "음력";
        }
        if (!mySharedPreferences.getBooleanExtra(Const.USER_IS_BIRTHDAY_OPEN)) {
            birthdayOpen = "비공개";
        }
        mBirthdayText.setText(stringBuilder.append(birthday).append(" / ").append(solar).append(" / ").append(birthdayOpen));
        mPushAlarmSwitch.setChecked(mySharedPreferences.getBooleanExtra(Const.PUSH_ALARM));
        //mRequestFriendAlarmSwitch.setChecked(mySharedPreferences.getBooleanExtra(Const.REQUEST_FRIEND_ALARM));
    }

    /**
     * 로그아웃
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_fragment_main_my_page_logout)
    void onClickLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        mAlertDialog = builder.create();
        mAlertDialog.setTitle("로그아웃");
        mAlertDialog.setMessage("로그아웃 하시겠습니까?");
        mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Objects.requireNonNull(getActivity()).finish();
            mPresenter.setLogout();         // 로그아웃 시 로그인 타입 및 토큰 삭제
        });
        mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
        mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
        mAlertDialog.show();
    }

    /**
     * 회원 탈퇴
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_fragment_main_my_page_withdrawal)
    void onClickWithdrawal() {
        ((MainActivity) Objects.requireNonNull(getActivity())).startDeleteMyPageActivity();
    }

    /**
     * 수정
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.tv_fragment_main_my_page_edit_btn)
    void onClickEdit() {
        ((MainActivity) Objects.requireNonNull(getActivity())).startEditMyPageActivity();
    }
}
