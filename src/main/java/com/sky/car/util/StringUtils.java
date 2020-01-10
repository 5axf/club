package com.sky.car.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

public class StringUtils {

	private static Random randGen = new Random();
	private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
			+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
	private static char[] numbers = ("0123456789").toCharArray();

	
	//字符串切分方法（字符串  切分符号  ）
	public static String[] mySplit(String srcStr,String token){
		int fromIndex = 0;
		int count = 0;
		
		if(null == srcStr || "".equals(srcStr)){
			return null;
		}
		
		while(fromIndex >= 0){
			fromIndex = srcStr.indexOf(token,fromIndex);
			if(fromIndex >=0 ){
		      count++;
		      fromIndex++;
			}
		}
	    count++;
	    
		int i = 0;
		int endIndex = 0;
		String [] rltStr = new String [count];
		fromIndex = 0;
		while(fromIndex >= 0 && i < count){
			endIndex = srcStr.indexOf(token,fromIndex);
			if(endIndex >=0){
				if(endIndex > fromIndex){
			       rltStr[i] = srcStr.substring(fromIndex, endIndex);
				} else {
				   rltStr[i] = "";
				}
				i++;
				fromIndex = endIndex + 1;
			} else {
				rltStr[i] = srcStr.substring(fromIndex);
				fromIndex = endIndex;
			}
		}
		return rltStr;
	}
	
	/**
	 * 获得随机的字符串
	 * 
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
		return new String(randBuffer).toLowerCase();
	}

	public static String randomNum(int length) {
		if (length < 1) {
			return null;
		}
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbers[randGen.nextInt(10)];
		}
		return new String(randBuffer);
	}

	/**
	 * 将指定的字符串中的指定字符，替换为新字符
	 * 
	 * @param str
	 *            指定串
	 * @param oldStr
	 *            老字符串
	 * @param newStr
	 *            替换的新字符串
	 * @return
	 */
	public static String replaceAll(String str, String oldStr, String newStr) {
		if (str.indexOf(oldStr) != -1) {
			str = str.replace(oldStr, newStr);
		}
		return str;
	}

	/**
	 * 获得指定内容中，有start开始，end结尾的内容列表
	 * 
	 * @param content
	 *            内容
	 * @param start
	 *            开始内容
	 * @param end
	 *            截止内容
	 * @return 符合条件的内容列表
	 */
	public static List<String> getContentBetween(String content, String start,
			String end) {
		int index = content.indexOf(start);
		List<String> results = new ArrayList<String>();
		while (index != -1) {
			int e = content.indexOf(end, index);
			if (e == -1) {
				break;
			}
			results.add(content.substring(index + start.length(), e));
			index = content.indexOf(start, e);
		}
		return results;
	}

	/**
	 * 根据指定的URL获得对应的内容
	 * 
	 * @param url
	 *            指定的url
	 * @return 获得的内容
	 * @throws IOException
	 */
	public static String getContent(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String temp = null;
		StringBuilder content = new StringBuilder();
		while ((temp = reader.readLine()) != null) {
			content.append(temp);
		}
		return content.toString();
	}

