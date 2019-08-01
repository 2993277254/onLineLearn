package com.lsfly.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

	private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public static final String DATE_PATTERN_DEFAULT = "yyyyMMdd";
	public static final String DATE_PATTERN_DEFAULT_YYYYMM = "yyyyMM";
	public static final String TIME_PATTERN_DEFAULT = "HHmmss";
	public static final String DATE_PATTERN_DEFAULT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	/**
	 * 功能：判断字符串是否为日期格式
	 *
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLegalDate(String sDate,String format) {
	//	int legalLen = 16;
		/*if ((sDate == null) || (sDate.length() != legalLen)) {
			return false;
		}*/

		DateFormat formatter = new SimpleDateFormat(format);
		try {
			Date date = formatter.parse(sDate);
			return sDate.equals(formatter.format(date));
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isLegalDate(String sDate) {
		int legalLen = 16;
		if ((sDate == null) || (sDate.length() != legalLen)) {
			return false;
		}

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = formatter.parse(sDate);
			return sDate.equals(formatter.format(date));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 取得系统时间戳
	 */
	public static long getTimeMillis()
	{
		return System.currentTimeMillis();
	}

	/**
	 * 字串轉時間
	 * @param str 時間字串
	 * @param fmt 字串格式
	 * @return Date
	 */
	public static Date stringToDate(String str, String fmt){
		if (null == str || "".equals (str))
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try{
			return sdf.parse(str);
		}catch (ParseException pe){
			logger.error(pe.getMessage());
		}
        return null;
	}

	 /**
     * 计算两个日期之间相差的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static Integer daysBetween(Date smdate,Date bdate)
    {
    	try{
	        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        smdate=sdf.parse(sdf.format(smdate));
	        bdate=sdf.parse(sdf.format(bdate));
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(smdate);
	        long time1 = cal.getTimeInMillis();
	        cal.setTime(bdate);
	        long time2 = cal.getTimeInMillis();
	        long between_days=(time2-time1)/(1000*3600*24);

	       return Integer.parseInt(String.valueOf(between_days));
		}catch (ParseException pe){
			return null;
		}
    }

	/**
	 * 整型轉字符串格式日期，預設格式yyyy-MM-dd
	 * by shadow
	 */
	//返回排程日期补足8位并格式化"0000-00-00"
	public static String getFormatDate(Integer date){
		if(date==null)return "";

		String Date=date.toString();
		String strDate="";
		String formatDate="";
		if (Date.length()==0)
			formatDate="";
		else{
			for (int i=0;i<(8-Date.length());i++){
				strDate+="0";
			}
			Date=strDate+Date;
			formatDate=Date.substring(0,4)+"-"+Date.substring(4,6)+"-"+Date.substring(6,8);
		}
		return formatDate;
	}
	/**
	 * 整型轉字符串格式时间，預設格式hh:mm:ss
	 * by shadow
	 */
	public static String getFormatTime(Integer time){
		if (time==null)
			return "";
		String Time=time.toString();
		String strTime="";
		String formatTime="";
		if (Time.length()==0)
			formatTime="";
		else{
			for (int i=0;i<(6-Time.length());i++){
				strTime+="0";
			}
			Time=strTime+Time;
			formatTime=Time.substring(0,2)+":"+Time.substring(2,4)+":"+Time.substring(4,6);
		}
		return formatTime;
	}


	/**
	 * 字串轉日期，預設格式yyyyMMdd
	 * @param str 日期字串
	 * @return Date
	 */
	public static Date stringToDate(String str){
		if (null == str || "".equals (str))
			return null;
		return	stringToDate (str, DATE_PATTERN_DEFAULT);
	}

	/**
	 * 字串轉日期，預設格式yyyyMM
	 * @param str 日期字串
	 * @return Date
	 */
	public static Date stringToDateYYYYMM(String str){
		if (null == str || "".equals (str))
			return null;
		return	stringToDate (str, DATE_PATTERN_DEFAULT_YYYYMM);
	}

	/**
	 * 字串轉日期，預設格式YYYYMMDDHHMMSS
	 * @param str 日期字串
	 * @return Date
	 */
	public static Date stringToDateYYYYMMDDHHMMSS(String str){
		if (null == str || "".equals (str))
			return null;
		Date date=new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(str);
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		return	date;
	}

	/**
	 * 日期轉字串
	 * @param date 日期
	 * @param fmt 字串格式
	 * @return String
	 */
	public static String dateToString(Date date, String fmt){
		if (null == date)
			return "";

		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try{
			return new String(sdf.format(date, new StringBuffer(), new java.text.FieldPosition(0)));
		}catch (NullPointerException ne){
			logger.error(ne.getMessage());
		}
        return null;
	}

	/**
	 * 日期轉字串，預設格式yyyyMMdd
	 * @param date 日期
	 * @return String
	 */
	public static String dateToString(Date date){
		if (null == date)
			return "";
		return	dateToString(date, DATE_PATTERN_DEFAULT);
	}

	/**
	 * 日期轉數字
	 * @param date
	 * @return
	 */
	public static int dateToInt(Date date){
		try{
			return Integer.parseInt(DateUtils.dateToString(date));
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * 數字日期時間轉日期時間
	 * @param date
	 * @param time
	 * @return
	 */
	public static Date intToDate(int date, int time){
		String fullDateTime = intDateToStr(date) + intTimeToStr(time);
		return stringToDate(fullDateTime, "yyyyMMddHHmmss");
	}

	/**
	 * 數字日期時間轉日期時間
	 * @param date
	 * @return
	 */
	public static Date intToDate(int date){
		String fullDate = intDateToStr(date);
		return stringToDate(fullDate, "yyyyMMdd");
	}

	/**
	 * 時間轉字串，預設格式HHmmss
	 * @param date 時間
	 * @return String
	 */
	public static String timeToString(Date date){
		if (null == date)
			return "";
		return dateToString(date, TIME_PATTERN_DEFAULT);
	}
	/**
	 * 時間轉字串，預設格式HHmmss
	 * @param date 時間
	 * @return String
	 */
	public static String timeToString(Date date, String fmt){
		if (null == date)
			return "";
		return dateToString(date, fmt);
	}
	/**
	 * 時間轉數字
	 * @param date
	 * @return
	 */
	public static int timeToInt(Date date){
		try{
			return Integer.parseInt(DateUtils.timeToString(date));
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * 數字日期轉字串
	 * @param date
	 * @param fmt
	 * @return
	 */
	public static String intDateToStr(int date, String fmt){
		return dateToString(stringToDate(String.format("%08d", date)), fmt);
	}

	/**
	 * 數字日期轉字串, 預設格式yyyyMMdd
	 * @param date
	 * @return
	 */
	public static String intDateToStr(int date){
		return intDateToStr(date, DATE_PATTERN_DEFAULT);
	}

	/**
	 * 數字時間轉字串
	 * @param time
	 * @param fmt
	 * @return
	 */
	public static String intTimeToStr(int time, String fmt){
		return dateToString(stringToDate(String.format("%06d", time), TIME_PATTERN_DEFAULT), fmt);
	}

	/**
	 * 數字時間轉字串, 預設格式HHmmss
	 * @param time
	 * @return
	 */
	public static String intTimeToStr(int time){
		return intTimeToStr(time, TIME_PATTERN_DEFAULT);
	}


	/**
	 * 取得系統時間
	 * @return Date
	 */
	public static Date getSysDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 取得昨日
	 * @return
	 */
	public static Date getYesterday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 取得明日(隔日)
	 * @return
	 */
	public static Date getTomorrow(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +1);
		return cal.getTime();
	}

	/**
	 * 增加日期月份
	 * @param dt 時間
	 * @param amount 增加月份
	 * @return Date
	 */
	public static Date addMonth(Date dt, int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MONTH, amount);
		return cal.getTime();
	}

	/**
	 * 增加日期天數
	 * @param dt 時間
	 * @param amount 增加天數
	 * @return Date
	 */
	public static Date addDate(Date dt, int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.DAY_OF_MONTH, amount);
		return cal.getTime();
	}

	/**
	 * 增加分鐘數
	 * @param dt 時間
	 * @param amount 增加分鐘數
	 * @return
	 */
	public static Date addMinute(Date dt, int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.add(Calendar.MINUTE, amount);
		return cal.getTime();
	}

	/**
	 * 計算時間差距
	 * @param sDate 起始時間
	 * @param eDate 結束時間
	 * @return
	 */
	public static long totalSec(Date sDate, Date eDate){
		long sTime = sDate.getTime();
		long eTime = eDate.getTime();
		long total = eTime - sTime;
		return total;
	}

	/**
	 * 檢查信用卡有效月年
	 * [v1 > v2, 回傳大於0],
	 * [v1 = v2, 回傳0],
	 * [v1 < v2, 回傳小於0]
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static int compareVaildYearMonth(String v1, String v2){
		String v1YM = v1.substring(2) + v1.substring(0, 2);
		String v2YM = v2.substring(2) + v2.substring(0, 2);
		return v1YM.compareTo(v2YM);
	}

	/**
	 * 轉換信用卡過期日,MMyy轉成yyyyMM
	 * @param MMyy
	 * @return yyyMM
	 */
	public static String convertCardtValidYearMonth(String MMyy){
		try {
			//須為數字
			Integer.parseInt(MMyy);
			//更換年月
			String sysYear = DateUtils.dateToString(getSysDate(), "yyyy");
			String result = sysYear.substring(0, 2)
					+ MMyy.substring(2)
					+ MMyy.substring(0, 2);
			return result;
		}catch(Exception e){
			logger.info(e.getMessage(), e);
			return null;
		}

	}

	/**
	 * 西元年字串轉民國年字串
	 * @param dateStr
	 * @return
	 */
	public static String dateStrToROC(String dateStr){
		try{
			Date date = DateUtils.stringToDate(dateStr);
			int dateInt = DateUtils.dateToInt(date);
			return String.valueOf(dateInt - 19110000);
		}catch(Exception e){
			logger.info(e.getMessage(), e);
			return null;
		}

	}

	/**
	 * 民國年字串轉西元年字串
	 * @param rocStr
	 * @return
	 */
	public static String ROCToDateStr(String rocStr){
		try{
			int dateInt = Integer.parseInt(rocStr) + 19110000;
			return String.valueOf(dateInt);
		}catch(Exception e){
			logger.info(e.getMessage(), e);
			return null;
		}

	}

	/**
	 * 計算剩餘時間
	 * @param limitDate 截止時間
	 * @param currentDate 當下時間
	 * @return long[]回傳 [index:0] 分, [index:1] 秒
	 */
	public static long[] getVerifyRemainTime(Date limitDate, Date currentDate){
		long[] result = new long[]{0l, 0l};
		long totalMSec = limitDate.getTime() - currentDate.getTime();
		long totalSec = (new BigDecimal(totalMSec).divide(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP)).longValue();
		result[0] = totalSec / 60l;
		result[1] = totalSec % 60l;
		return result;
	}

	/**
	 * 獲得日曆日：該年的第×天
	 * @param currentDate
	 * @return
	 */
	public static int getCalandaDay(Date currentDate){
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(currentDate);
		 return cal.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 取得系統西元年字串日期 EX:20120101
	 *
	 * @return
	 */
	public static String getSystemDate() {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy" + "MM" + "dd"); // + "HH" + "mm" + "ss"
		return format.format(date);
	}
	/**
	 * 取到當日日期的前一天
	 * @return
	 */
	public static String getBeforeDate(){
		Date date = new Date();
		date=getYesterday();
		DateFormat format = new SimpleDateFormat("yyyy" + "MM" + "dd"); // + "HH" + "mm" + "ss"
		return format.format(date);
	}
	/**
	 * 取得系統西元年字串時間 EX:171020
	 *
	 * @return
	 */
	public static String getSystemTime() {
		Calendar cal=Calendar.getInstance();
		String   hours=  "0" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
		String   minute= "0" +  String.valueOf(cal.get(Calendar.MINUTE));
		String   second= "0" + String.valueOf(cal.get(Calendar.SECOND));
		String systime =  hours.substring(hours.length() - 2) +  minute.substring(minute.length() - 2 ) + second.substring(second.length() - 2 );
		return systime;
	}

	/**
	 * 取該月份第一天
	 * @param date
	 * @return
	 */
	public static int getMonthFirstDate(int date){
			String str = String.valueOf(date);
			str = str.substring(0, 6) + "01";
			Integer i = Integer.valueOf(str);
		return i.intValue();
	}

	/**
	 * 取該月份最後一天
	 * @param date
	 * @return
	 */
	public static int getMonthLastDate(int date){
			String str = String.valueOf(date);
			str = str.substring(0, 6) + "01";
			Integer i = Integer.valueOf(str);
			Date lastDate = DateUtils.addDate(DateUtils.addMonth(DateUtils.intToDate(i), 1), -1);
		return Integer.valueOf(dateToString(lastDate)).intValue();
	}

	/**
	 * 取現在時間的timestamp  字串
	 * @return
	 */
	public static String getNowByTimeStamp (){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp now = new Timestamp(System.currentTimeMillis());
		return df.format(now);
	}


	/**
	 * 比較兩個時間 差距
	 * @return
	 */
	public static int differenceTimeStamp1 (String timeStamp1,String timeStamp2){
		Timestamp ts1 = Timestamp.valueOf(timeStamp1);

		Timestamp ts2 = Timestamp.valueOf(timeStamp2);
//		System.out.println(ts1.getTime() - ts2.getTime());//取兩個date的long值相減,就是微秒
		int diffTime = (int)((ts1.getTime() - ts2.getTime() ) / (1000 * 60)); //目前暫設5分鐘
		return diffTime;
	}

	public static String getDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(System.currentTimeMillis());
	}


	/**
	 * 传入一个时间字符串，返回时间格式
	 * @param dateString
	 * @return
	 */
	public static DateFormat getDateFormat(String dateString){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df2 = new SimpleDateFormat("HH:mm:ss");
		DateFormat df3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Pattern pattern=Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
		Matcher matcher = pattern.matcher(dateString);
		if(matcher.matches())
			return df;
		pattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
		matcher = pattern.matcher(dateString);
		if(matcher.matches())
			return df1;
		pattern=pattern.compile("(\\d{2}):(\\d{2}):(\\d{2})");
		matcher = pattern.matcher(dateString);
		if(matcher.matches())
			return df2;
		pattern=pattern.compile("(\\d{4})/(\\d{2})/(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");
		matcher = pattern.matcher(dateString);
		if(matcher.matches())
			return df3;
		return null;
	}

	/**
	 * 获取当前时间的前某个月
	 * @param i
	 * @return
	 */
	public static String getLastAnyMonth(int i){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -i);
		Date m = c.getTime();
		return sdf.format(m);
	}

	public static void main(String[] args) {
		System.out.println(stringToDate("2018-09-10 10:00","yyyy-MM-dd HH:mm"));
		System.out.println(isDate("2018-09-10 10:00:00"));
		System.out.println(isLegalDate("2018-09-10 10:00"));
		//System.out.println(isDate("2018-09-10"));
		//System.out.println(getLastAnyMonth(4));
	}


}