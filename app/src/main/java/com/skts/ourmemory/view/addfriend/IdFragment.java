package com.skts.ourmemory.view.addfriend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.skts.ourmemory.R;
import com.skts.ourmemory.common.ServerConst;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.friend.FriendDAO;
import com.skts.ourmemory.model.friend.FriendPostResult;
import com.skts.ourmemory.presenter.IdPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IdFragment extends BaseFragment implements IdContract.View {
    private Unbinder unbinder;
    private final IdContract.Presenter mPresenter;
    private Context mContext;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_fragment_add_friend_search_by_id_edit_text)
    EditText mSearchId;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_add_friend_search_by_id_text_view)
    TextView mNoUserTextView;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.iv_fragment_add_friend_search_user_data_profile_image)
    ImageView mUserProfileImage;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_fragment_add_friend_search_user_data_text_view)
    TextView mUserName;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fragment_add_friend_search_user_data_include)
    LinearLayout mLinearLayout;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_fragment_add_friend_search_user_data_plus_button)
    Button mAddUserButton;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_fragment_add_friend_search_user_data_cancel_button)
    Button mCancelButton;

    /*User data*/
    private int mFriendUserId;

    /*Dialog*/
    private AlertDialog mAlertDialog;

    public IdFragment() {
        this.mPresenter = new IdPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend_search_by_id, container, false);
        unbinder = ButterKnife.bind(this, view);

        mContext = Objects.requireNonNull(container).getContext();
        mPresenter.setView(this);

        mSearchId.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                try {
                    mPresenter.getUserId(Integer.parseInt(mSearchId.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("올바른 ID 값을 입력해주세요.");
                }
                return true;
            }
            return false;
        });

        mAddUserButton.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            mAlertDialog = builder.create();
            mAlertDialog.setTitle("친구 추가");
            mAlertDialog.setMessage("친구 추가 하시겠습니까?");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
                mPresenter.requestFriend(mFriendUserId);
                setFriendWait();
                dialogInterface.dismiss();
            });
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
            mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
            mAlertDialog.show();
        });

        mCancelButton.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            mAlertDialog = builder.create();
            mAlertDialog.setTitle("친구 추가 취소");
            mAlertDialog.setMessage("친구 추가를 취소 하시겠습니까?");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> {
                mPresenter.cancelFriend(mFriendUserId);
                setFriendRequest();
                dialogInterface.dismiss();
            });
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
            mAlertDialog.setOnShowListener(dialogInterface -> mAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.GRAY));
            mAlertDialog.show();
        });

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_friend_search_by_id;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

        mPresenter.releaseView();
        mContext = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showUserList(int userId, FriendPostResult friendPostResult) {
        List<FriendDAO> userData = friendPostResult.getResponse();
        if (userData.isEmpty()) {
            mLinearLayout.setVisibility(View.GONE);
            mNoUserTextView.setText("\"" + mSearchId.getText() + "\"" + "에 해당하는 사용자 정보가 없습니다.");
            mNoUserTextView.setVisibility(View.VISIBLE);
            return;
        }

        FriendDAO friendDAO = userData.get(0);
        String friendStatus = friendDAO.getFriendStatus();

        if (userId == friendDAO.getFriendId()) {
            // 본인
            setMySelf();
        } else if (friendStatus == null) {
            setFriendRequest();
        } else if (friendStatus.equals(ServerConst.WAIT)) {
            setFriendWait();
        } else if (friendStatus.equals(ServerConst.REQUESTED_BY)) {
            setFriendRequestedBy();
        } else {
            // else if (friendStatus.equals(ServerConst.BLOCK))
            setFriendBlock();
        }

        mFriendUserId = userData.get(0).getFriendId();
        mUserName.setText(userData.get(0).getName());
        mLinearLayout.setVisibility(View.VISIBLE);
        mNoUserTextView.setVisibility(View.GONE);

        // Glide 로 이미지 표시
        if (friendDAO.getProfileImageUrl() == null) {
            Glide.with(this).load(R.drawable.ic_baseline_person_30).override(150, 150).circleCrop().into(mUserProfileImage);
        } else {
            Glide.with(this).load(friendDAO.getProfileImageUrl()).override(150, 150).circleCrop().into(mUserProfileImage);
        }
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }

    @Override
    public void setMySelf() {
        mAddUserButton.setText("본인입니다");
        mAddUserButton.setEnabled(false);
        mCancelButton.setVisibility(View.GONE);
    }

    @Override
    public void setFriendRequest() {
        mAddUserButton.setText("친구 추가");
        mAddUserButton.setEnabled(true);
        mCancelButton.setVisibility(View.GONE);
    }

    @Override
    public void setFriendWait() {
        mAddUserButton.setText("친구 요청 중");
        mAddUserButton.setEnabled(false);
        mCancelButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFriendRequestedBy() {
        mAddUserButton.setText("친구 승인");
        mAddUserButton.setEnabled(true);
        mCancelButton.setVisibility(View.GONE);
    }

    @Override
    public void setFriendBlock() {
        mAddUserButton.setText("친구 차단");
        mAddUserButton.setEnabled(false);
        mCancelButton.setVisibility(View.VISIBLE);
    }
}