	/**
	 * 截断字符串(按字节截取)
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String trimStringInLength(String s, int n) {
		if (n == 0) {
			return s;
		}
		if (s == null)
			return "";
		// if (str.getBytes().length < toCount)
		// return str;
		// int reInt = 0;
		// String reStr = "";
		// char[] tempChar = str.toCharArray();
		// for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
		// String s1 = str.valueOf(tempChar[kk]);
		// byte[] b = s1.getBytes();
		// reInt += b.length;
		// reStr += tempChar[kk];
		// }
		// if (toCount == reInt || (toCount == reInt - 1))
		// reStr += "...";
		// return reStr;

		// byte[] bytes;
		// try {
		// bytes = input.getBytes("GBK");
		//		
		// byte[] data = new byte[n]; // 结果
		//
		//
		// int x = 0;
		// int y = 0;
		// while (x < n && x < bytes.length ) {
		// int length = Character.toString(input.charAt(y)).getBytes().length;
		// if (length > 1) { // 双字节
		//
		// if (x >= n - 1) { // 如果是最后一个字节
		//
		// break;
		// } else {
		// data[x] = bytes[x];
		// data[x + 1] = bytes[x + 1];
		// x += 2;
		// y++;
		// }
		// } else {
		// data[x] = bytes[x];
		// x++;
		// y++;
		// }
		// }
		//        
		// return new String(data, 0, x);
		// } catch (UnsupportedEncodingException e) {
		// 
		// }
		// return input;

		int index = 0; // 定义游标位置
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < n && index < s.length(); i++) {
			if (s.charAt(index) < 255 && s.charAt(index) > 0
					|| Character.isDigit(s.charAt(index))) {

				ss.append(s.charAt(index)); // 如果当前字符不输于字母或者是数字，则添加到结果。
				index++;
			} else {
				ss.append(s.charAt(index)); // 如果当前字符是汉字，则添加到结果中，游标向前移动一位。
				index++;
				i++;
			}
		}
		if (index < s.length()) {
			ss.append("...");
		}
		return ss.toString();

	}

	/**
	 * 截断字符串(按字符截取)
	 * 
	 * @param str
	 *            被截取的字符
	 * @param length
	 *            截取的长度
	 * @param format
	 *            截取后添加的字符串,通常是"..."
	 * @return
	 */
	public static String trimStringInLength(String str, int toCount,
			String format) {
		if (str == null)
			return "";
		if (format == null)
			format = "";
		if (str.length() > toCount) {
			str = str.substring(0, toCount - 1) + format;
		}
		return str;
	}


	/**
	 * 
	 * 去除所有html元素
	 * 
	 * 
	 */

	public static String dropHtml(String Source) {
		if (isBlank(Source)) {
			return "";
		}
		Source.trim();
		String excuteString = null;
		int debut;
		int fin;
		debut = Source.indexOf("<");
		fin = Source.indexOf(">");

		while ((debut >= 0) && (fin > 0)) {
			debut = Source.indexOf("<");
			fin = Source.indexOf(">");
			if (debut < 0) {
				break;
			}
			if (debut >= fin) {
				debut = 0;
			}
			excuteString = null;
			excuteString = Source.substring(0, debut);
			excuteString.trim();
			excuteString = excuteString + "" + Source.substring(fin + 1).trim();
			Source = excuteString;
		}
		Source = Source.replaceAll("&nbsp", "");
		return Source;

	}

	/**
	 * 去除所有的html标记
	 * 
	 * @param input
	 * @param length
	 * @return
	 */
	public static String dropHtmlTag(String input) {
		if (input == null || input.trim().equals("")) {
			return "";
		}
		// 去掉所有html元素
		String str = input.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll(
				"<[^>]*>", "");
		str = str.replaceAll("[(/>)<]", "");
		return str;
	}

	/**
	 * 去除Script标记
	 * 
	 * @param input
	 *            目标字符串
	 * @return
	 */
	public static String dropScriptTag(String input) {
		if (isBlank(input))
			return "";
		String rs = input.replaceAll("<script[^>]*?>.*?</script>", "");
		return rs;
	}

	/**
	 * <p>
	 * 检查字符串,字符串为空格,("") 或null
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isBlank(null)      = true
	 * StringUtil.isBlank(&quot;&quot;)        = true
	 * StringUtil.isBlank(&quot; &quot;)       = true
	 * StringUtil.isBlank(&quot;bob&quot;)     = false
	 * StringUtil.isBlank(&quot;  bob  &quot;) = false
	 * </pre>
	 * 
	 * @param str
	 *            目标字符串,或者Null
	 * @return <code>true</code> 如果目标字符串,是null,("")或空格
	 */
	
	public static boolean isBlank(String str){
		return str==null || str.equals("null");
	}

