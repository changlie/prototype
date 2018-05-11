package com.yunjia.util;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public abstract class StrUtil {

	/**
	 * 中文转拼音
	 * <p> 北京  -> beijing
	 * @param chinese
	 * @return
	 */
	public static String toPinyin(String chinese) {
		StringBuilder pinyin = new StringBuilder();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		int len = chinese.length();
		for (int i = 0; i < len; i++) {
			char c = chinese.charAt(i);

			String[] pinyinArray = null;
			try {
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, defaultFormat);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			if (pinyinArray != null) {
				pinyin.append(pinyinArray[0]);
			} else if (c != ' ') {
				pinyin.append(c);
			}
		}
		return pinyin.toString();
	}

	/**
	 * [1,2,3] => "1,2,3"
 	 * @param source
	 * @return
	 */
	public static String toString(String[] sArr) {
		if (sArr == null || sArr.length == 0) {
			return "";
		}
		StringBuilder ret = new StringBuilder();
		for (String s : sArr) {
			ret.append(",").append(clearBlankSymbol(s));
		}
		return ret.substring(1);
	}

	/**
	 * [1,2,3] => "1,2,3"
 	 * @param source
	 * @return
	 */
	public static String toString(List<String> sList) {
		if (sList == null || sList.size() == 0) {
			return "";
		}
		StringBuilder ret = new StringBuilder();
		for (String s : sList) {
			ret.append(",").append(clearBlankSymbol(s));
		}
		return ret.substring(1);
	}

	/**
	 * "1,2,3" => [1,2,3]
 	 * @param source
	 * @return
	 */
	public static List<Long> getLongList(String source) {
		List<Long> ret = new ArrayList<>();
		if (source == null || source.equals("")) {
			return ret;
		}

		String[] sArr = source.split(",");
		for (String s : sArr) {
			s = clearBlankSymbol(s);
			if ("".equals(s)) {
				continue;
			}

			ret.add(Long.valueOf(s));
		}
		return ret;
	}

	/**
	 * "1,2,3" => [1,2,3]
 	 * @param source
	 * @return
	 */
	public static List<Integer> getIntegerList(String source) {
		List<Integer> ret = new ArrayList<>();
		if (source == null || source.equals("")) {
			return ret;
		}

		String[] sArr = source.split(",");
		for (String s : sArr) {
			s = clearBlankSymbol(s);
			if ("".equals(s)) {
				continue;
			}

			ret.add(Integer.valueOf(s));
		}
		return ret;
	}

	/**
	 * e.g. "1,2,3" => "'1','2','3'"
	 * @param source
	 * @return
	 */
	public static String convertSqlIds(String source) {
		if (source == null || source.equals("")) {
			return "";
		}
		StringBuilder ret = new StringBuilder();

		String[] sArr = source.split(",");
		for (String s : sArr) {
			s = clearBlankSymbol(s);
			if ("".equals(s)) {
				continue;
			}

			ret.append(",'").append(s).append("'");
		}

		return ret.substring(1);
	}

	/**
	 * e.g. [1,2,3] => "'1','2','3'"
	 * @param source
	 * @return
	 */
	public static String convertSqlIds(List<? extends Object> list) {
		if (ObjectUtil.isEmpty(list)) {
			return "";
		}
		StringBuilder ret = new StringBuilder();

		for (Object s : list) {
			if (s instanceof String) {
				s = ((String) s).trim();
				if ("".equals(s)) {
					continue;
				}
			}

			ret.append(",'").append(s).append("'");
		}

		return ret.substring(1);
	}

	/**
	 * e.g.
	 * <p>
	 * length=5 , return "?,?,?,?,?"
	 * <p>
	 * length=2 , return "?,?"
	 * 
	 * @param length
	 * @return
	 */
	public static String getPlaceholders(int length) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < length; i++) {
			ret.append(",?");
		}
		return ret.substring(1);
	}
	
	/**
	 * 去掉字符中空白符号
	 * @param s
	 * @return
	 */
	public static String clearBlankSymbol(String s) {
		return s.replaceAll("\\s+", "");
	}
	
	/**
	 * <b>用sql语句拼接</b>
	 * <P>为将  date类型转  '2017-11-20'的sql片段
	 * @param field 字段名
	 * @return
	 */
	public static String dateFormatSql(String field) {
		return "CONVERT(nchar(10), "+field+", 23)";
	}
	

}
