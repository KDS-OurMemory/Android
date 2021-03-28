package com.skts.ourmemory.model;

public class CalendarModel {
    private String mToday;              // 오늘 날짜
    private boolean mSchedule1;         // 스케쥴1
    private boolean mSchedule2;         // 스케쥴2
    private boolean mSchedule3;         // 스케쥴3
    private boolean mClicked;           // 클릭 여부

    /*생성자*/
    public CalendarModel() {
    }

    /* Getter/Setter */

    public String getToday() {
        return mToday;
    }

    public void setToday(String today) {
        this.mToday = today;
    }

    public boolean isSchedule1() {
        return mSchedule1;
    }

    public void setSchedule1(boolean schedule1) {
        this.mSchedule1 = schedule1;
    }

    public boolean isSchedule2() {
        return mSchedule2;
    }

    public void setSchedule2(boolean schedule2) {
        this.mSchedule2 = schedule2;
    }

    public boolean isSchedule3() {
        return mSchedule3;
    }

    public void setSchedule3(boolean schedule3) {
        this.mSchedule3 = schedule3;
    }

    public boolean isClicked() {
        return mClicked;
    }

    public void setClicked(boolean clicked) {
        this.mClicked = clicked;
    }
}
