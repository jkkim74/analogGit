/**
 *
 */
package com.skplanet.bisportal.common.resolver;

import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.model.acl.BipUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ho-Jin, Ha (mimul@wiseeco.com)
 */
@Slf4j
public class BipUserArgumentResolver implements HandlerMethodArgumentResolver {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.
	 * springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.debug("supportsParameter {}", BipUser.class.isAssignableFrom(parameter.getParameterType()));
		return parameter.getParameterType().equals(BipUser.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.
	 * springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer,
	 * org.springframework.web.context.request.NativeWebRequest,
	 * org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		String voyagerMaster = CookieUtils.getCookie(request, Constants.COOKIE_VOYAGER_MASTER);
		if (StringUtils.isEmpty(voyagerMaster)) {
			voyagerMaster = request.getHeader(Constants.COOKIE_VOYAGER_MASTER);
			if (StringUtils.isEmpty(voyagerMaster))
				voyagerMaster = request.getParameter(Constants.COOKIE_VOYAGER_MASTER);
		}

		if (!StringUtils.isEmpty(voyagerMaster)) {
			return CookieUtils.getVoyagerCookie(request);
		} else {
			throw new Exception("Authentication credentials not found.");
		}
	}

}
