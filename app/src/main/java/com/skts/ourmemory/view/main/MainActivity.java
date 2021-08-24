package com.skts.ourmemory.view.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.Const;
import com.skts.ourmemory.contract.MainContract;
import com.skts.ourmemory.model.room.RoomPostResult;
import com.skts.ourmemory.model.schedule.AddSchedulePostResult;
import com.skts.ourmemory.model.schedule.SchedulePostResult;
import com.skts.ourmemory.model.user.MyPagePostResult;
import com.skts.ourmemory.presenter.MainPresenter;
import com.skts.ourmemory.util.DebugLog;
import com.skts.ourmemory.view.AddScheduleActivity;
import com.skts.ourmemory.view.AlarmActivity;
import com.skts.ourmemory.view.BaseActivity;
import com.skts.ourmemory.view.DeleteMyPageActivity;
import com.skts.ourmemory.view.EditMyPageActivity;
import com.skts.ourmemory.view.FriendActivity;
import com.skts.ourmemory.view.RoomActivity;
import com.skts.ourmemory.view.ToDoListActivity;
import com.skts.ourmemory.view.addfriend.AddFriendActivity;
import com.skts.ourmemory.view.addroom.AddRoomActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract.View {
    private final String TAG = MainActivity.class.getSimpleName();

    private MainPresenter mMainPresenter;

    /*Fragment*/
    private FragmentManager mFragmentManager;
    private Fragment mHomeFragment;
    private Fragment mOurMemoryFragment;
    private Fragment mMyPageFragment;
    private Fragment mMyMemoryFragment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.nav_activity_main_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_activity_main_bottom_navigation_view_ll)
    LinearLayout mCategoryLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.cl_activity_main_bottom_navigation_view_layout)
    ConstraintLayout mConstraintLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_bottom_navigation_view_friend)
    FrameLayout mFriendLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_bottom_navigation_friend)
    ImageView mFriendImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_bottom_navigation_view_alarm)
    FrameLayout mAlarmLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_bottom_navigation_alarm)
    ImageView mAlarmImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainPresenter = new MainPresenter();
        mMainPresenter.setView(this);

        mFragmentManager = getSupportFragmentManager();

        // 기본 프래그먼트 지정(홈)
        setInitFragment();

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switchFragment(item.getItemId());
            return true;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.releaseView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAlarm();           // 알람 체크
    }

    @Override
    public void onBackPressed() {
        if (mMainPresenter.exitApp()) {
            moveTaskToBack(true);       // 태스크를 백그라운드로 이동
            finishAndRemoveTask();               // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());     // 앱 프로세스 종료
        }
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public void setInitFragment() {
        mHomeFragment = new HomeFragment();
        mFragmentManager.beginTransaction().replace(R.id.fl_activity_main_frame_layout, mHomeFragment).commit();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void switchFragment(int id) {
        switch (id) {
            case R.id.item_activity_main_navigation_home:
                // 홈
                mCategoryLayout.setVisibility(View.GONE);

                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mHomeFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().show(mHomeFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_category:
                // 카테고리
                mCategoryLayout.setVisibility(View.VISIBLE);
                tempCreateExcel();
                break;
            case R.id.item_activity_main_navigation_my_memory:
                // 나의 기억공간
                mCategoryLayout.setVisibility(View.GONE);

                if (mMyMemoryFragment == null) {
                    Display display = getWindowManager().getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int height = point.y;           // 레이아웃 높이

                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    float density = displayMetrics.densityDpi / 160f;

                    mMyMemoryFragment = new MyMemoryFragment(density, height);
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mMyMemoryFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_our_memory:
                // 우리의 기억공간
                mCategoryLayout.setVisibility(View.GONE);

                if (mOurMemoryFragment == null) {
                    mOurMemoryFragment = new OurMemoryFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mOurMemoryFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().show(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyPageFragment).commit();
                }
                break;
            case R.id.item_activity_main_navigation_my_page:
                // 마이페이지
                mCategoryLayout.setVisibility(View.GONE);

                if (mMyPageFragment == null) {
                    mMyPageFragment = new MyPageFragment();
                    mFragmentManager.beginTransaction().add(R.id.fl_activity_main_frame_layout, mMyPageFragment).commit();
                }

                if (mHomeFragment != null) {
                    mFragmentManager.beginTransaction().hide(mHomeFragment).commit();
                }
                if (mMyMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mMyMemoryFragment).commit();
                }
                if (mOurMemoryFragment != null) {
                    mFragmentManager.beginTransaction().hide(mOurMemoryFragment).commit();
                }
                if (mMyPageFragment != null) {
                    mFragmentManager.beginTransaction().show(mMyPageFragment).commit();
                }
                break;
            default:
                DebugLog.e(TAG, "프래그먼트 선택 오류");
                break;
        }
    }

    private void tempCreateExcel() {
        String totalStr = "";
        // 20210714 020607 33.057.1 96 null null null null
        // 20210526083113	17.376.2	96	null	null	null	null

        DebugLog.e("testtt", "엑셀 파일 생성");
        String dir = "";

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String fileDir = "/TEMP_EXCEL/Data";
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + fileDir;
        } else {
            DebugLog.e(TAG, "디렉토리 오류");
        }

        File path = new File(dir);
        if (!path.exists()) {
            if (path.mkdirs()) {
                DebugLog.i(TAG, "디렉토리 만들기");
                ;
            }
        }

        String fileName = "R1238255-62_E7E7F9667C7C_20210714_110501_hyadmin.csv";

        // 파일 생성
        File file = new File(dir + File.separator + fileName);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
            // 컬럼명 입력
            bufferedWriter.write("Time\tTemperature\tHumidity\tBattery\taccelerator-x(mg)\taccelerator-y(mg)\taccelerator-z(mg)\tG");
            //bufferedWriter.write("TimeTemperatureHumidityBatteryaccelerator-x(mg)accelerator-y(mg)accelerator-z(mg)G");
            bufferedWriter.newLine();

            GregorianCalendar gregorianCalendar = new GregorianCalendar(2021, 6, 14, 9, 1, 7);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            for (int i = 0; i < 9608; i++) {
                gregorianCalendar.add(Calendar.MINUTE, 5);
                double randTemp = Math.random() * 2 + 24;
                String randTempStr = String.format("%.1f", randTemp);

                double randHumid = Math.random() * 3 + 87;
                String randHumidStr = String.format("%.1f", randHumid);

                totalStr = dateFormat.format(gregorianCalendar.getTime()) + "\t" + randTempStr + "\t" + randHumidStr + "\t" + "96\tnull\tnull\tnull\tnull";

                bufferedWriter.write(totalStr);
                bufferedWriter.newLine();
            }

            bufferedWriter.write("20210816174200\tFE\tFE\tFE\tFE\tFE\tFE\tFE");

            DebugLog.i(TAG, "파일저장 완료");

            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void checkAlarm() {
        int alarmCount = mMainPresenter.checkAlarmCount();
        int friendRequestCount = mMainPresenter.checkFriendRequestCount();
        int allCount = alarmCount + friendRequestCount;

        BadgeDrawable categoryBadgeDrawable = mBottomNavigationView.getOrCreateBadge(R.id.item_activity_main_navigation_category);
        BadgeDrawable badgeDrawable1 = BadgeDrawable.create(MainActivity.this);
        BadgeDrawable badgeDrawable2 = BadgeDrawable.create(MainActivity.this);

        if (allCount != 0) {
            categoryBadgeDrawable.setVisible(true);
            categoryBadgeDrawable.setNumber(allCount);

            if (alarmCount != 0) {
                badgeDrawable1.setVisible(true);
                badgeDrawable1.setNumber(alarmCount);
                mAlarmLayout.setForeground(badgeDrawable1);
                mAlarmLayout.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> BadgeUtils.attachBadgeDrawable(badgeDrawable1, mAlarmImage, mAlarmLayout));
            }
            if (friendRequestCount != 0) {
                badgeDrawable2.setVisible(true);
                badgeDrawable2.setNumber(friendRequestCount);
                mFriendLayout.setForeground(badgeDrawable2);
                mFriendLayout.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> BadgeUtils.attachBadgeDrawable(badgeDrawable2, mFriendImage, mFriendLayout));
            }
        } else {
            categoryBadgeDrawable.setVisible(false);
            mAlarmLayout.setForeground(null);
            mFriendLayout.setForeground(null);
        }
    }

    @Override
    public void startAddScheduleActivity(SchedulePostResult.ResponseValue responseValue, int year, int month, int day, String purpose) {
        Intent intent = new Intent(this, AddScheduleActivity.class);
        intent.putExtra(Const.CALENDAR_DATA, responseValue);

        intent.putExtra(Const.CALENDAR_YEAR, year);
        intent.putExtra(Const.CALENDAR_MONTH, month);
        intent.putExtra(Const.CALENDAR_DAY, day);
        if (purpose.equals(Const.CALENDAR_ADD)) {
            // 일정 추가 시
            intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_ADD);
        } else {
            // 일정 편집 시
            intent.putExtra(Const.CALENDAR_PURPOSE, Const.CALENDAR_EDIT);
        }
        startActivityForResult(intent, Const.REQUEST_CODE_CALENDAR);
    }

    @Override
    public void startAddRoomActivity() {
        startActivity(new Intent(this, AddRoomActivity.class));
    }

    @Override
    public void startAddFriendActivity() {
        startActivity(new Intent(this, AddFriendActivity.class));
    }

    @Override
    public void startEditMyPageActivity() {
        startActivityForResult(new Intent(this, EditMyPageActivity.class), Const.REQUEST_CODE_EDIT_MY_PAGE);

        // 액티비티 전환 애니메이션 설정
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startDeleteMyPageActivity() {
        startActivityForResult(new Intent(this, DeleteMyPageActivity.class), Const.REQUEST_CODE_DELETE_MY_PAGE);

        // 액티비티 전환 애니메이션 설정
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startRoomActivity(int position) {
        RoomPostResult roomPostResult = mMainPresenter.getRoomPostResult();
        RoomPostResult.ResponseValue responseValue = roomPostResult.getResponseValueList().get(position);

        Intent intent = new Intent(this, RoomActivity.class);
        intent.putExtra(Const.ROOM_DATA, responseValue);
        startActivity(intent);

        // 액티비티 전환 애니메이션 설정
        overridePendingTransition(R.anim.slide_in_right_room, R.anim.slide_out_left_room);
    }

    /**
     * AddScheduleActivity 에서 처리된 결과를 받는 메소드
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == Const.REQUEST_CODE_CALENDAR) {
                // 프래그먼트로 데이터 처리
                AddSchedulePostResult addSchedulePostResult = (AddSchedulePostResult) Objects.requireNonNull(data).getExtras().getSerializable(Const.SCHEDULE_DATA);
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mMyMemoryFragment)) {
                    MyMemoryFragment myMemoryFragment = (MyMemoryFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(myMemoryFragment).updateCalendarData(addSchedulePostResult);
                }
            } else if (requestCode == Const.REQUEST_CODE_EDIT_MY_PAGE) {
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mMyPageFragment)) {
                    MyPageFragment myPageFragment = (MyPageFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(myPageFragment).setMyPageData();
                }
            } else if (requestCode == Const.REQUEST_CODE_DELETE_MY_PAGE) {
                // 회원 탈퇴 성공
                finish();
            }
        }
    }

    @Override
    public FragmentManager getMyFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public RoomPostResult getRoomData() {
        return mMainPresenter.getRoomPostResult();
    }

    @Override
    public SchedulePostResult getScheduleData() {
        return mMainPresenter.getSchedulePostResult();
    }

    @Override
    public MyPagePostResult getMyPageData() {
        return mMainPresenter.getMyPagePostResult();
    }

    @Override
    public void showRoomData() {
        if (mHomeFragment != null) {
            if (!mHomeFragment.isHidden()) {
                // 홈 프래그먼트
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mHomeFragment)) {
                    HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(homeFragment).showRoomData(mMainPresenter.getRoomPostResult());
                }
            }
        }
        if (mOurMemoryFragment != null) {
            if (!mOurMemoryFragment.isHidden()) {
                // OurMemory 프래그먼트
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mOurMemoryFragment)) {
                    OurMemoryFragment ourMemoryFragment = (OurMemoryFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(ourMemoryFragment).showRoomData(mMainPresenter.getRoomPostResult());
                }
            }
        }
    }

    @Override
    public void showScheduleData() {
        if (mMyMemoryFragment != null) {
            if (!mMyMemoryFragment.isHidden()) {
                // MyMemory 프래그먼트
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mMyMemoryFragment)) {
                    MyMemoryFragment myMemoryFragment = (MyMemoryFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(myMemoryFragment).showScheduleData(mMainPresenter.getSchedulePostResult());
                }
            }
        }
    }

    @Override
    public void showMyPageData() {
        if (mMyPageFragment != null) {
            if (!mMyPageFragment.isHidden()) {
                // MyPage 프래그먼트
                if (Objects.equals(getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout), mMyPageFragment)) {
                    MyPageFragment myPageFragment = (MyPageFragment) getSupportFragmentManager().findFragmentById(R.id.fl_activity_main_frame_layout);
                    Objects.requireNonNull(myPageFragment).showMyPageData(mMainPresenter.getMyPagePostResult());
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_bottom_navigation_view_friend)
    void onClickFriendView() {
        startActivity(new Intent(this, FriendActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_bottom_navigation_view_to_do_list)
    void onClickToDoListView() {
        startActivity(new Intent(this, ToDoListActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_bottom_navigation_view_alarm)
    void onClickAlarmView() {
        startActivity(new Intent(this, AlarmActivity.class));
    }
}