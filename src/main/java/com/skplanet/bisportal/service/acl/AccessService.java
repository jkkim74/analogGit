package com.skplanet.bisportal.service.acl;

import java.util.List;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.acl.AccessLog;
import com.skplanet.bisportal.model.acl.LoginLog;

/**
 * The AccessLogService interface(AccessLog 처리 인터페이스).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
public interface AccessService {
	int createAccessLog(AccessLog accessLog);

	int createLoginLog(LoginLog loginLog);

	List<AccessLog> getReportUseCount(JqGridRequest jqGridRequest) throws Exception;
}