	/**
	 * <p>
	 * 检查字符串,字符串不为空格,不为("") 或不为null
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.isNotBlank(null)      = false
	 * StringUtil.isNotBlank(&quot;&quot;)        = false
	 * StringUtil.isNotBlank(&quot; &quot;)       = false
	 * StringUtil.isNotBlank(&quot;bob&quot;)     = true
	 * StringUtil.isNotBlank(&quot;  bob  &quot;) = true
	 * </pre>
	 * 
	 * @param str
	 *            目标字符串,或Null
	 * @return <code>true</code> 如果目标字符串不为("")和不为null和不为空格
	 */
	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 替换一个字符串中第一次出现的keywords
	 * 
	 * @param source
	 *            需要修改的字符串
	 * @param keyWords
	 *            需要替换的关键字
	 * @param targetWords
	 *            替换后的关键字
	 * @return
	 */
	public static String replaceIndexKeyWords(String source, String keyWords,
			String targetWords) {
		if (!isBlank(keyWords)) {
			if (keyWords.startsWith("?")) {
				return source;
			}
		}
		// if(isBlank(source)){
		// return "";
		// }else{
		// if(source.indexOf(keyWords)!=-1){
		// String front= source.substring(0,
		// source.indexOf(keyWords)+keyWords.length());
		// String back =
		// source.substring(source.indexOf(keyWords)+keyWords.length());
		// return front.replaceAll(keyWords, targetWords)+back;
		// }else{
		// return source;
		// }
		// }
		return (source == null ? "" : source).replaceAll((keyWords == null ? ""
				: keyWords), (targetWords == null ? "" : targetWords));
	}

