package com.skts.ourmemory.view.signup;

import com.skts.ourmemory.BaseContract;

public class SignUpContract {

    interface View extends BaseContract.View {

    }

    interface Presenter extends BaseContract.Presenter<View> {
        @Override
        void setView(View View);

        @Override
        void releaseView();
    }
}
