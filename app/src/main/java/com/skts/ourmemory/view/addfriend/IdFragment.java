package com.skts.ourmemory.view.addfriend;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.skts.ourmemory.R;
import com.skts.ourmemory.contract.IdContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.presenter.IdPresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.List;

public class IdFragment extends BaseFragment implements IdContract.View {
    private final IdContract.Presenter mPresenter;
    private Context mContext;
    private EditText mSearchId;
    private ImageView mUserProfileImage;
    private TextView mUserName;
    private ImageView mAddUserButton;
    private LinearLayout mLinearLayout;

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

        mContext = container.getContext();
        mPresenter.setView(this);

        mSearchId = view.findViewById(R.id.et_fragment_add_friend_search_by_id_edit_text);
        mUserProfileImage = view.findViewById(R.id.iv_fragment_add_friend_search_user_data_profile_image);
        mUserName = view.findViewById(R.id.tv_fragment_add_friend_search_user_data_text_view);
        mAddUserButton = view.findViewById(R.id.iv_fragment_add_friend_search_user_data_plus_button);
        mLinearLayout = view.findViewById(R.id.fragment_add_friend_search_user_data_include);

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
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> mPresenter.requestFriend(mFriendUserId));
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
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
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUserList(List<UserDAO> userData) {
        try {
            mFriendUserId = userData.get(0).getUserId();
            mUserName.setText(userData.get(0).getName());
            mLinearLayout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }
}
