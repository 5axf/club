package com.sky.car.util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

public class Utils {
	private static Log log = LogFactory.getLog(Utils.class);
	
	public static boolean isEmpty(Object...  objects ) {
		
		if(objects!=null) {
			for(Object o : objects) {
				if(isEmpty(o)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static int countAll(Integer...  objects ) {
		int result = 0;
		if(objects!=null) {
			for(Integer o : objects) {
				int num = getInt(o);
				result+=num;
			}
		}
		return result;
	}
	
	public static boolean checkHasNotNull(Integer...  objects ) {
		boolean flag = false;
		if(objects!=null) {
			for(Integer o : objects) {
				Integer num = getInt(o);
				if(num!=null) {
					flag = true;
				}
			}
		}
		return flag;
	}
	
	public static int getInt(Integer i) {
		if(i==null) return 0;
		return i;
	}
	
	public static boolean isNotEmpty(Object...  objects ) {
		
		if(objects!=null) {
			for(Object o : objects) {
				if(isNotEmpty(o)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isEmpty(Object pObj) {
		if (pObj == null) {
			return true;
		}
		if (pObj.equals("")) {
			return true;
		}
		if (pObj.equals("null")) {
			return true;
		}
		if ((pObj instanceof String)) {
			if (((String) pObj).length() == 0)
				return true;
		} else if ((pObj instanceof Collection)) {
			if (((Collection<?>) pObj).size() == 0)
				return true;
		} else if (((pObj instanceof Map)) && (((Map<?, ?>) pObj).size() == 0)) {
			return true;
		}

		return false;
	}

	public static boolean isNotEmpty(Object pObj) {
		if (pObj == null) {
			return false;
		}
		if (pObj.equals("")) {
			return false;
		}
		if (pObj.equals("null")) {
			return false;
		}
		if ((pObj instanceof String)) {
			if (((String) pObj).length() == 0)
				return false;
		} else if ((pObj instanceof Collection)) {
			if (((Collection<?>) pObj).size() == 0)
				return false;
		} else if (((pObj instanceof Map)) && (((Map<?, ?>) pObj).size() == 0)) {
			return false;
		}

		return true;
	}
	
	
	public static Integer parseInt(Object obj) {
		if(isEmpty(obj)) {
			return null;
		}
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return null;
		}
	}

	public static void copyPropBetweenBeans(Object pFromObj, Object pToObj) {
		if (isNotEmpty(pToObj))
			try {
				BeanUtils.copyProperties(pToObj, pFromObj);
			} catch (Exception e) {
				log.error("JavaBean之间的属性值拷贝发生错误啦, 详细错误信息:" + e.getMessage());
			}
	}

	public static String getFixedPersonIDCode(String personIDCode) throws Exception {
		if (isEmpty(personIDCode)) {
			throw new Exception("输入的身份证号无效，请检查");
		}
		if (personIDCode.length() == 18) {
			if (isIdentity(personIDCode)) {
				return personIDCode;
			}
			throw new Exception("输入的身份证号无效，请检查");
		}
		if (personIDCode.length() == 15) {
			return fixPersonIDCodeWithCheck(personIDCode);
		}
		throw new Exception("输入的身份证号无效，请检查");
	}

	public static String fixPersonIDCodeWithCheck(String personIDCode) throws Exception {
		if ((isEmpty(personIDCode)) || (personIDCode.trim().length() != 15)) {
			throw new Exception("输入的身份证号不足15位，请检查");
		}

		if (!isIdentity(personIDCode)) {
			throw new Exception("输入的身份证号无效，请检查");
		}
		return fixPersonIDCodeWithoutCheck(personIDCode);
	}

	public static String fixPersonIDCodeWithoutCheck(String personIDCode) throws Exception {
		if ((isEmpty(personIDCode)) || (personIDCode.trim().length() != 15)) {
			throw new Exception("输入的身份证号不足15位，请检查");
		}

		String id17 = personIDCode.substring(0, 6) + "19" + personIDCode.substring(6, 15);

		char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int[] factor = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
		int[] idcd = new int[18];

		for (int i = 0; i < 17; i++) {
			idcd[i] = Integer.parseInt(id17.substring(i, i + 1));
		}
		int sum = 0;
		for (int i = 0; i < 17; i++) {
			sum += idcd[i] * factor[i];
		}
		int remainder = sum % 11;
		String lastCheckBit = String.valueOf(code[remainder]);
		return id17 + lastCheckBit;
	}

	public static boolean isIdentity(String identity) {
		if (isEmpty(identity)) {
			return false;
		}
		if ((identity.length() == 18) || (identity.length() == 15)) {
			String id15 = null;
			if (identity.length() == 18)
				id15 = identity.substring(0, 6) + identity.substring(8, 17);
			else
				id15 = identity;
			try {
				Long.parseLong(id15);
				String birthday = "19" + id15.substring(6, 12);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				sdf.parse(birthday);
				if ((identity.length() == 18) && (!fixPersonIDCodeWithoutCheck(id15).equals(identity)))
					return false;
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static String[] mergeStringArray(String[] a, String[] b) {
		if ((a.length == 0) || (isEmpty(a))) {
			return b;
		}
		if ((b.length == 0) || (isEmpty(b))) {
			return a;
		}
		String[] c = new String[a.length + b.length];
		for (int m = 0; m < a.length; m++) {
			c[m] = a[m];
		}
		for (int i = 0; i < b.length; i++) {
			c[(a.length + i)] = b[i];
		}
		return c;
	}

	public static BigDecimal getRandom(int start, int end) {
		return new BigDecimal(start + Math.random() * end);
	}

	public static boolean checkPagePar(Integer val) {
		return (val != null) && (val.intValue() > 0);
	}

	public static String getLocalIpAddress() {
		InetAddress localInet = null;
		String localIp = null;
		try {
			localInet = InetAddress.getLocalHost();
			localIp = localInet.getHostAddress().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return localIp;
	}
	
	public static String getBigNum(int i) {
		switch (i) {
		case 1: return "一";
		case 2: return "二";
		case 3: return "三";
		case 4: return "四";
		case 5: return "五";
		case 6: return "六";
		case 7: return "七";
		case 8: return "八";
		case 9: return "九";
		case 10: return "十";
		case 11: return "十一";
		case 12: return "十二";
		case 13: return "十三";
		case 14: return "十四";
		case 15: return "十五";
		case 16: return "十六";
		case 17: return "十七";
		case 18: return "十八";
		case 19: return "十九";
		case 20: return "二十";

		}
		return "";
	}

	/**
	 * 判断字符串里面是否存在表情符号特殊字符
	 * @param str
	 * @return
	 */
	public static boolean isSpecialString(String str){
		String regEx="[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]";
		Pattern p=Pattern.compile(regEx);
		Matcher m=p.matcher(str);
		return m.find();
	}

}