package com.skplanet.bisportal.service.mstr;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.microstrategy.web.objects.WebObjectsException;
import com.skplanet.bisportal.model.acl.Login;
import com.skplanet.bisportal.model.bip.ComRoleUser;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import com.skplanet.bisportal.model.mstr.MstrResponse;

/**
 * The MstrService class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
public interface MstrService {
	void createSession(HttpServletRequest request, HttpServletResponse response, Login loginUser);

	void closeSession(HttpServletRequest request, HttpServletResponse response);

	MstrResponse getCheckSession(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws WebObjectsException;

	MstrResponse getPrompts(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws Exception;

	ComRoleUser getMstrRole(ComRoleUser comRoleUser) throws Exception;

	MstrResponse getDateTypes(HttpServletRequest request, HttpServletResponse response, MstrRequest mstrRequest) throws Exception;

	MstrResponse getWeeks(MstrRequest mstrRequest) throws Exception;
}
