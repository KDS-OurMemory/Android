package com.skts.ourmemory.contract;

import com.skts.ourmemory.model.memory.MemoryDAO;

import java.util.List;

public class CalendarAdapterContract {
    public interface Model {
        int getCalendarCount();

        void addItems(List<MemoryDAO> items);

        void addPlusItem(MemoryDAO item);

        void editItem(MemoryDAO item);

        void deleteItem(int memoryId);

        String getCalendarDay(int position);

        List<MemoryDAO> getCalendarData(int position);

        MemoryDAO getData(int position);
    }

    public interface View {
        void notifyAdapter();       // UI Update
    }
}
