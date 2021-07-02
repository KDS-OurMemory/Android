package com.skts.ourmemory.contract;

import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.List;

public class CalendarAdapterContract {
    public interface Model {
        int getCalendarCount();
        void addItems(List<SchedulePostResult.ResponseValue> items);
        String getCalendarDay(int position);
        List<SchedulePostResult.ResponseValue> getCalendarData(int position);
        //void updateCalendarData()
    }

    public interface View {
        void notifyAdapter();       // UI Update
    }
}
