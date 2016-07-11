package com.skplanet.bisportal.common.utils;

import ch.lambdaj.function.matcher.Predicate;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.ssh.BatchCommand;
import com.skplanet.bisportal.common.ssh.Command;
import com.skplanet.bisportal.model.bip.OrgUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ch.lambdaj.collection.LambdaCollections.with;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Map.Entry;

/**
 * The Utils class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
public class Utils {
	public static String getCreateDate() throws Exception {
		Locale currentLocale = LocaleContextHolder.getLocale();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_24TIME_FORMAT, currentLocale);
		return sdf.format(new Date());
	}

	/**
	 * 증감치 계산.
	 * 
	 * @param query
	 *            URL Query 값.
	 * @return Map<String, Object> 키별로 파싱된 Map 출력.
	 */
	public static Map<String, Object> getQueryParam(String query) throws Exception {
		Map<String, Object> params = Maps.newHashMap();
		try {
			for (String param : query.split("&")) {
				String[] pair = param.split("=");
				String key = URLDecoder.decode(pair[0], CharEncoding.UTF_8);
				String value = StringUtils.EMPTY;
				if (pair.length > 1) {
					value = URLDecoder.decode(pair[1], CharEncoding.UTF_8);
				}
				params.put(key, value);
			}
		} catch (Exception e) {
			log.error("getQueryParam()", e);
			params = null;
		}
		return params;
	}

	/**
	 * 증감치 계산.
	 * 
	 * @param request
	 *            HttpServletRequest.
	 * @return String 사용자의 브라우저 속성값 추출.
	 */
	public static String getUserAgent(HttpServletRequest request) throws Exception {
		return request.getHeader(Constants.HEADER_USER_AGENT);
	}

	/**
	 * Batch Job 실행.
	 * 
	 * @param job
	 *            Job 이름
	 * @param filename
	 *            파일이름
	 * @return success or fail
	 */
	public static String runBatch(String job, String filename) throws Exception {
		Map<String, String> cmd = Maps.newHashMap();
		cmd.put("job", job);
		cmd.put("filename", filename);
		Command batch = new BatchCommand();
		return batch.execute(cmd);
	}

	/**
	 * Map 의 value 들을 List로 변환한다.
	 * 
	 * @param map
	 *            map
	 * @param <K>
	 *            key generic type of map
	 * @param <V>
	 *            value generic type of map
	 * @return converted list
	 */
	public static <K, V> List<V> mapToList(Map<K, V> map) throws Exception {
		return newArrayList(transform(map.entrySet(), new Function<Entry<K, V>, V>() {
			@Override public V apply(Entry<K, V> input) {
				return input.getValue();
			}
		}));
	}

	public static Map<String, String> getMailEwmaImage(BigDecimal basicValue, BigDecimal lclValue, BigDecimal uclValue,
			String increaseValue) throws Exception {
		String imageName;
		Map<String, String> ewmaImageUrl = Maps.newHashMap();
		if (NumberUtil.isEmpty(basicValue) || NumberUtil.isEmpty(lclValue) || NumberUtil.isEmpty(uclValue)) {
			ewmaImageUrl.put("brandUrl", getInRangeUrl(increaseValue));
			ewmaImageUrl.put("dimensionUrl", StringUtils.EMPTY);
		} else {
			if (NumberUtil.inRange(basicValue, lclValue, uclValue)) {
				ewmaImageUrl.put("brandUrl", getInRangeUrl(increaseValue));
				ewmaImageUrl.put("dimensionUrl", StringUtils.EMPTY);
			} else {
				imageName = getOutRangeUrl(increaseValue);
				ewmaImageUrl.put("brandUrl", imageName);
				ewmaImageUrl.put("dimensionUrl", "<img src='" + Constants.mailImageUrl + imageName + "' />");
			}
		}
		return ewmaImageUrl;
	}

	public static String getInRangeUrl(String increaseValue) throws Exception {
		String imageName;
		if ('+' == getFirstChar(increaseValue)) {
			imageName = "check_img01.jpg";
		} else if ('-' == getFirstChar(increaseValue)) {
			imageName = "check_img05.jpg";
		} else {
			imageName = "check_img04.jpg";
		}
		return imageName;
	}

	public static String getOutRangeUrl(String increaseValue) throws Exception {
		String imageName;
		if ('+' == getFirstChar(increaseValue)) {
			imageName = "check_img03.jpg";
		} else {
			imageName = "check_img02.jpg";
		}
		return imageName;
	}

	public static char getFirstChar(String increaseValue) throws Exception {
		if (StringUtils.isEmpty(increaseValue))
			return '*';
		else
			return increaseValue.charAt(0);
	}

	public static String getIncreaseFontColor(String increaseValue) throws Exception {
		String font;
		if ('+' == getFirstChar(increaseValue)) {
			font = "#59c1c2";
		} else if ('-' == getFirstChar(increaseValue)) {
			font = "#fc7648";
		} else {
			font = "#808080";
		}
		return font;
	}

	public static String getHostName() {
		String hostName;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			log.error("getHostName {}", e);
			hostName = null;
		}
		return hostName;
	}

	public static String[] getReceivePhones(List<OrgUser> orgUsers) {
		if (CollectionUtils.isEmpty(orgUsers)) {
			return null;
		}
		List<OrgUser> mblOrgUsers = with(orgUsers).clone().retain(new Predicate<OrgUser>() {
			@Override
			public boolean apply(OrgUser orgUser) {
				return StringUtils.isNotEmpty(orgUser.getMblPhonNum());
			}
		});

		if (CollectionUtils.isEmpty(mblOrgUsers)) {
			return null;
		}

		String[] receivePhones = new String[mblOrgUsers.size()];
		int i = 0;
		for (OrgUser orgUser : mblOrgUsers) {
			receivePhones[i++] = StringUtils.replace(orgUser.getMblPhonNum(), "-", StringUtils.EMPTY);
		}
		return receivePhones;
	}
}
