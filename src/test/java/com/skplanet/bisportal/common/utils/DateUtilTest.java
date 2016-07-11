package com.skplanet.bisportal.common.utils;

import com.skplanet.bisportal.common.consts.Constants;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.skplanet.bisportal.common.utils.DateUtil.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

public class DateUtilTest {
	@Test
	public void addDaysTest() throws Exception {
		assertThat(DateUtil.addDays("20140717", -7), is("20140710")); // 1주전
		assertThat(DateUtil.addDays("20140704", -7), is("20140627")); // 8일간
		System.out.println(DateUtil.addDays("20150401", -30));
	}

	@Test
	public void addMonthsTest() throws Exception {
		assertThat(DateUtil.addMonths("20140717", -1), is("20140617")); // 1달전
	}

	@Test
	public void addHoursTest() throws Exception {
		assertThat(DateUtil.addHours("2014071723", -1), is("2014071722")); // 1시간 전
		System.out.println(DateUtil.addHours("2014071700", -1));
		System.out.println(DateUtil.getCurrentDate("yyyyMMddHH").substring(8));
		System.out.println(DateUtil.addHours("2014071711", -24));
	}

	@Test
	public void changeFormatDateTest() throws Exception {
		assertThat(DateUtil.changeFormatDate("20140717", "yyyyMMdd", "yyyy.MM.dd"), is("2014.07.17"));
		assertThat(DateUtil.changeFormatDate("20140717", "yyyyMMdd", "yyyyMM"), is("201407"));
		assertThat(DateUtil.changeFormatDate("20140817", "yyyyMMdd", "MM.dd"), is("08.17"));
		assertThat(DateUtil.changeFormatDate("20140817", "yyyyMMdd", "MM"), is("08"));
		System.out.println("2014071712".substring(0, 8));
		LocalDate date = new LocalDate();
		System.out.println(date.dayOfWeek().getAsText());
	}

	@Test
	public void addYearsTest() throws Exception {
		assertThat(DateUtil.addYears("20140717", -1), is("20130717")); // 1년전
	}

	@Test
	public void testIsYesterday() {
		assertTrue(isYesterday("20131201", "20131130"));
		assertTrue(isYesterday("20140703", "20140702"));
		assertTrue(isYesterday("20140702", "20140701"));
		assertTrue(isYesterday("20140701", "20140630"));

		assertFalse(isYesterday("20140701", "20140631"));
	}

	@Test
	public void testIsOneWeekAgo() {
		assertTrue(isOneWeekAgo("20131201", "20131124"));
		assertTrue(isOneWeekAgo("20140703", "20140626"));
		assertTrue(isOneWeekAgo("20140702", "20140625"));
		assertTrue(isOneWeekAgo("20140701", "20140624"));
		assertTrue(isOneWeekAgo("20130101", "20121225"));
	}

	@Test
	public void testIsOneMonthAgo() {
		assertTrue(isOneMonthAgo("20131201", "20131101"));
		assertTrue(isOneMonthAgo("20140703", "20140603"));
		assertTrue(isOneMonthAgo("20140228", "20140128"));
		assertTrue(isOneMonthAgo("20090101", "20081201"));
		assertTrue(isOneMonthAgo("19991225", "19991125"));
	}

	@Test
	public void testIsOneYearAgo() {
		assertTrue(isOneYearAgo("20131201", "20121201"));
		assertTrue(isOneYearAgo("20140101", "20130101"));
		assertTrue(isOneYearAgo("20100101", "20090101"));
		assertTrue(isOneYearAgo("20090101", "20080101"));
		assertTrue(isOneYearAgo("20001225", "19991225"));
	}