	/**
	 * 把文本编码为Html代码
	 * 
	 * @param target
	 * @return 编码后的字符串
	 */
	public static String htmEncode(String target) {
		if (isBlank(target)) {
			return "";
		}
		StringBuffer stringbuffer = new StringBuffer();
		int j = target.length();
		for (int i = 0; i < j; i++) {
			char c = target.charAt(i);
			switch (c) {
			case 39:
				stringbuffer.append("&#39;");
				break;
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && target.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && target.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

	/**
	 * 将数组转换为'1,2,'格式
	 * 
	 * @param array
	 * @return
	 */
	public static String arrayToString(String[] array) {
		StringBuffer str = new StringBuffer();
		if (array != null) {
			for (String string : array) {
				str.append(string + ",");
			}
		}
		return str.toString();
	}

	/**
	 * 替换单引号(')，分号(;) 和 注释符号(--) 用于防止sql注入
	 * @param str
	 * @return
	 */
	public static String transactSQLInjection(String str) {
		return str.replaceAll(".*([';]+|(--)+).*", " ");
	}

	/**
	 * <p>
	 * 截取字符串从指定的位置开始到指定的结束位置
	 * </p>
	 * <p>
	 * 如果开始位置大于结束位置那么则返回<code> "" </code>
	 * </p>
	 * <p>
	 * 如果目标字符串为<code>null</code> 则返回<code>null</code>
	 * </p>
	 * 
	 * <pre>
	 * StringUtil.substring(null, *, *)    = null
	 * StringUtil.substring(&quot;&quot;, * ,  *)    = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 0, 2)   = &quot;ab&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 0)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 4)   = &quot;c&quot;
	 * StringUtil.substring(&quot;abc&quot;, 4, 6)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, 2, 2)   = &quot;&quot;
	 * StringUtil.substring(&quot;abc&quot;, -2, -1) = &quot;b&quot;
	 * StringUtil.substring(&quot;abc&quot;, -4, 2)  = &quot;ab&quot;
	 * </pre>
	 * 
	 * @param str
	 *            目标字符串,或者Null
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return 如果开始位置大于结束位置那么返回<code>""</code>,如果目标字符串为<code>null</code>那么则返回<code>null</code>
	 */
	public static String substring(String str, int start, int end) {
		if (str == null) {
			return null;
		}
		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}
		if (end > str.length()) {
			end = str.length();
		}
		if (start > end) {
			return "";
		}
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}
		if (end < str.length()) {
			return str.substring(start, end);
		}
		return str.substring(start, end);
	}
	
	/**
	 * access_token=F553148995D97D39357A841DAF25D398&expires_in=7776000
	 * 根据标识（sign）拆分字符串（str）为数组(String[])
	 * 将access_token=F553148995D97D39357A841DAF25D398和expires_in=7776000放入map中
	 * 再将map存入List中
	 * @param str
	 * @param sign
	 * @return
	 */
	public static List<HashMap<String,Object>> splitBySign(String str , String sign ,String regex){
		String[] strs = str.split(sign);
		List<HashMap<String,Object>>  params =  new ArrayList<HashMap<String,Object>>();
		for(String temp:strs){
			String[] para = temp.split(regex);
			HashMap<String,Object> hm = new HashMap<String,Object>();
			hm.put(para[0], para[1]);
			params.add(hm);
		}
		return params;
	}
	
	/**
	 * 参数编码
	 * 
	 * @param value
	 * @return
	 */
	public static String encode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace(
					"*", "%2A").replace("%7E", "~").replace("#", "%23");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 参数反编码
	 * 
	 * @param s
	 * @return
	 */
	public static String decode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	/**
	 * 本方宗旨在于把 用逗号隔开 或者没有逗号的 的字符串 编程适合用于SQL in 语句的表达式
	 * 目标形式是: 'aa','bb','cc'  或者 'aa', 可以直接拼接成SQL  .. in ('aa','bb','cc').. 或者 .. in ('aa')..
	 * 输入的形式可以已经是目标形式 或者定义良好的用逗号分隔的形式 如 aa,bb  aaa  'aaa','bbb'
	 * @param str
	 * @return
	 */
	public static String changeToSqlFormat(String str){
		if(isBlank(str)){
			return "''";
		}
		
		if(!str.contains("'")){
			str="'"+str+"'";
		}
		
		if(str.contains(",")&&!str.contains("','")){
			str=str.replaceAll(",", "','");
		}
		return str;
	}
	
    //32位整型格式的IP地址(little-endian)转化到字符串格式的IP地址
    public static String long2StringIP(long ip)

    {

        long ip4 = ip >> 0 & 0x000000FF;

        long ip3 = ip >> 8 & 0x000000FF;

        long ip2 = ip >> 16 & 0x000000FF;

        long ip1 = ip >> 24 & 0x000000FF;

        

        return String.valueOf(ip1) + "." + String.valueOf(ip2) + "." + 

                String.valueOf(ip3) + "." + String.valueOf(ip4);

    }

    /**
	 * 通过展会类别获取对应的展会预告列表页路径
	 * @param exType
	 * @return
	 */
	public static String getFileUrl(String fileUrl, String exType){
		if(StringUtils.isNotBlank(exType)){
			int type = Integer.parseInt(exType);
			switch (type) {
			case 1001:
				fileUrl += "ruanzhuang/";
				break;
			case 1002:
				fileUrl += "dengshi/";
				break;
			case 1003:
				fileUrl += "wujin/";
				break;
			case 1004:
				fileUrl += "hongmu/";
				break;
			case 1005:
				fileUrl += "diban/";
				break;
			case 1006:
				fileUrl += "chugui/";
				break;
			case 1007:
				fileUrl += "tuliao/";
				break;
			case 1008:
				fileUrl += "louti/";
				break;
			case 1009:
				fileUrl += "weiyu/";
				break;
			case 1010:
				fileUrl += "menchuang/";
				break;
			case 1011:
				fileUrl += "jiaju/";
				break;
			case 1012:
				fileUrl += "jiancai/";
				break;
			case 1013:
				fileUrl += "shicai/";
				break;
			case 1014:
				fileUrl += "taoci/";
				break;
			case 1015:
				fileUrl += "yigui/";
				break;
			case 1016:
				fileUrl += "qita/";
				break;
			}
		}
		return fileUrl;
	}
    
    //字符串格式的IP地址转化到32位整型格式的IP地址(little-endian)
    public static Long stringIP2Long(String ipStr) throws Exception

    {

        String[] list = ipStr.split("\\.");

        if(list.length != 4)

        {

            throw new Exception("IP地址格式错误");

        }

        long ip = Long.parseLong(list[0]) << 24 & 0xFF000000;

        ip += Long.parseLong(list[1]) << 16 & 0x00FF0000;

        ip += Long.parseLong(list[2]) << 8 & 0x0000FF00;

        ip += Long.parseLong(list[3]) << 0 & 0x000000FF;

        return ip;

    }
    
    /**
     * 查找字符串中包含多少个指定的字符串
     * @param str 要查找的字符串
     * @param findStr 指定的字符串
     * @return 总数
     */
    public static int getCount(String str, String findStr){
    	int count = 0;
		if(str.indexOf(findStr) >= 0){
			count++;
			count = StringUtils.count(str.substring(str.indexOf("[page]") + findStr.length()), findStr, count);
		}
    	return count;
    }
    
    private static int count(String str, String findStr, int count){
    	if(str.indexOf(findStr) >= 0){
			count++;
			count = StringUtils.count(str.substring(str.indexOf("[page]") + findStr.length()), findStr, count);
		}
    	return count;
    }
    
    /**
     * 补全sql中的in中语句
     * @param id
     * @param sql
     * @return
     */
    public static StringBuffer sqlIn(String id, StringBuffer sql){
    	String[] ids = id.split(",");
    	for (int i = 0; i < ids.length; i++) {
    		sql.append("'"+ ids[i] +"',");
		}
    	sql = new StringBuffer(sql.toString().substring(0, sql.toString().length() - 1) + ") ");
    	return sql;
    }
    
    /**
     * 获取年月日三级目录
     * @return
     */
    public static String DateUrl(){
    	SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    	SimpleDateFormat mm = new SimpleDateFormat("MM");
    	SimpleDateFormat dd = new SimpleDateFormat("dd");
    	StringBuffer dateUrl = new StringBuffer();
    	dateUrl.append("/");
    	dateUrl.append(yyyy.format(new Date()));
    	dateUrl.append("/");
    	dateUrl.append(mm.format(new Date()));
    	dateUrl.append("/");
    	dateUrl.append(dd.format(new Date()));
    	dateUrl.append("/");
    	return dateUrl.toString();
    }
    
    
    /**
     * 去除文件路径中重复出现的斜杠和反斜杠
     * @param str
     * @return
     */
    public static String removeDuplicatedSlant(String str){
    	if(!str.contains("\\\\")&& !str.contains("\\/") && !str.contains("/\\") && !str.contains("//"))
    	{
    		return str;
    	}else{
    		str=str.replace("\\\\", "\\");
    		str=str.replace("\\/", "/");
    		str=str.replace("/\\", "/");
    		str=str.replace("//", "/");
    		return removeDuplicatedSlant(str);
    	}
    }
    
    /**
     * 删除文件
     * @param fileUrl 文件路径
     * @return
     */
    public static boolean delFile(String fileUrl){
    	if(StringUtils.isNotBlank(fileUrl)){
    		File file = new File(fileUrl);
        	return file.delete();
    	}
    	return false;
    }
    
    
    public static String replaceNullString(String val){
    	   if(null==val||"null".equals(val)) return "";
           return val;    	
    }
    
    /**
	 * 替换不能通过GET方式传递的字符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceNoneUrl(String str) {
		str = str.replaceAll("\\+", "%2B");
		str = str.replaceAll("\\/", "%2F");
		return str.replaceAll("\\=", "%3D");
	}

	/**
	 * 替换回不能通过GET方式传递的字符
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceFromUrl(String str) {
		str = str.replaceAll("%2B", "\\+");
		str = str.replaceAll("%2F", "\\/");
		return str.replaceAll("%3D", "\\=");
	}
	
	public static Double valueOf(Float f){
		if(f!=null){
			try {
				return Double.valueOf(f);
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
	
	public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        } else {
            boolean isNumber = str.matches("[0-9]+");
            return isNumber;
        }
    }

    public static String toString(String str) {
        return str != null && !"null".equalsIgnoreCase(str) ? str : "";
    }

    public static Integer stringToIntegerNullToNull(String str) {
        return str != null && !"".equals(str) ? Integer.valueOf(str) : null;
    }

    public static int stringToInt(String str) {
        return str != null && !"".equals(str) && !"null".equalsIgnoreCase(str) ? Integer.valueOf(str) : 0;
    }

    public static double stringToDouble(String str) {
        return str != null && !"".equals(str) && !"null".equalsIgnoreCase(str) ? Double.valueOf(str) : 0.0D;
    }

    public static Float stringToFloat(String str) {
        return str != null && !"".equals(str) && !"null".equalsIgnoreCase(str) ? Float.valueOf(str) : 0.0F;
    }

    public static int[] stringArrayToIntArray(String[] ss) {
        int[] ii = new int[ss.length];

        for(int i = 0; i < ss.length; ++i) {
            ii[i] = stringToInt(ss[i]);
        }

        return ii;
    }

    public static float[] stringArrayToFloatArray(String[] ss) {
        float[] ff = new float[ss.length];

        for(int i = 0; i < ss.length; ++i) {
            ff[i] = stringToFloat(ss[i]);
        }

        return ff;
    }

    public static double[] stringArrayToDoubleArray(String[] ss) {
        double[] dd = new double[ss.length];

        for(int i = 0; i < ss.length; ++i) {
            dd[i] = stringToDouble(ss[i]);
        }

        return dd;
    }

    public static String listToString(List list) {
        if (list == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < list.size(); ++i) {
                Object obj = list.get(i);
                if (i != 0) {
                    sb.append(",");
                }

                sb.append(obj.toString());
            }

            return sb.toString();
        }
    }

    public static String intArrayToString(Object[] oo) {
        if (oo == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < oo.length; ++i) {
                Object obj = oo[i];
                if (i != 0) {
                    sb.append(",");
                }

                sb.append(obj.toString());
            }

            return sb.toString();
        }
    }

    public static int[] stringToIntArray(String source, String split) {
        String[] spits = source.split(split);
        int length = spits.length;
        int[] dest = new int[length];

        for(int i = 0; i < length; ++i) {
            dest[i] = stringToInt(spits[i]);
        }

        return dest;
    }

    public static boolean toBoolean(String str) {
        str = str.trim();
        return str.equals("1");
    }

    public static boolean intToBoolean(int i) {
        return i == 1;
    }

    public static int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static String firstToUpperCase(String str) {
        String s = str.substring(0, 1).toUpperCase() + str.substring(1);
        return s;
    }

    public static String firstToLowerCase(String str) {
        String s = str.substring(0, 1).toLowerCase() + str.substring(1);
        return s;
    }

    public static String intToBinaryString(int num) {
        StringBuilder builder = new StringBuilder();
        String s = "";

        for(int i = 0; i < 31; ++i) {
            if ((num & 1) == 1) {
                s = "1";
            } else {
                s = "0";
            }

            builder.append(s);
            num >>= 1;
        }

        return builder.reverse().toString();
    }

    public static int binaryStringToInt(String str) {
        char[] cc = str.toCharArray();
        int num = 0;

        for(int i = 0; i < cc.length; ++i) {
            num += (int)Math.pow(2.0D, (double)(cc.length - 1 - i)) * Integer.valueOf(String.valueOf(cc[i]));
        }

        return num;
    }

    public static final String toHex(byte[] hash) {
        StringBuffer buf = new StringBuffer(hash.length * 2);

        for(int i = 0; i < hash.length; ++i) {
            if ((hash[i] & 255) < 16) {
                buf.append("0");
            }

            buf.append(Long.toString((long)(hash[i] & 255), 16));
        }

        return buf.toString();
    }

    public static String md5(String inputStr) {
        String pwd = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] s = digest.digest(inputStr.getBytes("UTF-8"));
            pwd = toHex(s);
            return pwd;
        } catch (NoSuchAlgorithmException var4) {
            System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
            var4.printStackTrace();
            return null;
        } catch (Exception var5) {
            System.err.println("Failed to load the MD5 MessageDigest. Jive will be unable to function normally.");
            var5.printStackTrace();
            return null;
        }
    }

    public static String md5HashValue(String value) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException var3) {
            var3.printStackTrace();
        }

        md.update(value.getBytes());
        return toHexString(md.digest());
    }

    private static String toHexString(byte[] in) {
        StringBuilder hexString = new StringBuilder();

        for(int i = 0; i < in.length; ++i) {
            String hex = Integer.toHexString(255 & in[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String lPad(String s, int paddedLength, String pad) {
        StringBuilder sb = new StringBuilder(s);

        while(sb.length() < paddedLength) {
            sb.insert(0, pad);
        }

        String result = sb.substring(0, paddedLength);
        return result;
    }

    public static String rPad(String s, int paddedLength, String pad) {
        StringBuilder sb = new StringBuilder(s);

        while(sb.length() < paddedLength) {
            sb.append(pad);
        }

        String result = sb.substring(0, paddedLength);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(rPad("12345678", 12, "2"));
        System.out.println(lPad("12345678", 12, "2"));
    }
	
}
