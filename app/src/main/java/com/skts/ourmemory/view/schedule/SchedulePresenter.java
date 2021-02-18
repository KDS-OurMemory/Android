package com.skts.ourmemory.view.schedule;

public class SchedulePresenter implements ScheduleContract.Presenter {
    private final String TAG = SchedulePresenter.class.getSimpleName();

    private ScheduleContract.View mView;

    public SchedulePresenter() {
    }

    @Override
    public void setView(ScheduleContract.View view) {
        this.mView = view;
    }

    @Override
    public void releaseView() {
        this.mView = null;
    }
}
