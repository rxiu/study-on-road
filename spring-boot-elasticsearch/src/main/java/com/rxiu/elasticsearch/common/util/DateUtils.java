package com.rxiu.elasticsearch.common.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rxiu on 2018/05/30.
 **/
public class DateUtils {

    public static Date getDayBegin(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, i);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDayEnd(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, i);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    public static Date getBeginDayOfWeek(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 7 * i);
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getDayBegin(cal.getTime(), 0);
    }

    public static Date getEndDayOfWeek(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek(date, i));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return getDayEnd(cal.getTime(), 0);
    }

    public static Date getBeginDayOfMonth(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        cal.set(Calendar.DATE, 1);
        return getDayBegin(cal.getTime(), 0);
    }

    public static Date getEndDayOfMonth(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return getDayEnd(cal.getTime(), 0);
    }

    public static Date getBeginDayOfYear(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        return getBeginDayOfMonth(cal.getTime(), 0);
    }

    public static Date getEndDayOfYear(Date date, Integer i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        return getEndDayOfMonth(cal.getTime(), 0);
    }
}
