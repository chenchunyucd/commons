package ccy.work.commons.utils;

import org.apache.commons.lang.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 * @author chenchunyu
 *
 */
public class DateUtil {

	public static final String format = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 获取当月的起始时间点(自然月)
	 * @return
	 */
	public static Date thisMonthStartTime(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

	/**
	 * 获取当月的结束时间点(自然月)
	 * @return
	 */
	public static Date thisMonthEndTime(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}

	/**
     * 获取传入时间的月末时间点
     * @param date
     * @return
     */
    public static Date monthEndTime(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }


    public static void main(String[] args) {
        Date date = new Date();
        Date end = monthEndTime(date);
    }

    /**
     * 获取当年的起始时间点(自然年)
     * @return
	 */
	public static Date thisYearStartTime(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_YEAR, 1);
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

	/**
	 * 获取当年的结束时间点
	 * @return
	 */
	public static Date thisYearEndTime(){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_YEAR, ca.getActualMaximum(Calendar.DAY_OF_YEAR));
		ca.set(Calendar.HOUR_OF_DAY, 23);
		ca.set(Calendar.MINUTE, 59);
		ca.set(Calendar.SECOND, 59);
		return ca.getTime();
	}

	 /**
     * 计算两个日期之间相隔的天数
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws java.text.ParseException
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
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
    }   
    
    /**
     * 获取上月日期（自然月）
     * @return
     */
    public static Date lastMonth() {
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -1);
		return ca.getTime();
	}

    /**
     * 获取当天最后时间点 23:59:59
     *
     * @return
     */
    public static Date thisDayEndTime() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

    /**
     * 获取当天起始时间点 0:0:0
     *
     * @return
     */
    public static Date thisDayStartTime() {
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

    public static String format(Date date) {
        if(date==null) {
            return null;
        }
        String s = DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        if(s==null) {
            return DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return s;
    }

    public static final Date parseDate(String strDate, String format) {

        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);

        try {
            date = df.parse(strDate);
        } catch (ParseException e) {}

        return (date);
    }

    public static Date formatDate(Date date,String format) {
        SimpleDateFormat inDf = new SimpleDateFormat(format);
        SimpleDateFormat outDf = new SimpleDateFormat(format);
        String reDate = "";
        try {
            reDate = inDf.format(date);
            return outDf.parse(reDate);
        } catch (Exception e) {

        }
        return date;
    }
    

}
