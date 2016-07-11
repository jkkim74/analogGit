package com.skplanet.bisportal.common.acl;

import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.acl.BipUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public class CookieUtils {
	public static String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies;
		String cookieVal = null;

		try {
			cookies = request.getCookies();
			if (cookieName == null || cookies == null)
				return null;
			int cookieLength = cookies.length;
			for (int i = 0; i < cookieLength; i++) {
				if (StringUtils.equals(cookieName, cookies[i].getName())) {
					if (StringUtils.isEmpty(cookies[i].getValue()) ||
							"null".equals(cookies[i].getValue())) {
						return null;
					} else {
						cookieVal = URLDecoder.decode(cookies[i].getValue(), CharEncoding.UTF_8);
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("getCookie {}", e);
			throw new RuntimeException("Exception generated.", e);
		}

		return cookieVal;
	}

	public static void setCookie(HttpServletResponse response, String cookieName, String cookieVal, int age,
			String path, String domain) throws Exception {
		StringBuffer sbHeader;
		Date expireDate;
		DateFormat df;
		String expire;

		try {
			if (StringUtils.isEmpty(cookieName))
				return;

			sbHeader = new StringBuffer(256);
			if (!StringUtils.isEmpty(cookieVal))
				cookieVal = URLEncoder.encode(cookieVal, CharEncoding.UTF_8);
			else
				cookieVal = StringUtils.EMPTY;

			// 쿠키 예제 : "Set-Cookie",
			// "LIPM=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; domain=.skplanet.com;"

			sbHeader.append(cookieName).append("=").append(cookieVal).append("; ");

			if (age == 1)
				sbHeader.append("expires=Thu, 01 Jan 1970 00:00:00 GMT; ");
			else if (age > 0) {
				expireDate = new Date();
				expireDate.setTime(expireDate.getTime() + age * 1000);

				df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				expire = df.format(expireDate);

				sbHeader.append("expires=").append(expire).append(" UTC; ");
			}

			if (StringUtils.isNotEmpty(path))
				sbHeader.append("path=").append(path).append("; ");

			if (StringUtils.isNotEmpty(domain))
				sbHeader.append("domain=").append(domain).append("; ");

			response.addHeader("Set-Cookie", sbHeader.toString());
		} catch (Exception e) {
			log.error("setCookie {}", e);
			throw new RuntimeException("Exception generated.", e);
		}
	}

	public static void deleteCookie(HttpServletResponse response, String cookieName) {
		try {
			setCookie(response, cookieName, null, 0, "/", null);
		} catch (Exception e) {
			log.error("deleteCookie {}", e);
			throw new RuntimeException("Exception generated.", e);
		}
	}

	public static void deleteCookie(HttpServletResponse response, String cookieName, String domain) {
		try {
			setCookie(response, cookieName, null, 0, "/", domain);
		} catch (Exception e) {
			log.error("deleteCookie {}", e);
			throw new RuntimeException("Exception generated.", e);
		}
	}

	public static void setVoyagerCookie(HttpServletResponse response, BipUser user, boolean keepCookie) {
		try {
			StringBuffer cookie = new StringBuffer(128);
			cookie.append("id=").append(user.getId());
			cookie.append("&username=").append(user.getUsername());
			cookie.append("&fullname=").append(user.getFullname());
			cookie.append("&email=").append(user.getEmail());
			// cookie.append("&mobile=").append(user.getMobile());
			// cookie.append("&voyager_token=").append(user.getVoyagerToken());
			cookie.append("&ip=").append(user.getIp());
			cookie.append("&role=").append(user.getRoleJson());
			// cookie.append("&last_update=").append(Utils.getCreateDate());
			AESCipher aes = new AESCipher(Constants.AES_KEY, Constants.AES_IV, AESCipher.AES_PKCS7_PADDING);
			aes.setEncryptionMode(AESCipher.AES_MODE_CBC);
			int age = -1;
			if (keepCookie)
				age = Constants.COOKIE_EXPIRE;
			setCookie(response, Constants.COOKIE_VOYAGER_MASTER,
					aes.encrypt(cookie.toString().getBytes(CharEncoding.UTF_8)), age, "/", null);
		} catch (Exception e) {
			log.error("setVoyagerCookie {}", e);
			//throw new RuntimeException("Exception generated.", e);
		}
	}

	public static void setLoginReferCookie(HttpServletResponse response, String loginReferer) {
		try {
			setCookie(response, "voyager_login_referer", loginReferer, -1, "/", null);
		} catch (Exception e) {
			log.error("setLoginReferCookie {}", e);
			//throw new RuntimeException("Exception generated.", e);
		}
	}

	/**
	 * request 에서 voyager 쿠키를 추출해서 BipUser 정보를 가져온다.
	 * 
	 * @param request
	 * @return BipUser
	 */
	public static BipUser getVoyagerCookie(HttpServletRequest request) {
		String voyagerMaster = getCookie(request, Constants.COOKIE_VOYAGER_MASTER);
		return getBipUserFromEncryptedString(voyagerMaster);
	}

	/**
	 * 프로젝트 별로 MSTR 세션 정보를 쿠키로 생성한다.
	 *
	 * @param response
	 * @param projectId
	 * @param sessionId
	 * @param keepCookie
	 */
	public static void setMstrCookie(HttpServletResponse response, String projectId, String sessionId, boolean keepCookie) {
		try {
			StringBuffer cookie = new StringBuffer(128);
			cookie.append(sessionId);
			int age = -1;
			if (keepCookie)
				age = Constants.COOKIE_EXPIRE;
			setCookie(response, projectId, sessionId, age, "/", null);
		} catch (Exception e) {
			log.error("setMstrCookie {}", e);
			//throw new RuntimeException("Exception generated.", e);
		}
	}

	/**
	 * request에서 MSTR 쿠키를 추출한다.
	 *
	 * @param request
	 * @return BipUser
	 */
	public static String getMstrCookie(HttpServletRequest request, String projectId) {
		return getCookie(request, projectId);
	}

	/**
	 * 암호화된 쿠키 정보를 복호화해 BipUser 정보를 가져온다.
	 * 
	 * @param encryptedString
	 * @return BipUser
	 */
	public static BipUser getBipUserFromEncryptedString(String encryptedString) {
		BipUser user = null;
		try {
			if (StringUtils.isEmpty(encryptedString))
				return null;
			AESCipher aes = new AESCipher(Constants.AES_KEY, Constants.AES_IV, AESCipher.AES_PKCS7_PADDING);
			aes.setEncryptionMode(AESCipher.AES_MODE_CBC);
			String decrypt = aes.decrypt(encryptedString.getBytes(CharEncoding.UTF_8));
			Map<String, Object> cookie = Utils.getQueryParam(decrypt);
			user = new BipUser();
			user.setIp((String) cookie.get("ip"));
			// user.setVoyagerToken((String) cookie.get("voyager_token"));
			user.setLastupDate((String) cookie.get("last_update"));
			if (cookie.get("id") != null)
				user.setId(Integer.parseInt((String) cookie.get("id")));
			user.setEmail((String) cookie.get("email"));
			user.setMobile((String) cookie.get("mobile"));
			user.setUsername((String) cookie.get("username"));
			user.setRoleJson((String) cookie.get("role"));
			user.setFullname((String) cookie.get("fullname"));
		} catch (Exception e) {
			log.error("getBipUserFromEncryptedString {}", e);
			// throw new RuntimeException("Exception generated.", e);
		}
		return user;
	}

	public static void deleteAllCookie(HttpServletResponse response) {
		CookieUtils.deleteCookie(response, Constants.COOKIE_VOYAGER_MASTER);
		CookieUtils.deleteCookie(response, "voyager_login_referer");
	}

	public static void deleteRefererCookie(HttpServletResponse response) {
		CookieUtils.deleteCookie(response, "voyager_login_referer");
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpServletResponse response = null;

		try {
			CookieUtils.setCookie(response, "voyager_token", "xxxxxxxxx", 24 * 60 * 60, "/", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
