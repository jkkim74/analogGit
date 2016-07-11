package com.skplanet.bisportal.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

/**
 * The NumberUtil class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public class NumberUtil {

	/**
	 * 숫자 포멧팅
	 *
	 * @param number
	 *            변형할 숫자
	 * @param point
	 *            선택할 포멧 지정
	 * @return String 변형된 포멧으로 출력.
	 */
	public static String formatNumberByPoint(double number, int point) throws Exception {
		String format;

		switch (point) {
		case 0:
			format = "###,###,###.###";
			break;
		case 1:
			format = "###,###,###,##0.0";
			break;
		case 2:
			format = "###,###,###,##0.00";
			break;
		case 3:
			format = "###,###,###,##0.000";
			break;
		case 4:
			format = "###,###,###,##0.0000";
			break;
		case 5:
			format = "###,###,###,##0.00000";
			break;
		default:
			format = "###,###,###.###";
			break;
		}
		DecimalFormat df = new DecimalFormat(format);
		return String.valueOf(df.format(number));
	}

	/**
	 * 증감치 계산.
	 *
	 * @param basicValue
	 *            기준값
	 * @param measureValue
	 *            목표값
	 * @return String 기준값대비 목표값이 얼마나 증감했는지 출력.
	 */
	public static String calculateGrowth(Double basicValue, Double measureValue) throws Exception {
		Double calculateValue = ((basicValue - measureValue) / measureValue) * 100;
		if (calculateValue > 0d) {
			return "+" + NumberUtil.formatNumberByPoint(calculateValue, 1) + "%";
		} else if (calculateValue == 0d) {
			return "=" + NumberUtil.formatNumberByPoint(calculateValue, 1) + "%";
		} else {
			return NumberUtil.formatNumberByPoint(calculateValue, 1) + "%";
		}
	}

	public static boolean isEmpty(BigDecimal value) throws Exception {
		if (value == null || value.intValue() == 0)
			return true;
		return false;
	}

	public static boolean isNull(BigDecimal value) throws Exception {
		if (value == null)
			return true;
		return false;
	}

	public static boolean inRange(BigDecimal basicValue, BigDecimal startValue, BigDecimal endValue) throws Exception {
		if (startValue.compareTo(basicValue) <= 0 && endValue.compareTo(basicValue) >= 0)
			return true;
		return false;
	}

	/**
	 * 숫자인지 체크
	 *
	 * @param str
	 *            숫자 문자
	 * @return boolean 숫지여부.
	 */
	public static boolean isNumber(String str) {
		if (StringUtils.isEmpty(str))
			return false;

		if (str.matches("^[-+]?\\d+(\\.\\d+)?$")) {
			return true;
		} else {
			// try parse double
			try {
				Double.parseDouble(str);
				return true;
			} catch (NumberFormatException de) {
				// try BigDecimal
				try {
					new BigDecimal(str);
					return true;
				} catch (NumberFormatException be) {
					return false;
				}
			}
		}
	}

	public static boolean isDigit(String number) {
		if (StringUtils.isEmpty(number)) {
			return false;
		}
		char chars[] = number.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (!Character.isDigit(chars[i])) {
				return false;
			}
		}
		return true;
	}
}
