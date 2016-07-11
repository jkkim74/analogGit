package com.skplanet.bisportal.controller.acl.login;

import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.acl.Login;
import com.skplanet.bisportal.model.acl.LoginLog;
import com.skplanet.bisportal.service.acl.AccessService;
import com.skplanet.bisportal.service.acl.UserService;
import com.skplanet.bisportal.service.mstr.MstrService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

/**
 * Created by pepsi on 2014. 5. 16..
 */
@Slf4j
@Controller
public class AuthController {
	@Value("#{common['mstr.run.state']}")
	private String MSTR_RUN_STATE;

	@Autowired
	private UserService userServiceImpl;

	@Autowired
	private AccessService accessServiceImpl;

	@Autowired
	private MstrService mstrServiceImpl;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/dau", method = RequestMethod.GET)
	public String DAU() {
		return "dau";
	}

	@RequestMapping(value = "/kid", method = RequestMethod.GET)
	public String kid() {
		return "kid";
	}

	/**
	 * 로그인 처리
	 *
	 * @param loginUser
	 *            로그인 정보
	 * @return 쿠키 셋팅.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String login(Login loginUser, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String result = "Success";
		try {
			BipUser user = userServiceImpl.login(loginUser.getUsername(), loginUser.getPassword(),
					CookieUtils.getIpAddress(request));
			if (user != null) {
				// set login cookie
				CookieUtils.setVoyagerCookie(response, user, loginUser.isLoginCheck());
				// set MSTR cookie
				if (StringUtils.equals(MSTR_RUN_STATE, Constants.ON))
					mstrServiceImpl.createSession(request, response, loginUser);
				// set login leferer cookie
				if (StringUtils.isNotEmpty(loginUser.getReturnUrl()))
					CookieUtils.setLoginReferCookie(response, URLEncoder.encode(loginUser.getReturnUrl(), CharEncoding.UTF_8));

				// 접근 로그 저장.
				LoginLog loginLog = new LoginLog();
				loginLog.setUsername(loginUser.getUsername().toUpperCase());
				loginLog.setIp(CookieUtils.getIpAddress(request));
				loginLog.setAgent(Utils.getUserAgent(request));
				loginLog.setLastUpdate(Utils.getCreateDate());
				accessServiceImpl.createLoginLog(loginLog);
			} else {
				log.info("Ldap Login Failed. {}", loginUser.getUsername());
				throw new Exception("wrong username or password");
			}
		} catch (Exception e) {
			log.error("Login Failed. {}", e);
			throw new Exception("wrong username or password");
		}
		return result;
	}

	@RequestMapping(value = "/sso", method = {RequestMethod.GET, RequestMethod.HEAD})
	public String singleSignOn(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String loginId = null;
		String serverSession = (StringUtils.defaultIfEmpty(request.getParameter("authinform"),StringUtils.EMPTY));
		String serverSessionId = (StringUtils.defaultIfEmpty(request.getHeader("SM_SERVERSESSIONID"), StringUtils.EMPTY));
		//log.info("SSO(Header) serverSession {}, serverSessionId {}", serverSession, serverSessionId);
		if(StringUtils.isNotEmpty(serverSession) && StringUtils.equals(serverSession, serverSessionId)) {
			loginId = (StringUtils.defaultIfEmpty(request.getHeader("SM_USER"), StringUtils.EMPTY));
		}
		if (StringUtils.isEmpty(loginId)) {
			loginId = CookieUtils.getCookie(request, "SM_USER");
		}
		if (StringUtils.isEmpty(loginId)) {
			log.info("SSO loginId is null.");
			return "redirect:/";
		}
		userServiceImpl.sso(loginId, CookieUtils.getIpAddress(request), Utils.getUserAgent(request), response);
		return "redirect:/";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String signOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CookieUtils.deleteAllCookie(response);
		if (StringUtils.equals(MSTR_RUN_STATE, Constants.ON))
			mstrServiceImpl.closeSession(request, response);
		return "redirect:/login?returnUrl=";
	}

	@RequestMapping(value = "/deleteReferer", method = RequestMethod.GET)
	public AjaxResult deleteReferer(HttpServletResponse response) throws Exception {
		CookieUtils.deleteRefererCookie(response);
		AjaxResult result = new AjaxResult();
		result.setCode(200);
		result.setMessage("success");
		return result;
	}

	@RequestMapping(value = "/login/cookie", method = RequestMethod.GET)
	public @ResponseBody BipUser cookie(HttpServletRequest request) throws Exception {
		BipUser user = CookieUtils.getVoyagerCookie(request);
		return user;
	}
}