	@Test
	public void testIsDayInOneWeek() {

		// date2가 assertDate을 기준으로 8일주기에 속해야 한다.
		String assertDate = "20131201";

		assertTrue(isBeforeWeek(assertDate, "20131130", 1)); // 1주일전을 구하면 8일 주기에 속해짐.
		assertTrue(isBeforeWeek(assertDate, "20131129", 1));
		assertTrue(isBeforeWeek(assertDate, "20131128", 1));
		assertTrue(isBeforeWeek(assertDate, "20131127", 1));
		assertTrue(isBeforeWeek(assertDate, "20131126", 1));
		assertTrue(isBeforeWeek(assertDate, "20131125", 1));
		assertTrue(isBeforeWeek(assertDate, "20131124", 1));

		assertFalse(isBeforeWeek(assertDate, "20131123", 1));
	}

	@Test
	public void testIsDayInFourteenWeek() {

		// date2가 assertDate을 기준으로 14주일 주기에 속해야 한다.
		String assertDate = "20131223";

		assertTrue(isBeforeWeek(assertDate, "20131223", 14));
		assertTrue(isBeforeWeek(assertDate, "20131216", 14));
		assertTrue(isBeforeWeek(assertDate, "20131209", 14));
		assertTrue(isBeforeWeek(assertDate, "20131202", 14));
		assertTrue(isBeforeWeek(assertDate, "20131125", 14));
		assertTrue(isBeforeWeek(assertDate, "20131118", 14));
		assertTrue(isBeforeWeek(assertDate, "20131111", 14));
		assertTrue(isBeforeWeek(assertDate, "20131104", 14));
		assertTrue(isBeforeWeek(assertDate, "20131028", 14));
		assertTrue(isBeforeWeek(assertDate, "20131021", 14));
		assertTrue(isBeforeWeek(assertDate, "20131014", 14));
		assertTrue(isBeforeWeek(assertDate, "20131007", 14));
		assertTrue(isBeforeWeek(assertDate, "20131001", 14));
		assertTrue(isBeforeWeek(assertDate, "20130916", 14));

		assertFalse(isBeforeWeek(assertDate, "20130909", 14));
	}

	@Test
	public void testIsBeforeThirteenMonth() {
		// date2가 assertDate을 기준으로 13개월 주기에 속해야 한다.
		String assertDate = "201312";

		assertTrue(isBeforeMonth(assertDate, "201312", 13));
		assertTrue(isBeforeMonth(assertDate, "201311", 13));
		assertTrue(isBeforeMonth(assertDate, "201310", 13));
		assertTrue(isBeforeMonth(assertDate, "201309", 13));
		assertTrue(isBeforeMonth(assertDate, "201308", 13));
		assertTrue(isBeforeMonth(assertDate, "201307", 13));
		assertTrue(isBeforeMonth(assertDate, "201306", 13));
		assertTrue(isBeforeMonth(assertDate, "201305", 13));
		assertTrue(isBeforeMonth(assertDate, "201304", 13));
		assertTrue(isBeforeMonth(assertDate, "201303", 13));
		assertTrue(isBeforeMonth(assertDate, "201302", 13));
		assertTrue(isBeforeMonth(assertDate, "201301", 13));
		assertTrue(isBeforeMonth(assertDate, "201212", 13));

		assertFalse(isBeforeMonth(assertDate, "201211", 13));
	}

	@Test
	public void testGetDays() {
		String test1Date = DateUtil.getCurrentDate();
		String test2Date = DateUtil.getCurrentDate(Constants.DATE_YMD_FORMAT);
		assertThat(test1Date, is(test2Date));
		System.out.println("test1:" + test1Date);
		System.out.println("test2:" + test2Date);
		assertThat(DateUtil.getDays("20150205", "20150208", "yyyyMMdd"), is(3));
	}

	@Test
	public void testIsValidDate() {
		assertFalse(DateUtil.isValidDate("20150229", "yyyyMMdd"));
		assertFalse(DateUtil.isValidDate("20150228", "yyyyMM"));
		assertTrue(DateUtil.isValidDate("2015012", "yyyyMM"));
		assertTrue(DateUtil.isValidDate("20151228", "yyyyMMdd"));
	}
}
