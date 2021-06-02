package com.skts.ourmemory.presenter;

import com.skts.ourmemory.contract.UserSettingContract;

public class UserSettingPresenter implements UserSettingContract.Presenter {
    private UserSettingContract.View mView;

    @Override
    public void setView(UserSettingContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
