package com.skts.ourmemory.contract;

import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.List;

public class CalendarAdapterContract {
    public interface Model {
        int getCalendarCount();
        void addItems(List<SchedulePostResult.ResponseValue> items);
    }

    public interface View {
        void notifyAdapter();       // UI Update
    }
}
