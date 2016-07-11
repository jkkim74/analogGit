package com.skplanet.bisportal.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * The CorsFilter class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 */
public class CorsFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// exclude resource files from filtering
		if (request.getRequestURI().matches(".*(css|jpg|png|gif|js|groovy)")) {
			filterChain.doFilter(request, response);
			return;
		}
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		response.addHeader("Access-Control-Allow-Headers",
				"origin, content-type, accept, x-requested-with, sid, SM_USER");
		//response.addHeader("Access-Control-Max-Age", "1800");// 30 min
		filterChain.doFilter(request, response);
	}
}
