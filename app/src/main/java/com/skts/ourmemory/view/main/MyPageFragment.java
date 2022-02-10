package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class MyPageFragment extends BaseFragment implements MyPageContract.View {
    private final String TAG = MyPageFragment.class.getSimpleName();

    private Unbinder unbinder;
    private final MyPageContract.Presenter mPresenter;
    private Context mContext;
    private final int GET_GALLERY_IMAGE = 200;
    private final int GET_CAPTURE_IMAGE = 201;
    private String mCurrentPhotoPath;

    /*Dialog*/
    private AlertDialog mAlertDialog;

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
            DebugLog.e("testtt", "" + myPagePostResult.getResultMessage());
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
        if (url.equals("")) {
            Glide.with(this).load(R.drawable.ic_main_person_24).into(mProfileImage);
            return;
        }
        Glide.with(this).load(url).override(300, 400).circleCrop().into(mProfileImage);
    }

    /**
     * 비트맵을 파일로 변환하는 함수
     */
    @Override
    public File saveBitmapToJpeg(Bitmap bitmap) {
        // 내부저장소 캐시 경로를 받아옵니다.
        File storage = Objects.requireNonNull(getActivity()).getCacheDir();

        // 저장할 파일 이름
        String fileName = "ourMemory.jpg";

        // Storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);

        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream outputStream = new FileOutputStream(tempFile);

            // Compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);

            // 스트림 사용 후 닫아줍니다.
            outputStream.close();

        } catch (FileNotFoundException e) {
            DebugLog.e(TAG, "FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            DebugLog.e(TAG, "IOException : " + e.getMessage());
        }

        return tempFile;
    }

    /**
     * 사진 촬영
     */
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void captureCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 인텐트를 처리 할 카메라 액티비티가 있는지 확인
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            // 촬영한 사진을 저장할 파일 생성
            File photoFile = null;
            try {
                // 임시로 사용할 파일이므로 경로는 캐시폴더로
                File tempDir = getActivity().getCacheDir();

                // 임시촬영파일 세팅
                String imageFileName = "ourMemory";

                File tempFile = File.createTempFile(
                        imageFileName,  /* 파일이름 */
                        ".jpg",         /* 파일형식 */
                        tempDir      /* 경로 */
                );

                // ACTION_VIEW 인텐트를 사용할 경로 (임시파일의 경로)
                mCurrentPhotoPath = tempFile.getAbsolutePath();
                photoFile = tempFile;
            } catch (IOException e) {
                e.printStackTrace();
            }

            //파일이 정상적으로 생성되었다면 계속 진행
            if (photoFile != null) {
                // Uri 가져오기
                Uri photoURI = FileProvider.getUriForFile(mContext, getActivity().getPackageName(), photoFile);
                // 인텐트에 Uri 담기
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                // 인텐트 실행
                startActivityForResult(takePictureIntent, GET_CAPTURE_IMAGE);
            }
        }
    }

    /**
     * 이미지를 회전시키는 함수
     */
    @Override
    public Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
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
        MyBottomSheetDialog bottomSheetDialog = new MyBottomSheetDialog();
        bottomSheetDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "MyBottomSheetDialog");
        bottomSheetDialog.setOnClickListener(new MyBottomSheetDialog.BottomSheetListener() {
            @Override
            public void onClickTakePhoto() {
                // 사진 촬영
                captureCamera();
            }

            @Override
            public void onClickSelectPhoto() {
                // 사진 선택
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }

            @Override
            public void onClickDeletePhoto() {
                // 사진 삭제
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                mAlertDialog = builder.create();
                mAlertDialog.setTitle("프로필 삭제");
                mAlertDialog.setMessage("프로필을 삭제 하시겠습니까?");
                mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mPresenter.deleteUploadProfile();
                });
                mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
                mAlertDialog.show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GET_GALLERY_IMAGE:
                // 사진 선택
                if (data != null && data.getData() != null) {
                    String url = mPresenter.getPath(mContext, data.getData());
                    File file = new File(url);

                    // 서버 전송
                    mPresenter.putUploadProfile(file);
                }
                break;
            case GET_CAPTURE_IMAGE:
                // 사진 촬영
                File file = new File(mCurrentPhotoPath);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                    if (bitmap != null) {
                        ExifInterface exifInterface = new ExifInterface(mCurrentPhotoPath);
                        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap;
                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap = rotate(bitmap, 90);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap = rotate(bitmap, 180);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap = rotate(bitmap, 270);
                                break;

                            case ExifInterface.ORIENTATION_NORMAL:
                            default:
                                rotatedBitmap = bitmap;
                        }

                        // 파일
                        File sendFile = saveBitmapToJpeg(rotatedBitmap);

                        // 서버 전송
                        mPresenter.putUploadProfile(sendFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 파일 삭제
                    if (file.delete()) {
                        DebugLog.e(TAG, "삭제 성공");
                    }
                }
                break;
        }
    }
}
