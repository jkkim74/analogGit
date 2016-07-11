package com.skplanet.bisportal.common.utils;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The DateUtil class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
@Slf4j
public class DateUtil {
	/**
	 * 숫자 포멧팅
	 * 
	 * @param fromDate
	 *            변경전 날짜
	 * @param fromFormat
	 *            변경전 날짜 포멧
	 * @param toFormat
	 *            변경할 날짜 포멧
	 * @return String 변경된 포멧으로 출력.
	 */
	public static String changeFormatDate(String fromDate, String fromFormat, String toFormat) {
		DateTimeFormatter fromFmt = DateTimeFormat.forPattern(fromFormat);
		DateTimeFormatter toFmt = DateTimeFormat.forPattern(toFormat);
		LocalDate localDate = fromFmt.parseLocalDate(fromDate);
		return toFmt.print(localDate);
	}

	public static String dayOfWeek(String date, String format) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(format);
		LocalDate localDate = fmt.parseLocalDate(date);
		return localDate.dayOfWeek().getAsText();
	}

	/**
	 * ISODate 문자열을 원하는 형태의 날짜 문자열로 변환
	 * 
	 * @param isoDate
	 *            ISO8601 형태의 날짜 문자열
	 * @param toFormat
	 *            변경하고자 하는 날짜 포맷
	 * @return 원하는 포맷의 날짜 문자열
	 */
	public static String convertFormatFromISODate(String isoDate, String toFormat) {
		DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(isoDate);
		DateTimeFormatter toFmt = DateTimeFormat.forPattern(toFormat);
		return toFmt.print(dt);
	}

	/**
	 * yyyyMMdd 현재시간 출력.
	 *
	 * @param
	 * @return 시간 패턴형태로(yyyyMMdd) 현재 시간 출력.
	 */
	public static String getCurrentDate() {
		return getCurrentDate(Constants.DATE_YMD_FORMAT);
	}

	/**
	 * 포멧별 현재시간 출력.
	 * 
	 * @param pattern
	 *            시간 패턴.
	 * @return 시간 패턴형태로 현재 시간 출력.
	 */
	public static String getCurrentDate(String pattern) {
		DateTime dt = new DateTime();
		DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
		return fmt.print(dt);
	}

	/**
	 * 기준일자를 월을 증감시킨 결과값 출력.
	 * 
	 * @param date
	 *            기준일자.
	 * @param months
	 *            증감월 숫자.
	 * @return String 계산된 시간.
	 */
	public static String addMonths(String date, int months) {
		if (months == 0) {
			return date;
		}
		DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_YMD_FORMAT);
		DateTime dt = fmt.parseDateTime(date);
		DateTime subtracted = dt.withFieldAdded(DurationFieldType.months(), months);
		return fmt.print(subtracted);
	}

	/**
	 * return day of the week of the input data. return in abbreviation pattern
	 * for the default language of the current system.
	 *
	 * <pre>
	 * DateUtil.getDayOfWeek(&quot;2011-02-04&quot;) = &quot;Fri&quot;;
	 * </pre>
	 *
	 * @param date date(yyyyMMdd)
	 * @return String day of week (shortText, Default Locale)
	 */
	public static String getDayOfWeek(String date) {
		return getDayOfWeek(date, true, LocaleContextHolder.getLocale());
	}

	/**
	 * return day of the week of the input data. return in abbreviation or full
	 * day of the week pattern for the language after getting locale info.
	 *
	 * <pre>
	 * DateUtil.getDayOfWeek(&quot;2011-02-04&quot;, true, Locale.US) = &quot;Fri&quot;;
	 * DateUtil.getDayOfWeek(&quot;2011-02-04&quot;, false, Locale.US) = &quot;Friday&quot;;
	 * </pre>
	 *
	 * @param date  date(yyyy-MM-dd)
	 * @param abbreviation if <code>true</code>, return in abbreviation.
	 * @param locale locale
	 * @return String day of week
	 */
	public static String getDayOfWeek(String date, boolean abbreviation,
			Locale locale) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_YMD_FORMAT);
		DateTime dt = fmt.parseDateTime(date);
		DateTime.Property dayOfWeek = dt.dayOfWeek();

		if (abbreviation)
			return dayOfWeek.getAsShortText(locale);
		else
			return dayOfWeek.getAsText(locale);
	}

	/**
	 * 기준일자를 년을 증감시킨 결과값 출력.
	 * 
	 * @param date
	 *            기준일자.
	 * @param years
	 *            증감 년수.
	 * @return String 계산된 시간.
	 */
	public static String addYears(String date, int years) {
		if (years == 0) {
			return date;
		}
		DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_YMD_FORMAT);
		DateTime dt = fmt.parseDateTime(date);
		DateTime subtracted = dt.withFieldAdded(DurationFieldType.years(), years);
		return fmt.print(subtracted);
	}

	/**
	 * 기준일자를 일을 증감시킨 결과값 출력.
	 * 
	 * @param date
	 *            기준일자.
	 * @param days
	 *            증감 일수.
	 * @return String 계산된 시간.
	 */
	public static String addDays(String date, int days) {
		if (days == 0) {
			return date;
		}
		DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_YMD_FORMAT);
		DateTime dt = fmt.parseDateTime(date);
		DateTime subtracted = dt.withFieldAdded(DurationFieldType.days(), days);
		return fmt.print(subtracted);
	}

	/**
	 * 기준일자를 시간을 증감시킨 결과값 출력.
	 *
	 * @param date
	 *            기준일자.
	 * @param hours
	 *            증감 시간.
	 * @return String 계산된 시간.
	 */
	public static String addHours(String date, int hours) {
		if (hours == 0) {
			return date;
		}
		DateTimeFormatter fmt = DateTimeFormat.forPattern(Constants.DATE_YMDH_FORMAT);
		DateTime dt = fmt.parseDateTime(date);
		DateTime subtracted = dt.withFieldAdded(DurationFieldType.hours(), hours);
		return fmt.print(subtracted);
	}

	/**
	 * date1와 date2의 날짜가 diffDay 만큼 차이나는 지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @param beforeDiffDay
	 * @return
	 */
	public static boolean isSameDay(String date1, String date2, int beforeDiffDay) {
		DateFormat sdf = new SimpleDateFormat(Constants.DATE_YMD_FORMAT);
		try {
			Date originalDate = sdf.parse(date1);
			Date targetDate = sdf.parse(date2);
			Date beforeDate = new Date(originalDate.getTime() - (beforeDiffDay * Constants.DAY_IN_MS));
			return DateUtils.isSameDay(targetDate, beforeDate);
		} catch (ParseException e) {
			log.error("isSameDay {}", e);
			return false;
		}
	}

	/**
	 * date1와 date2가 어제 날짜 인지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isYesterday(String date1, String date2) {
		return isSameDay(date1, date2, 1);
	}

	/**
	 * date1와 date2가 1주일 전인지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isOneWeekAgo(String date1, String date2) {
		return isSameDay(date1, date2, 7);
	}

	/**
	 * date1와 date2의 날짜가 diffMonth 만큼 차이나는 지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @param diffMonth
	 * @return
	 */
	public static boolean isBeforeMonth(String date1, String date2, int diffMonth) {
		DateFormat sdf = new SimpleDateFormat(Constants.DATE_YM_FORMAT);
		try {
			Date originalDate = sdf.parse(date1);
			Date targetDate = sdf.parse(date2);

			Calendar beforeCal = Calendar.getInstance();
			beforeCal.setTime(originalDate);
			beforeCal.add(Calendar.MONTH, -diffMonth);
			return beforeCal.getTime().before(targetDate);
		} catch (ParseException e) {
			log.error("isBeforeMonth {}", e);
			return false;
		}
	}

	/**
	 * date1와 date2가 1달 전인지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isOneMonthAgo(String date1, String date2) {
		DateFormat sdf = new SimpleDateFormat(Constants.DATE_YMD_FORMAT);
		try {
			Date originalDate = sdf.parse(date1);
			Date targetDate = sdf.parse(date2);
			Calendar beforeCal = Calendar.getInstance();
			beforeCal.setTime(originalDate);
			beforeCal.add(Calendar.MONTH, -1);
			return DateUtils.isSameDay(targetDate, beforeCal.getTime());
		} catch (ParseException e) {
			log.error("isOneMonthAgo {}", e);
			return false;
		}
	}

	/**
	 * date1와 date2가 1년 전인지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isOneYearAgo(String date1, String date2) {
		DateFormat sdf = new SimpleDateFormat(Constants.DATE_YMD_FORMAT);
		try {
			Date originalDate = sdf.parse(date1);
			Date targetDate = sdf.parse(date2);

			Calendar beforeCal = Calendar.getInstance();
			beforeCal.setTime(originalDate);
			beforeCal.add(Calendar.YEAR, -1);
			return DateUtils.isSameDay(targetDate, beforeCal.getTime());
		} catch (ParseException e) {
			log.error("isOneYearAgo {}", e);
			return false;
		}
	}

	/**
	 * date1와 date2의 날짜가 diffWeek 만큼 차이나는 지 확인한다.
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isBeforeWeek(String date1, String date2, int diffWeek) {
		DateFormat sdf = new SimpleDateFormat(Constants.DATE_YMD_FORMAT);
		try {
			Date originalDate = sdf.parse(date1);
			Date targetDate = sdf.parse(date2);
			Date weekDate = new Date(originalDate.getTime() - (7l * diffWeek * Constants.DAY_IN_MS + 1l));
			return weekDate.before(targetDate);
		} catch (ParseException e) {
			log.error("isBeforeWeek {}", e);
			return false;
		}
	}

	/**
	 * 현재날짜의 yyyyMMdd를 구한다.
	 * 
	 * @return
	 */
	public static String getCurrentYmd() {
		return getCurrentDate(Constants.DATE_YMD_FORMAT);
	}

	/**
	 * basicDate가 현재 yyyyMM와 같은지 비교한다.
	 * 
	 * @param basicDate
	 * @return
	 */
	public static boolean isCurrentYm(String basicDate) {
		return StringUtils.equals(getCurrentDate(Constants.DATE_YM_FORMAT), basicDate);
	}

	/**
	 * day 인지 검증.
	 * 
	 * @param dateType
	 * @return
	 */
	public static boolean isDay(ReportDateType dateType) {
		return dateType == ReportDateType.DAY;
	}

	/**
	 * week 인지 검증
	 * 
	 * @param dateType
	 * @return
	 */
	public static boolean isWeek(ReportDateType dateType) {
		return dateType == ReportDateType.WEEK;
	}

	/**
	 * sum 인지 검증
	 *
	 * @param dateType
	 * @return
	 */
	public static boolean isSum(ReportDateType dateType) {
		return dateType == ReportDateType.SUM;
	}

	/**
	 * month 인지 검증
	 * 
	 * @param dateType
	 * @return
	 */
	public static boolean isMonth(ReportDateType dateType) {
		return dateType == ReportDateType.MONTH;
	}

	public static boolean isBetweenDate(String basicDate, String startDate, String endDate) {
		int basic = Integer.parseInt(basicDate);
		return (basic >= Integer.parseInt(startDate) && basic <= Integer.parseInt(endDate));
	}

	public static JqGridRequest getWkFrto(JqGridRequest jqGRidRequest, List<SmwCldrWk> list) {
		String stYear = jqGRidRequest.getStartDate().substring(0, 4);
		String edYear = jqGRidRequest.getEndDate().substring(0, 4);
		int yearInter = Integer.parseInt(stYear) - Integer.parseInt(edYear);
		int prevYear = Integer.parseInt(edYear) - 1;
		String strprevYear = Integer.toString(prevYear);
		if (yearInter == -1) {
			jqGRidRequest.setStrdStYear(list.get(0).getStrdYear());
			jqGRidRequest.setStrdWkFrStSeq(list.get(0).getStrdWkSeq());
			jqGRidRequest.setStrdWkFrEdSeq(list.get(1).getStrdWkSeq());
			jqGRidRequest.setStrdEdYear(list.get(2).getStrdYear());
			jqGRidRequest.setStrdWkToStSeq("1");
			jqGRidRequest.setStrdWkToEdSeq(list.get(2).getStrdWkSeq());
			jqGRidRequest.setStrdPrevEdYear(stYear);
		}
		if (yearInter < -1) {
			if (list.size() < 4) {
				jqGRidRequest.setStrdStYear(stYear);
				jqGRidRequest.setStrdWkFrStSeq("1");
				jqGRidRequest.setStrdWkFrEdSeq("100");
				jqGRidRequest.setStrdEdYear(edYear);
				jqGRidRequest.setStrdWkToStSeq("1");
				jqGRidRequest.setStrdWkToEdSeq(list.get(1).getStrdWkSeq());
			} else {
				jqGRidRequest.setStrdStYear(stYear);
				jqGRidRequest.setStrdWkFrStSeq(list.get(0).getStrdWkSeq());
				jqGRidRequest.setStrdWkFrEdSeq("100");
				jqGRidRequest.setStrdEdYear(edYear);
				jqGRidRequest.setStrdWkToStSeq("1");
				jqGRidRequest.setStrdWkToEdSeq(list.get(2).getStrdWkSeq());
			}
			jqGRidRequest.setStrdPrevEdYear(strprevYear);

		}
		if (yearInter == 0) {
			jqGRidRequest.setStrdStYear(list.get(0).getStrdYear());
			jqGRidRequest.setStrdWkFrStSeq(list.get(0).getStrdWkSeq());
			int strdWkFrEdSeq = Integer.parseInt(list.get(2).getStrdWkSeq()) - 1;
			jqGRidRequest.setStrdWkFrEdSeq(Integer.toString(strdWkFrEdSeq));
			jqGRidRequest.setStrdEdYear(list.get(2).getStrdYear());
			jqGRidRequest.setStrdWkToStSeq(list.get(2).getStrdWkSeq());
			jqGRidRequest.setStrdWkToEdSeq(list.get(2).getStrdWkSeq());
			jqGRidRequest.setStrdPrevEdYear(edYear);
		}
		return jqGRidRequest;
	}

	/**
	 * Calculate number of days between startDate and endDate
	 *
	 * @param startDate
	 *            start date
	 * @param endDate
	 *            end date
	 * @param datePattern
	 *            date pattern
	 * @return integer of days
	 */
	public static int getDays(String startDate, String endDate, String datePattern) {
		DateTimeFormatter fmt = DateTimeFormat.forPattern(datePattern);

		DateTime startDateTime = fmt.parseDateTime(startDate);
		DateTime endDateTime = fmt.parseDateTime(endDate);

		long startMillis = startDateTime.getMillis();
		long endMillis = endDateTime.getMillis();

		int startDay = (int) (startMillis / (60 * 60 * 1000 * 24));
		int endDay = (int) (endMillis / (60 * 60 * 1000 * 24));

		return endDay - startDay;
	}

	public static boolean isValidDate(String date, String datePattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
			sdf.setLenient(false);
			sdf.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 날짜비교함수( newStartDate > oldStartDate return true ) - cookatrice
	 *
	 * @param oldStartDate
	 * @param newStartDate
	 * @return
	 */
	public static boolean compareStartDate(String oldStartDate, String newStartDate) {
		boolean result = false;
		Date oldDate;
		Date newDate;
		try {
			if (StringUtils.isEmpty(oldStartDate) || StringUtils.isEmpty(newStartDate)) {
				return result;
			}
			SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_YMD_FORMAT);
			oldDate = format.parse(oldStartDate);
			newDate = format.parse(newStartDate);
			if (newDate.compareTo(oldDate) > 0) {
				result = true;
			}
		} catch (ParseException e) {
			log.error("ParseException {}", e);
		}
		return result;
	}
}
