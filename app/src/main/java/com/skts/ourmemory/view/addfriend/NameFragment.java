package com.skts.ourmemory.view.addfriend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skts.ourmemory.R;
import com.skts.ourmemory.adapter.UserListAdapter;
import com.skts.ourmemory.contract.NameContract;
import com.skts.ourmemory.model.UserDAO;
import com.skts.ourmemory.presenter.NamePresenter;
import com.skts.ourmemory.view.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class NameFragment extends BaseFragment implements NameContract.View {
    private final NameContract.Presenter mPresenter;
    private Context mContext;
    private EditText mSearchName;
    private TextView mNoUserTextView;
    private RecyclerView mRecyclerView;

    /*User data*/
    private List<Integer> mFriendUserIdList;
    private UserListAdapter mUserListAdapter;

    /*Dialog*/
    private AlertDialog mAlertDialog;

    public NameFragment() {
        this.mPresenter = new NamePresenter();
        this.mUserListAdapter = new UserListAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend_search_by_name, container, false);

        mContext = container.getContext();
        mPresenter.setView(this);

        mSearchName = view.findViewById(R.id.et_fragment_add_friend_search_by_name_edit_text);
        mNoUserTextView = view.findViewById(R.id.tv_fragment_add_friend_search_by_name_text_view);
        mRecyclerView = view.findViewById(R.id.rv_fragment_add_friend_search_by_name_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));                // 리사이클러뷰에 LinearLayoutManager 객체 지정

        mSearchName.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                try {
                    mPresenter.getUserName(mSearchName.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("올바른 이름을 입력해주세요.");
                }
                return true;
            }
            return false;
        });

        /*addUserButton.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            mAlertDialog = builder.create();
            mAlertDialog.setTitle("친구 추가");
            mAlertDialog.setMessage("친구 추가 하시겠습니까?");
            mAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), (dialogInterface, i) -> mPresenter.requestFriend(mFriendUserId));
            mAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
            mAlertDialog.show();
        });*/

        return view;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_friend_search_by_name;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void showUserList(List<UserDAO> userData) {
        try {
            if (userData != null) {
                ArrayList<String> list = new ArrayList<>();
                if (userData.get(0).getName() != null) {
                    for (int i = 0; i < userData.size(); i++) {
                        list.add(userData.get(i).getName());
                    }

                    mUserListAdapter = new UserListAdapter(list);
                    mRecyclerView.setAdapter(mUserListAdapter);;
                    mNoUserTextView.setVisibility(View.GONE);
                    return;
                }
            }

            mNoUserTextView.setText("\"" + mSearchName.getText() + "\"" + "에 해당하는 사용자 정보가 없습니다.");
            mUserListAdapter.setListClear();
            mNoUserTextView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Context getAppContext() {
        return mContext;
    }
}
