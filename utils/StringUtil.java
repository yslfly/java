
package com.uid.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：汪询原 系统平台：企业通讯平台 ECC 版本号：1.0
 */

public class StringUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static String SEPARATOR = "\r\n";
	
	/**
	 * 
	 */
	public StringUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static boolean isEmpty(String str) {
		boolean result = true;
		if (str != null && !str.trim().equals("") && !str.trim().equals("null") && !str.trim().equals("undefined")) {
			result = false;
		}
		return result;
	}


	public static String createValue(String inStr) {
		// to be developed
		return inStr;
	}
	
	public static boolean isMobileNO(String mobiles){     
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");     
        Matcher m = p.matcher(mobiles);     
        return m.matches();     
    } 
   
    public static boolean isEmail(String email){     
     String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);     
        Matcher m = p.matcher(email);      
        return m.matches();     
    } 
    
    public static boolean isEnglishDigital(String email){
    	String str = "\\d+.\\d+|\\w+";
    	Pattern p = Pattern.compile(str);     
        Matcher m = p.matcher(email);      
        return m.matches();    
    }

	/**
	 * pad a string to the specified length, by adding a pad character to the
	 * left
	 * 
	 * @param s
	 *            String
	 * @param size
	 *            int
	 * @param padChar
	 */
	public static String padLeft(String s, int size, char padChar) {
		StringBuffer b = new StringBuffer(size);
		b.append(s);
		while (b.length() < size) {
			b.insert(0, padChar);
		}
		return b.toString();
	}

	public static String padLeft(String s, int size) {
		return padLeft(s, size, ' ');
	}

	/**
	 * Check if this string is empty when trimmed
	 */
	public static boolean isEmptyTrimmed(String s) {
		return (s == null || s.trim().length() == 0);
	}

	/**
	 * Convert a HashMap into a stringified representation of the form
	 * key1=value1&amp;key2=value2;
	 * 
	 * @param <code>HashMap</code>
	 * @return <code>String</code> string representation of this HashMap
	 */
	public static String toString(HashMap m) {
		StringBuffer buf = new StringBuffer(256);
		boolean first = true;
		for (Iterator i = m.entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			if (!first) {
				buf.append('&');
			}
			buf.append(e.getKey());
			buf.append('=');
			buf.append(e.getValue());
			first = false;
		}
		return buf.toString();
	}

	/**
	 * Convert a String of the form key1=value1&amp;key2=value2 into a HashMap
	 * of key value pairs.
	 * 
	 * @param <code>String</code> the stringified representation of a HashMap
	 * @return <code>HashMap</code>
	 */
	public static HashMap fromString(String s) {
		String[] sa = split(s, "&");
		final int len = sa.length;
		HashMap m = new HashMap();
		for (int i = 0; i < len; i++) {
			split(sa[i], '=', m);
		}
		return m;
	}

	/**
	 * Split the string of the form a=b into two tokens, and add it to the
	 * HashMap as a key, value pair.
	 * 
	 * @param <code>String</code> the string to split
	 * @param <code>Map</code> map to add to.
	 */
	public static void split(String s, char sep, Map m) {
		if (isEmpty(s)) {
			return;
		}
		final int kvlen = s.length();
		int idx = s.indexOf(sep);
		String key = null;
		String value = null;
		if (idx < 0) {
			key = s;
		}
		key = s.substring(0, idx);
		if (idx < kvlen) {
			value = s.substring(idx + 1);
		}
		// Add a key value pair
		if (key != null && value != null) {
			m.put(key, value);
		}
	}

	/**
	 * Create a string array from a string separated by delim
	 * 
	 * @param line
	 *            the line to split
	 * @param delim
	 *            the delimter to split by
	 * @return a string array of the split fields
	 */
	public static String[] split(String line, String delim) {
		ArrayList v = new ArrayList();
		StringTokenizer t = new StringTokenizer(line, delim);
		while (t.hasMoreTokens())
			v.add(t.nextToken());

		String[] s = (String[]) v.toArray(new String[0]);
		return s;
	}

	public static String convertToHtmlNewline(String message) {
		StringBuffer strBuf = new StringBuffer();
		StringTokenizer st = new StringTokenizer(message, SEPARATOR);
		while (st.hasMoreTokens()) {
			strBuf.append(st.nextToken()).append("<br>");
		}
		return strBuf.toString();
	}

	public static String stripZeroAndNumberSignFromInput(String inputstring) {
		int index = -1;
		String result = inputstring.trim();
		while (true) {
			if (result.startsWith("0") && !result.equals("")) {
				result = result.substring(1);
				continue;
			}
			if (result.endsWith("#") && !result.equals("")) {
				result = result.substring(0, result.length() - 1);
				continue;
			}
			if (result.endsWith("*") && !result.equals("")) {
				result = result.substring(0, result.length() - 1);
				continue;
			}
			break;
		}
		return result;
	}

	public static String stripNumberSignFromInput(String inputstring) {
		if (inputstring == null || inputstring.trim().equals("")) {
			return "";
		}
		int index = -1;
		String result = inputstring.trim();
		while (true) {
			if (result.endsWith("#") && !result.equals("")) {
				result = result.substring(0, result.length() - 1);
				continue;
			}
			break;
		}
		return result;
	}
	
	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author zhangfan.ye
	 */
	public static String formatDuring(long mss) throws Exception{
		long year = mss /(1000*60*60*24*365);
		long days = (mss % (1000*60*60*24*365)) / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		StringBuffer result = new StringBuffer();
		if(year!=0){
			result.append(year+new String("年".getBytes("gb2312")));
		}
		if(days!=0){
			result.append(days+new String("个月".getBytes("gb2312")));
		}
		if(hours!=0){
			result.append(hours+new String("小时".getBytes("gb2312")));
		}
		if(minutes!=0){
			result.append(minutes+new String("分钟".getBytes("gb2312")));
		}
		if(seconds!=0){
			result.append(seconds+new String("秒".getBytes("gb2312")));
		}
		if("".equals(result.toString())){
			return "0";
		}
		return result.toString();
	}
	
	/**
	 * 获得批号日期
	 * @return
	 */
	public static StringBuffer lotNumberDate(){
		Calendar c = Calendar.getInstance();
		StringBuffer str = new StringBuffer();
	    String year = String.valueOf(c.get(Calendar.YEAR));
	    String month = String.valueOf(c.get(Calendar.MONTH)+1);
	    month = month.length()<2?"0"+month:month;
	    year = year.length()<2?"0"+year:year;
	    String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
	    str.append(year.substring(2)).append(".");
	    str.append(month).append(".");
	    str.append(day);
		return str;
	}
	
	/**
	 * 获得批号日期
	 * @return
	 */
	public static String lotNumberToYMD(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(new Date());
	}
	
	/**
	 * 根据日期类型转成Date
	 * @param time
	 * @param type
	 * @return
	 */
	public static Date pareseDateByString(String times,String type){
		Calendar c = Calendar.getInstance();
		int field = 0;
		int time = pareseInteger(times);
		if(time!=-1){
			if("小时".equals(type)){
				field = Calendar.HOUR;
			}else if("天".equals(type)){
				field = Calendar.DAY_OF_MONTH;
			}else if("月".equals(type)){
				field = Calendar.MONTH;
			}else if("年".equals(type)){
				field = Calendar.YEAR;
			}
			c.add(field, time);
			return c.getTime();
		}
		return null;
	}
	 
	
	/**
	 * 字符串转LONG
	 * @param str
	 * @return
	 */
	public static long pareseLong(String str){
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 字符串转LONG
	 * @param str
	 * @return
	 */
	public static long pareseLong(Object obj){
		try {
			return Long.parseLong(obj.toString());
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static double pareseDouble(String str){
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static int pareseInteger(String str){
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return -1;
		}
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static String converDateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static String converDateToYMDString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static Date converStringToDate(String str)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static String converTimestampToString(Timestamp time){
		Date date = new Date(time.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static Timestamp converDateToTimeStamp(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Timestamp time = null;
		try {
			Date date = sdf.parse(str);
			time = new Timestamp(date.getTime());
		} catch (ParseException e) {
			return null;
		}
		return time;
	}
	
	/**
	 * 字符串转日期
	 * @param str
	 * @return
	 */
	public static Timestamp converDateToTimeStampYYYYMMDDHHmmss(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp time = null;
		try {
			Date date = sdf.parse(str);
			time = new Timestamp(date.getTime());
		} catch (ParseException e) {
			return null;
		}
		return time;
	}
	
	/**
	 * 获得当前年月日
	 * @return
	 */
	public static int getNowDay(){
		SimpleDateFormat sdf =  new SimpleDateFormat("yyMMddHHmmss");
		return pareseInteger(sdf.format(new Date()));
	}
	
	public static String getTimeMillis(){
		SimpleDateFormat sdf =  new SimpleDateFormat("yyMMddHHmmss");
		String time =  "123456"+sdf.format(new Date());
		//time = 123456+System.currentTimeMillis()+;
		return time;
	}
	
	/**
	 * 获取过期时间
	 * @param chargeType
	 * @return
	 */
	public static Date getOverDue(String chargeType){
		Date d=null;
		Calendar date = Calendar.getInstance();
		if("年".equals(chargeType)){
			date.add(Calendar.YEAR, 1);
			d = date.getTime();
		}else if("月".equals(chargeType)){
			date.add(Calendar.MONTH, 1);
			d = date.getTime();
		}else if("小时".equals(chargeType)){
			date.add(Calendar.HOUR, 1);
			d = date.getTime();
		}
		return d;
	}
	
	/**
	 * 日期累加
	 * @param day
	 * @return
	 */
	public static Date addDate(int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public static String getStringByLength(String content,int length)
	{
		if(content.length() > length)
		{
			content = content.substring(length);
			return content +"......";
		}
		return content;
	}
	
	public static String converDouble(double d){
		DecimalFormat df = new DecimalFormat("#.00");
		return df.format(d);
		
	}
	/**
	 *检查字符串是否为数字
	 * @param str
	 * @return
	 */
	   public static boolean checkIsNumber(String str){
			  boolean result = false;		
			  Pattern pattern = Pattern.compile("[0-9]{1,}");
			  Matcher matcher = pattern.matcher((CharSequence)str);
			  result = matcher.matches();				
			  return result;
		}
	   /**
		 *判断是否为正数
		 * @param str
	     * @return
		*/
			public static boolean checkIsPositiveNumber(String str){
				  boolean result = false;		
				  Pattern pattern = Pattern.compile("([1-9]\\d*((.\\d+)*))|(0.\\d+)");
				  Matcher matcher = pattern.matcher((CharSequence)str);
				  result = matcher.matches();				
				  return result;
			}
	/**
	 *检查是否为小数
	 * @param str
     * @return
	*/
		public static boolean checkIsDecimalNumber(String str){
			  boolean result = false;		
			  Pattern pattern = Pattern.compile("\\d+.\\d+");
			  Matcher matcher = pattern.matcher((CharSequence)str);
			  result = matcher.matches();				
			  return result;
		}
    /**
     *检查字符串是否为邮箱
	 * @param str
	 * @return		 
	*/
	  public static boolean checkIsEmail(String str){	
		      Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
			  Matcher matcher = pattern.matcher(str);
			  return matcher.matches();
	  }
	/**
	 *检查字符串是否为手机
	 * @param str
	 * @return		 
	*/
	  public static boolean checkIsPhone(String str){
			  Pattern pattern = Pattern.compile("^(((1[3-8]{1}))+\\d{9})$");        
			  Matcher matcher = pattern.matcher(str);
			  return matcher.matches();
	  }
	  
	  /**
	   * 四舍五入
	   * @param v
	   * @return
	   */
	  public static double converDecimal(double v){
		  BigDecimal   b   =   new   BigDecimal(v);  
		  return   b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
	  }
	    
	  // 检查字符串长度,返回null则说明字符串为空或者限制条件不符合规则
	  public  static  Boolean checkStrIsInLengthLimit(String str, int lowerLimit, int upperLimit) {
			if(str != null && lowerLimit < upperLimit){
				int strLength = str.length();
				return (strLength <= upperLimit && strLength >= lowerLimit);
			}else {
				return null;
			}
	   }
	  
	  /**
	   * 用户名合法性
	   * */
	  public static boolean checkUserName(String userName) {
		  String regex = "^[a-zA-Z]\\w*$";
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(userName);
		  return m.matches();
		 }
	  
	  /**
	   * 左边自动补零
	   */
	  public static String autoAddLeftZero(int targetNum,int zeroNum) {
		  return  String.format("%0"+zeroNum+"d", targetNum);   
		
	  }
	   
	  public static void main(String[] args) throws Exception {  
		  System.out.println(new Date().toLocaleString());
		
		  System.out.println(autoAddLeftZero(14,8));
	
	     System.out.println(new Date().toLocaleString());
		
	  }	
}
