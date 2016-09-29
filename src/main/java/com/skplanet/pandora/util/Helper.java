package com.skplanet.pandora.util;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.springframework.format.datetime.DateFormatter;

import com.google.common.io.Resources;

import kr.co.skplanet.crypto.EncryptCustomerInfo;

public final class Helper {

	public static String nowDateString() {
		DateFormatter df = new DateFormatter("yyyyMMdd");
		return df.print(new Date(), Locale.getDefault());
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
			String[] values = EncryptCustomerInfo.getValues(emrejectKey, Constant.EMREJECT_PW);

			String key = values[0];
			String iv = values[1];

			return EncryptCustomerInfo.encrypt(plainText.getBytes("UTF-8"), key, iv);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
