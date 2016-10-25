package com.skplanet.ocb.util;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.io.Resources;
import com.skplanet.ocb.security.UserInfo;

import kr.co.skplanet.crypto.EncryptCustomerInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Helper {

	private static final String EMREJECT_PW = "9gPXBD95qbDedk5PaRLE";

	private static DateFormatter dfAsISODate = new DateFormatter("yyyy-MM-dd");
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

	public static String nowISODateString() {
		return dfAsISODate.print(new Date(), Locale.getDefault());
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

	public static String currentClientIp() {
		HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		// 프록시에서 remote ip address를 얻기위한 가능한 방법들.
		log.debug("X-Forwarded-For: {}", req.getHeader("X-Forwarded-For"));
		log.debug("Proxy-Client-IP: {}", req.getHeader("Proxy-Client-IP"));
		log.debug("WL-Proxy-Client-IP: {}", req.getHeader("WL-Proxy-Client-IP"));
		log.debug("HTTP_CLIENT_IP: {}", req.getHeader("HTTP_CLIENT_IP"));
		log.debug("HTTP_X_FORWARDED_FOR: {}", req.getHeader("HTTP_X_FORWARDED_FOR"));
		log.debug("getRemoteAddr(): {}", req.getRemoteAddr());

		return req.getRemoteAddr();
	}

}
