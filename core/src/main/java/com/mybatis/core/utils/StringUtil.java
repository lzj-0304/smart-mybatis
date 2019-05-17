package com.mybatis.core.utils;

/**
 * 字符串工具类
 * @author Administrator
 *
 */
public class StringUtil {

	/**
	 * 判断字符串是否为空
	 * true : 空
	 * flase : 非空
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str) {
		if (str != null && !"".equals(str)) {
			return false;
		}
		return true;
	}

	/**
	 * 将字符串的首字母变大写
	 * @param str
	 * @return
	 */
	public static String firstChar2Upper(String str){
		return str.toUpperCase().charAt(0)+str.substring(1);
	}
}
