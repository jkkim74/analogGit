package com.skplanet.bisportal.common.consts;

import java.math.BigDecimal;

/**
 * Constants 상수 클래스.
 *
 * @author HO-JIN, HA (mimul@wiseeco.com)
 */
public final class Constants {
	public static final String SPRING_BATCH_COMPLETED = "[COMPLETED]";
	public static final String SUCCESS = "success";
	public static final String DATA_NOT_FOUND = "data not found";
	public static final String FAIL = "fail";
	public static final String RUN_SCRIPT = "/data/batch/shell/bpm/run.sh";
	public static final String YES = "Y";
	public static final String NO = "N";
	public static final String DELETED = "D";
	public static final String CODE = "code";
	public static final String MESSAGE = "message";
	public static final String STATUS = "status";

	public static final String DATE_24TIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DATE_YMDH_FORMAT = "yyyyMMddHH";
	public static final String DATE_YMD_FORMAT = "yyyyMMdd";
	public static final String DATE_YM_FORMAT = "yyyyMM";
	public static final String DATE_Y_FORMAT = "yyyy";
	public static final String DATE_YMD_DASH_FORMAT = "yyyy-MM-dd";
	public static final String DATE_YMD_COMMA_FORMAT = "yyyy.MM.dd";
	public static final String DATE_YM_COMMA_FORMAT = "yyyy.MM";
	public static final String DATE_MD_COMMA_FORMAT = "MM.dd";
	public static final String DATE_M_COMMA_FORMAT = "MM";

	/* 암호화 키 */
	public static final String AES_KEY = "9c91204af6be527d2a0cd3599eba4176";
	public static final String AES_IV = "1aa0c49d92cd06aa2d6916a2582bdc58";

	/* 쿠키 관련 */
	public static final int COOKIE_EXPIRE = 24 * 60 * 60;// 만료일자
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;
	public static final String COOKIE_VOYAGER_MASTER = "voyager_master";
	public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_EVEN;

	/* 서비스 */
	public static final String SVC_HOPPIN = "hoppin";
	public static final String SVC_SK11 = "sk11";
	public static final String SVC_TMAP = "tmap";
	public static final String SVC_TSTORE = "tstore";
	public static final String SVC_SYRUP = "syrup";
	public static final String SVC_OCB = "ocb";
	public static final String SVC_BLE = "ble";
	public static final String SVC_TCLOUD = "tcloud";

	public static final String PLANET_SPACE_DSS_CONTAINER_NAME = "dss";
	public static final String mailImageUrl = "https://voyager.skplanet.com/resources/images/mail/";

	public static final String BRAND_URL = "brandUrl";
	public static final String DIMENSION_URL = "dimensionUrl";
	public static final String BOSS_TEXT = "경영실적";

	public static final String HOST_WAS01 = "NBISc-cmwas01";
	public static final String HOST_WAS02 = "NBISc-cmwas02";
	public static final String HOST_DEV_WAS = "NBISc-cmwas-pdev01";

	public static final int NUMBER_ZERO = 0;
	public static final int NUMBER_ONE = 1;
	public static final int NUMBER_TWO = 2;
	public static final int NUMBER_THREE = 3;

	/* 문서 보안 관련 */
	public static final String SCSL_PROPERTY = "/data/softcamp/02_Module/02_ServiceLinker/softcamp.properties";
	public static final String SCSL_ENC = "/data/softcamp/04_KeyFile/keyDAC_SVR0.sc";

	public static final String ON = "on";
	public static final String OFF = "off";

	/* MicroStrategy 관련 */
	public static final String EXCLUDE_PROJECT = "3E20AD2811E56C1988D00080EF45925A";
	public static final String WEEK_TYPE_1 = "WEEK1";
	public static final String WEEK_TYPE_2 = "WEEK2";
	public static final String WEEK_TYPE_3 = "WEEK3";

	/* 날짜주기 */
	public static final String DATE_DAY = "day";
	public static final String DATE_WEEK = "week";
	public static final String DATE_MONTH = "month";
}
