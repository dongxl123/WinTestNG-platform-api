package com.winbaoxian.testng.platform.utils;

import java.util.Calendar;
import java.util.Date;

public enum DateTimeUtils {

    INSTANCE;

    public Date getToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal.getTime();
    }

    public Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public Date getDatetime(Date date, int deltaDays) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, deltaDays);
        return cal.getTime();
    }

    public Long getStrictEndTime(Long endTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(endTime);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DATE, 1);
        return cal.getTimeInMillis();
    }

}
