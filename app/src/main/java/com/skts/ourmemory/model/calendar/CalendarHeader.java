package com.skts.ourmemory.model.calendar;

import com.skts.ourmemory.util.DateUtil;

public class CalendarHeader extends ViewModel{
    String header;
    long mCurrentTime;

    public CalendarHeader() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(long time) {
        this.header = DateUtil.getDate(time, DateUtil.CALENDAR_HEADER_FORMAT);
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
