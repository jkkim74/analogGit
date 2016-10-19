package com.skplanet.ocb.util;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.io.Resources;
import com.skplanet.ocb.security.UserInfo;

import kr.co.skplanet.crypto.EncryptCustomerInfo;

public final class Helper {

	private static final String EMREJECT_PW = "9gPXBD95qbDedk5PaRLE";

	private static DateFormatter dfAsDate = new DateFormatter("yyyyMMdd");
	private static DateFormatter dfAsDatetime = new DateFormatter("yyyyMMddHHmmss");
	private static DateFormatter dfAsMonthDay = new DateFormatter("MMdd");

	public static String nowDateString() {
		return dfAsDate.print(new Date(), Locale.getDefault());
	}

	public static String nowDateTimeString() {
		return dfAsDatetime.print(new Date(), Locale.getDefault());
	}

	public static String nowMonthDayString() {
		return dfAsMonthDay.print(new Date(), Locale.getDefault());
	}

	public static String yesterdayDateString() {
		// now - 1day(24*60*60*1000)
		Date yesterday = new Date(System.currentTimeMillis() - 86400000);
		return dfAsDate.print(yesterday, Locale.getDefault());
	}

	public static String toDateString(Date date) {
		return dfAsDate.print(date, Locale.getDefault());
	}

	public static String toDatetimeString(Date date) {
		return dfAsDatetime.print(date, Locale.getDefault());
	}

	public static String uniqueCsvFilename(String prefix) {
		if (prefix != null && prefix.trim().length() > 0) {
			return prefix + '_' + nowDateString() + '_' + UUID.randomUUID() + ".csv";
		}
		return nowDateString() + '_' + UUID.randomUUID() + ".csv";
	}

	public static String skpEncrypt(String plainText) {
		String emrejectKey = Resources.getResource("config/emreject.key").getPath();

		try {
			String[] values = EncryptCustomerInfo.getValues(emrejectKey, EMREJECT_PW);

			String key = values[0];
			String iv = values[1];

			return EncryptCustomerInfo.encrypt(plainText.getBytes("UTF-8"), key, iv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static UserInfo currentUser() {
		return (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
