package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.MyPageContract;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.presenter.MyPagePresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.util.MyBottomSheetDialog;
import com.skts.ourmemory.util.MySharedPreferences;
import com.skts.ourmemory.view.BaseFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment extends BaseFragment implements MyPageContract.View{
    private final String TAG = MyPageFragment.class.getSimpleName();

    private Unbinder unbinder;
    private final MyPageContract.Presenter mPresenter;
    private Context mContext;
    private final int GET_GALLERY_IMAGE = 200;

    /*Dialog*/
    private AlertDialog mAlertDialog;
    private BottomSheetDialog mBottomSheetDialog;

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

        initView();
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

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    private void initView() {
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

        // 프로필 이미지 표시
        if (mySharedPreferences.containCheck(Const.PROFILE_IMAGE_URL)) {
            // 저장된 URL 이 있으면
            setProfileImage(mySharedPreferences.getStringExtra(Const.PROFILE_IMAGE_URL));
        }

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

    @Override
    public void setProfileImage(String url) {
        // Glide 로 이미지 표시
        Glide.with(this).load(url).override(300, 400).circleCrop().into(mProfileImage);

        /*Glide.with(this)
                .asBitmap()
                .load(url)
                .optionalCircleCrop()
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        // 서버로부터 받아온 bitmap 을 ourMemory 이름의 jpg 로 변환해 캐시에 저장합니다.
                        saveBitmapToJpeg(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });*/
    }

    /**
     * 비트맵을 캐시에 저장하는 함수
     */
    @Override
    public void saveBitmapToJpeg(Bitmap bitmap) {
        // 내부저장소 캐시 경로를 받아옵니다.
        File storage = Objects.requireNonNull(getActivity()).getCacheDir();

        // 저장할 파일 이름
        String fileName = "ourMemory" + ".jpg";

        // Storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            // Compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            // 스트림 사용 후 닫아줍니다.
            outputStream.close();

        } catch (FileNotFoundException e) {
            DebugLog.e(TAG, "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            DebugLog.e(TAG, "IOException : " + e.getMessage());
        }

        getBitmapFromCacheDir();
    }

    /**
     * 캐시로부터 비트맵 이미지를 가져오는 함수
     */
    @Override
    public void getBitmapFromCacheDir() {
        // ourMemory 가 들어간 파일들을 저장할 배열입니다.
        ArrayList<String> ourMemories = new ArrayList<>();
        File file = new File(Objects.requireNonNull(getActivity()).getCacheDir().toString());
        File[] files = file.listFiles();
        for (File tempFile : files) {
            DebugLog.i(TAG, tempFile.getName());

            // ourMemory 가 들어가 있는 파일명을 찾습니다.
            if (tempFile.getName().contains("ourMemory")) {
                ourMemories.add(tempFile.getName());
            }
        }
        DebugLog.i(TAG, "ourMemories size: " + ourMemories.size());
        if (ourMemories.size() > 0) {
            int randomPosition = new Random().nextInt(ourMemories.size());

            // ourMemories 배열에 있는 파일 경로 중 하나를 랜덤으로 불러옵니다.
            String path = getActivity().getCacheDir() + "/" + ourMemories.get(randomPosition);

            // 파일 경로로부터 비트맵을 생성합니다.
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            mProfileImage.setImageBitmap(bitmap);
        }
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

    /**
     * 이미지 변경
     */
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.iv_activity_user_setting_profile_image)
    void onClickChangeImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        //intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, GET_GALLERY_IMAGE);

        /*mBottomSheetDialog = new BottomSheetDialog(mContext);
        mBottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet_profile);
        mBottomSheetDialog.show();*/

        /*// 사진 촬영
        dialogView.findViewById(R.id.tv_dialog_bottom_sheet_profile_take_photo).setOnClickListener(view -> {

        });
        // 사진 선택
        dialogView.findViewById(R.id.tv_dialog_bottom_sheet_profile_select_photo).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            //intent.setAction(Intent.ACTION_GET_CONTENT);
            //intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, GET_GALLERY_IMAGE);
        });
        // 취소
        dialogView.findViewById(R.id.tv_dialog_bottom_sheet_profile_cancel).setOnClickListener(view -> {
            mBottomSheetDialog.dismiss();
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            String url = mPresenter.getPath(mContext, data.getData());
            File file = new File(url);
            mPresenter.putUploadProfile(file);

            /*Uri uri = data.getData();
            mProfileImage.setImageURI(uri);*/
        }
    }
}
