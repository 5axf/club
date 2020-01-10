package com.sky.car.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtil {
	
	protected static Log LOGGER = LogFactory.getLog(DateUtil.class);

	/* 定义常用时间格式 */
	/** yyyy-MM-dd HH:mm:ss */
	public static final String DATE_DEFAULT_STR = "yyyy-MM-dd HH:mm:ss";
	/** yyyy-MM-dd kk:mm:ss.SSS */
	public static final String DATE_LONG_STR = "yyyy-MM-dd kk:mm:ss.SSS";
	/** yyyy-MM-dd */
	public static final String DATE_SMALL_STR = "yyyy-MM-dd";
	/** yyMMddHHmmss */
	public static final String DATE_KEY_STR = "yyMMddHHmmss";
	/** yyyyMMdd */
	public static final String DATE_DAY_STR = "yyyyMMdd";
	/** yyyyMMddHHmmss */
	public static final String DATE_All_KEY_STR = "yyyyMMddHHmmss";
	/** 时间格式 HH:mm:ss */
	public static final String DATE_TIME_DEFAULT = "HH:mm:ss";
	/** 时间格式 HHmmssSSS */
	public static final String DATE_TIME_STR = "HHmmssSSS";
	/**月份格式**/
	public static final String MONTH_STR = "yyyyMM";

	/** 每天小时数 */
	private static final long HOURS_PER_DAY = 24;
	/** 每小时分钟数 */
	private static final long MINUTES_PER_HOUR = 60;
	/** 每分钟秒数 */
	private static final long SECONDS_PER_MINUTE = 60;
	/** 每秒的毫秒数 */
	private static final long MILLIONSECONDS_PER_SECOND = 1000;
	/** 每分钟毫秒数 */
	public static final long MILLIONSECONDS_PER_MINUTE = MILLIONSECONDS_PER_SECOND * SECONDS_PER_MINUTE;
	/** 每天毫秒数 */
	public static final long MILLIONSECONDS_SECOND_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIONSECONDS_PER_SECOND;

	public static TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

	/**
	 * 将日期对象格式化成yyyy-MM-dd格式的字符串
	 * 
	 * @param date
	 *            待格式化日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date) {
		return formatDate(date, DATE_DEFAULT_STR);
	}

	/**
	 * 将日期对象格式化成yyyy-MM-dd HH:mm:ss格式的字符串
	 * 
	 * @param date
	 *            待格式化日期对象
	 * @return 格式化后的字符串
	 */
	public static String forDatetime(Date date) {
		if (date != null) {
			return formatDate(date, DATE_DEFAULT_STR);
		} else {
			return null;
		}

	}

	/**
	 * 将日期对象格式化成HH:mm:ss格式的字符串
	 * 
	 * @param date
	 *            待格式化日期对象
	 * @return 格式化后的字符串
	 */
	public static String formatTime(Date date) {
		return formatDate(date, DATE_TIME_DEFAULT);
	}

	/**
	 * 将日期对象格式化成指定类型的字符串
	 * 
	 * @param date
	 *            待格式化日期对象
	 * @param format
	 *            格式化格式
	 * @return 格式化后的字符串
	 */
	public static String formatDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}


	/**
	 * 带时区的格式化时间
	 * 
	 * @param date
	 * @param format
	 * @param timeZone
	 * @return
	 */
	public static String formatDateTimeZone(Date date, String format, TimeZone timeZone) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(timeZone);
		return sdf.format(date);
	}

	/**
	 * 按照默认yyyy-MM-dd HH:mm:ss格式返回当前时间
	 * 
	 * @return string
	 */
	public static String getNowTime() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_DEFAULT_STR);
		return df.format(new Date());
	}

	/**
	 * 根据pattern日期格式，返回当前时间字符串
	 * 
	 * @param pattern
	 * @return string
	 */
	public static String getNowTime(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date());
	}

	/**
	 * 根据日期date字符串，返回对应Date类型时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date parse(String date) {
		return parse(date, DATE_DEFAULT_STR);
	}

	/**
	 * 根据日期date字符串，按照pattern解析并返回对应Date类型时间
	 * 
	 * @param date
	 * @param pattern
	 * @return Date
	 */
	public static Date parse(String date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			LOGGER.error("程序异常",e);
			return null;
		}
	}
	
	public static Date parseDate(String date, String pattern) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		try {
			return df.parse(date);
		} catch (ParseException e) {
			LOGGER.error("程序异常",e);
			throw e;
		}
	}

	/**
	 * 比较传入的date与当前时间 <br>
	 * 返回结果: 1 比当前时间大，0.当前时间相同，-1：比当前时间小
	 * 
	 * @param date
	 * @return int
	 */
	public static int compareDateWithNow(Date date) {
		Date now = new Date();
		int rnum = date.compareTo(now);
		return rnum;
	}

	/**
	 * 传入进来的timestamp与当前时间的进行比较 <br>
	 * 返回结果: 1 比当前时间大，0.当前时间相同，-1：比当前时间小
	 * 
	 * @param
	 * @return
	 */
	public static int compareDateWithNow(long timestamp) {
		long now = nowDateToTimestamp();
		if (timestamp > now) {
			return 1;
		} else if (timestamp < now) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss返回成timestamp
	 * 
	 * @param date
	 * @return long
	 * @throws ParseException
	 */
	public static long dateToTimestamp(String date) throws ParseException {
		return dateToTimestamp(date, DATE_DEFAULT_STR);
	}

	/**
	 * 按照dateFormat格式date转换成date日期后，返回该date的timestamp
	 * 
	 * @param date
	 * @param dateFormat
	 * @return long
	 * @throws ParseException
	 */
	public static long dateToTimestamp(String date, String dateFormat) throws ParseException {
		long timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
		return timestamp;
	}

	/**
	 * 返回当前时间的时间戳
	 * 
	 * @return long
	 */
	public static long nowDateToTimestamp() {
		long timestamp = new Date().getTime();
		return timestamp;
	}

	/**
	 * 将时间戳返回dateFormat格式的日期字符串
	 * 
	 * @param timestamp
	 * @param dateFormat
	 * @return String
	 */
	public static String timestampToDate(long timestamp, String dateFormat) {
		String date = new SimpleDateFormat(dateFormat).format(new Date(timestamp));
		return date;
	}

	/**
	 * 将时间戳返回("yyyy-MM-dd HH:mm:ss)格式的日期字符串
	 * 
	 * @param timestamp
	 * @return String
	 */
	public static String timestampToDate(long timestamp) {
		return timestampToDate(timestamp, DATE_DEFAULT_STR);
	}

	/***
	 * 指定日期时间分钟上加上分钟数
	 * 
	 * @param date
	 * @param minutes
	 * @return
	 */
	public static Date changeMinute(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	
	/**
	 * 格式化字符串(将yyyy-mm-dd 格式化成yyyymmdd)
	 * @param date
	 * @return
	 */
	public static String formatToStr(String date){
		
		if(null!=date&&!"".equals(date)){
			return date.substring(0, 4)+date.substring(5, 7)+date.substring(8, 10);
		}
		return null;
	}
	
	/**
	 * 指定日期时间上加上时间数
	 * 
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date changeHours(Date date, int hours) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hours);
		return cal.getTime();
	}

	/**
	 * 指定的日期加减天数
	 * 
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date changeDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, days);
		return cal.getTime();
	}
	
	/**
	 * 指定的日期加减天数
	 * 
	 * @param date
	 * @return
	 */
	public static Date changeMonths(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}
	/**
	 * 指定的日期加减年数
	 * 
	 * @param date
	 * @param years
	 * @return
	 */
	public static Date changeYear(Date date, int years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}
	
	/**
	 * 设置日期时间
	 * @Title: setDateTime 
	 * @param date
	 * @param years
	 * @param month
	 * @param days
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 * @return Date    返回类型
	 */
	public static Date setDateTime(Date date, Integer years,Integer month,Integer days,Integer hour,Integer minute,Integer second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(years!=null){
			cal.set(Calendar.YEAR, years);
		}
		if(month!=null){
			cal.set(Calendar.MONTH, month-1);
		}
		if(days!=null){
			cal.set(Calendar.DAY_OF_MONTH, days);
		}
		if(hour!=null){
			cal.set(Calendar.HOUR_OF_DAY, hour);
		}
		if(minute!=null){
			cal.set(Calendar.MINUTE, minute);
		}
		if(second!=null){
			cal.set(Calendar.SECOND, second);
		}
		return cal.getTime();
	}
	
	/**
	 * 设置日期
	 * @Title: setDate 
	 * @param date
	 * @param years
	 * @param month
	 * @param days
	 * @return
	 * @return Date    返回类型
	 */
	public static Date setDate(Date date, Integer years,Integer month,Integer days) {
		return setDateTime(date,years,month,days,null,null,null);		
	}
	
	/**
	 * 设置时间
	 * @Title: setTime 
	 * @param date
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 * @return Date    返回类型
	 */
	public static Date setTime(Date date,Integer hour,Integer minute,Integer second) {
		return setDateTime(date,null,null,null,hour,minute,second);		
	}

	/**
	 * 获得两个日期之间相差的分钟数。（date1 - date2）
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回两个日期之间相差的分钟数值
	 */
	public static int intervalMinutes(Date date1, Date date2) {
		long intervalMillSecond = date1.getTime() - date2.getTime();
		// 相差的分钟数 = 相差的毫秒数 / 每分钟的毫秒数 (小数位采用进位制处理，即大于0则加1)
		return (int) (intervalMillSecond / MILLIONSECONDS_PER_MINUTE + (intervalMillSecond % MILLIONSECONDS_PER_MINUTE > 0 ? 1 : 0));
	}

	/**
	 * 获得两个日期之间相差的秒数差（date1 - date2）
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int intervalSeconds(Date date1, Date date2) {
		long intervalMillSecond = date1.getTime() - date2.getTime();
		return (int) (intervalMillSecond / MILLIONSECONDS_PER_SECOND + (intervalMillSecond % MILLIONSECONDS_PER_SECOND > 0 ? 1 : 0));
	}

	/**
	 * 比较date1，date2两个时间
	 * 
	 * @return boolean
	 * @throws
	 */
	public static boolean beforeDate(Date date1, Date date2) {
		return date1.before(date2);
	}

	/**
	 * 比较date1，date2，两个时间字符串
	 * 
	 * @return boolean
	 * @throws
	 */
	public static boolean beforeDate(String date1, String date2) {
		Date dt1, dt2;
		dt1 = parse(date1);
		dt2 = parse(date2);
		return beforeDate(dt1, dt2);
	}
	
	/**
	 * 判断是否闰年
	 * 
	 * @param year
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 400 == 0) || ((year % 4 == 0) && (year % 100 != 0));
	}
	
	/**
	 * 一个月有几天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int dayInMonth(int year, int month) {
		boolean yearleap = isLeapYear(year);
		int day;
		if (yearleap && month == 2) {
			day = 29;
		} else if (!yearleap && month == 2) {
			day = 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			day = 30;
		} else {
			day = 31;
		}
		return day;
	}
	
    /**
	 * 获取输入 周的开始日期
	 * 
	 * @param week
	 *            like 201412
	 * @return 8位日期 like 20140317
	 */
	public static String getWeekBeginDate(String week) {
		if (week == null || "".equals(week) || week.length() < 5) {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的周换算.");
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);   // 设置一个星期的第一天为星期1，默认是星期日 
			cal.set(Calendar.YEAR, Integer.parseInt(week.substring(0, 4)));
			cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week.substring(4,
					week.length())));
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException("进行周运算时输入得参数不符合系统规格." + e);
		}
	}
	
	
	/**
	 * 获取输入 周的结束日期
	 * 
	 * @param week
	 *            like 201412
	 * @return 8位日期 like 20140323
	 */
	public static String getWeekEndDate(String week) {
		if (week == null || "".equals(week) || week.length() < 5) {
			throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的周换算.");
		}
		try {
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);   // 设置一个星期的第一天为星期1，默认是星期日 
			cal.set(Calendar.YEAR, Integer.parseInt(week.substring(0, 4)));			
			cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(week.substring(4,
					week.length())));
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(cal.getTime());
		} catch (Exception e) {
			throw new RuntimeException("进行周运算时输入得参数不符合系统规格." + e);
		}
	}

	/**
	 *
	 * @param day
	 * @param dateFormat
	 * @return
	 */
	public static String getBeforeDay(int day, String dateFormat) {
		try {
			Calendar c = Calendar.getInstance();
			int curDay = c.get(Calendar.DATE);
			c.set(Calendar.DATE, curDay - day);
			return new SimpleDateFormat(dateFormat).format(c.getTime());
		} catch (Exception e) {
			throw new RuntimeException("异常.", e);
		}
	}
	
	/**
	 * 是否是yyyymmdd类型
	 * @return boolean
	 * @throws
	 */
	public static boolean isNotYmd(String date,String format) {
		try {
			SimpleDateFormat df = new SimpleDateFormat(format);
			df.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String formatDateNotNull(Date date, String format) {
		String result = "";
		if(date==null) return ""; 
		try {
			result = new SimpleDateFormat(format).format(date);
		} catch (Exception e) {
			return "";
		}
		return result;
	}
	/**
	 * 获取上N个月份(格式:yyyyMM)
	 * @param date
	 * @param mouth
	 * @return
	 */
	public static String getBeforeMonth(Date date, Integer month){
		return formatDate(changeMonths(date, -1), MONTH_STR);
	}
	
	public static String getFirstDay(Date date, int months){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return formatDate(c.getTime(), DATE_DAY_STR);
	}
	
	public static String getLastDay(Date date, int months){
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 0);
		return formatDate(c.getTime(), DATE_DAY_STR);
	}
	
	public static void main(String[] args){
		
		String dateStr= "2018-04-20 17:10:10";
		Date date = parse(dateStr, DATE_DEFAULT_STR);
		Date endDate = changeDays(date, 42);
		
		System.out.println(formatDate(endDate, DATE_DEFAULT_STR));
		
	}
	
    public static String getCurrentDateString(String dateFormat) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }
    
	//获取当前时间-格式‘年’月‘日’'时分秒'
	public static String getCurrenttime(){
		Date date =new Date();
		SimpleDateFormat sf =new SimpleDateFormat("yyyyMMddHHmmss");
		String time=sf.format(date);		 
		 return time;
	}
	
	public static String formatDateStr(Date date1, Date date2) {
		String result = "";

		if (date1 == null || date2 == null)
			return result;
		try {

			String y1 = formatDate(date1, "yyyy");
			String m1 = formatDate(date1, "MM");
			String d1 = formatDate(date1, "dd");
			int yy1 = Integer.parseInt(y1);
			int mm1 = Integer.parseInt(m1);
			int dd1 = Integer.parseInt(d1);

			String y2 = formatDate(date2, "yyyy");
			String m2 = formatDate(date2, "MM");
			String d2 = formatDate(date2, "dd");
			int yy2 = Integer.parseInt(y2);
			int mm2 = Integer.parseInt(m2);
			int dd2 = Integer.parseInt(d2);
			if (yy1 == yy2) {
				if (mm1 == mm2) {
					result = mm1 + "月" + dd1 + "-" + dd2 + "日";
					if(dd1 == dd2) {
						result =  mm1 + "月" + dd1 +"日";
					}
				} else {
					result = mm1 + "月" + dd1 + "日" + "-" + mm2 + "月" + dd2 + "日";
				}
			} else {
				result = yy1 + "年" + mm1 + "月" + dd1 + "日" + "-" + yy2 + "年" + mm2 + "月" + dd2 + "日";
			}
			return result;
		} catch (Exception e) {

		}
		return result;
	}
	
	public static String formatDateStrYear(Date date1, Date date2) {
		String result = "";

		if (date1 == null || date2 == null)
			return result;
		try {

			String y1 = formatDate(date1, "yyyy");
			String m1 = formatDate(date1, "MM");
			String d1 = formatDate(date1, "dd");
			int yy1 = Integer.parseInt(y1);
			int mm1 = Integer.parseInt(m1);
			int dd1 = Integer.parseInt(d1);

			String y2 = formatDate(date2, "yyyy");
			String m2 = formatDate(date2, "MM");
			String d2 = formatDate(date2, "dd");
			int yy2 = Integer.parseInt(y2);
			int mm2 = Integer.parseInt(m2);
			int dd2 = Integer.parseInt(d2);
			if (yy1 == yy2) {
				if (mm1 == mm2) {
					result = yy1 + "年" + mm1 + "月" + dd1 + "-" + dd2 + "日";
					if(dd1 == dd2) {
						result = yy1 + "年" + mm1 + "月" + dd1 +"日";
					}
				} else {
					result = yy1 + "年" + mm1 + "月" + dd1 + "日" + "-" + mm2 + "月" + dd2 + "日";
				}
			} else {
				result = yy1 + "年" + mm1 + "月" + dd1 + "日" + "-" + yy2 + "年" + mm2 + "月" + dd2 + "日";
			}
			return result;
		} catch (Exception e) {

		}
		return result;
	}
}
