//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sky.car.util;

import javax.sound.midi.SoundbankResource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeUtil {
    public TimeUtil() {
    }

    public static int currentTimeSecond() {
        int time = (int)(System.currentTimeMillis() / 1000L);
        return time;
    }

    public static int dayOf1970() {
        int currentTimeSecond = currentTimeSecond() + 28800;
        int dayOf1970 = currentTimeSecond / 86400;
        return dayOf1970;
    }

    public static int weekOf1970() {
        int dayOf1970 = dayOf1970();
        int weekOf1970 = (dayOf1970 + 3) / 7;
        return weekOf1970;
    }

    public static long getRefreshEveryDayTaskDelayTime() {
        long time = getTomorrowTime() - System.currentTimeMillis();
        return time;
    }

    public static int getSecondByFormatDate(String dateStr, String formatStr) {
        if (!"0".equals(dateStr) && !"".equals(dateStr)) {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            Date date = null;

            try {
                date = format.parse(dateStr);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }

            return (int)(date.getTime() / 1000L);
        } else {
            return 0;
        }
    }

    public static Date getDateByFormatDate(String dateStr, String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;

        try {
            date = format.parse(dateStr);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return date;
    }

    public static long getNextTimeMillis(int hour, int minute, int second, int milliSecond) {
        if (hour >= 0 && hour <= 24) {
            Calendar cal = Calendar.getInstance();
            cal.set(11, hour);
            cal.set(12, minute);
            cal.set(13, second);
            cal.set(14, milliSecond);
            long time = cal.getTimeInMillis();
            long currentTime = System.currentTimeMillis();
            return currentTime <= time ? time - currentTime : time - currentTime + 86400000L;
        } else {
            throw new RuntimeException("hour is error!");
        }
    }

    public static long getNextWeekTimeMillis(int week, int hour, int minute, int second, int milliSecond) {
        if (hour >= 0 && hour <= 24) {
            Calendar cal = Calendar.getInstance();
            cal.set(7, week + 1);
            cal.set(11, hour);
            cal.set(12, minute);
            cal.set(13, second);
            cal.set(14, milliSecond);
            long time = cal.getTimeInMillis();
            long currentTime = System.currentTimeMillis();
            return currentTime <= time ? time - currentTime : time - currentTime + 604800000L;
        } else {
            throw new RuntimeException("hour is error!");
        }
    }

    public static long getTodayTime(int hh, int mm, int ss, int ms) {
        Calendar cal = Calendar.getInstance();
        cal.set(11, hh);
        cal.set(12, mm);
        cal.set(13, ss);
        cal.set(14, ms);
        long time = cal.getTimeInMillis();
        return time;
    }

    public static long getTime(int year, int month, int day, int hh, int mm, int ss, int ms) {
        Calendar cal = Calendar.getInstance();
        cal.set(1, year);
        cal.set(2, month);
        cal.set(5, day);
        cal.set(11, hh);
        cal.set(12, mm);
        cal.set(13, ss);
        cal.set(14, ms);
        long time = cal.getTimeInMillis();
        return time;
    }

    public static long getTomorrowTime() {
        long time = getTodayTime(24, 0, 0, 0);
        return time;
    }

    public static long getTodayZeroHourTime() {
        long time = getTodayTime(0, 0, 0, 0);
        return time;
    }

    public static long getYesterdayTime() {
        long time = getTodayTime(-24, 0, 0, 0);
        return time;
    }

    public static boolean checkTimeIsToday(long time) {
        long beginTime = getTodayZeroHourTime();
        long endTime = beginTime + 86400000L;
        return time > beginTime && time <= endTime;
    }

    public static boolean checkIsSameDayByHour(long time, int hour) {
        long beginTime = getTodayZeroHourTime() + (long)(hour * 3600 * 1000);
        long endTime = beginTime + 86400000L;
        Calendar cal = Calendar.getInstance();
        int currentHour = cal.get(11);
        if (currentHour >= 0 && currentHour < 5) {
            beginTime -= 86400000L;
            endTime -= 86400000L;
        }

        return time > beginTime && time <= endTime;
    }

    public static long getTodayTimeMillisByHHMMSS(String hhmmss) {
        int[] ii = StringUtils.stringToIntArray(hhmmss, ":");
        int hour = ii[0];
        int minute = ii[1];
        int second = ii[2];
        if (hour >= 0 && hour <= 24) {
            Calendar cal = Calendar.getInstance();
            cal.set(11, hour);
            cal.set(12, minute);
            cal.set(13, second);
            long time = cal.getTimeInMillis();
            return time;
        } else {
            throw new RuntimeException("hour is error!");
        }
    }

    public static String getFormatTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = sdf.format(new Date(time));
        return str;
    }

    public static String getFormatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String str = sdf.format(date);
        return str;
    }

    public static boolean checkIsSameDay(int time1, int time2) {
        Date date1 = new Date((long)time1 * 1000L);
        Date date2 = new Date((long)time2 * 1000L);
        long t1 = getTime(date1.getYear(), date1.getMonth(), date1.getDay(), 0, 0, 0, 0);
        long t2 = getTime(date2.getYear(), date2.getMonth(), date2.getDay(), 0, 0, 0, 0);
        return t1 == t2;
    }

    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(7);
        int week = dayOfWeek - 1;
        return week;
    }

    public static long getTimeByWeekStr(String weekStr) {
        String[] ss = weekStr.split("\\|");
        String[] hms = ss[1].split(":");
        int week = Integer.valueOf(ss[0]);
        ++week;
        int hour = Integer.valueOf(hms[0]);
        int minute = Integer.valueOf(hms[1]);
        int second = Integer.valueOf(hms[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(7, week);
        cal.set(11, hour);
        cal.set(12, minute);
        cal.set(13, second);
        long time = cal.getTimeInMillis();
        return time;
    }

    /**
     * 传 0 则代表当前月，传 1 则代表 前一个月 ，传 2 则代表 前两个月，以此类推
     * @param month
     * @return
     */
    public static Map<String,String> getMonthMap(int month){
        Map<String,String> monthMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();// 获取当前日期
        cal_1.add(Calendar.MONTH, -month);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String firstDay = format.format(cal_1.getTime());
        // 获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, -(month-1));
        cale.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = format.format(cale.getTime());
        monthMap.put("firstDay", firstDay);
        monthMap.put("lastDay", lastDay);
        return monthMap;
    }

    public static Map<String,String> getYearMonth(int month){
        Map<String,String> monthMap = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        // 获取前月的第一天
        Calendar cal_1 = Calendar.getInstance();// 获取当前日期
        cal_1.add(Calendar.MONTH, -month);
        cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String yearMonth = format.format(cal_1.getTime());
        monthMap.put("yearMonth", yearMonth);
        return monthMap;
    }

    public static void main(String[] args) {
        Map<String, String> monthMap = getYearMonth(1);
        System.out.println("yearMonth==>:"+monthMap.get("yearMonth"));
    }
}
