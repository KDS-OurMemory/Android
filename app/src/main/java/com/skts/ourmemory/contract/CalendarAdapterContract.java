package com.skts.ourmemory.contract;

import com.skts.ourmemory.model.schedule.SchedulePostResult;

import java.util.List;

public class CalendarAdapterContract {
    public interface Model {
        int getCalendarCount();
        void addItems(List<SchedulePostResult.ResponseValue> items);
        void addPlusItem(SchedulePostResult.ResponseValue item);
        void editItem(SchedulePostResult.ResponseValue item);
        void deleteItem(int memoryId);
        String getCalendarDay(int position);
        List<SchedulePostResult.ResponseValue> getCalendarData(int position);
        SchedulePostResult.ResponseValue getData(int position);
    }

    public interface View {
        void notifyAdapter();       // UI Update
    }
}
