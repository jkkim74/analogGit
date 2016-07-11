package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.acl.AccessLog;
import com.skplanet.bisportal.model.acl.LoginLog;
import com.skplanet.bisportal.repository.acl.AccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The AccessLogService class(BipUser 처리 클래스).
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Service
@Slf4j
public class AccessServiceImpl implements AccessService {
	@Autowired
	private AccessLogRepository accessLogRepository;

	/**
	 * 사용자 접근 로그 등록.
	 *
	 * @param accessLog
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int createAccessLog(AccessLog accessLog) {
		int result = 0;
		try {
			result = accessLogRepository.createAccessLog(accessLog);
		} catch (Exception e) {
			log.error("createAccessLog {}", e);
		}
		return result;
	}

	/**
	 * 사용자 로그인 로그 등록.
	 *
	 * @param loginLog
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int createLoginLog(LoginLog loginLog) {
		int result = 0;
		try {
			result = accessLogRepository.createLoginLog(loginLog);
		} catch (Exception e) {
			log.error("createLoginLog {}", e);
		}
		return result;
	}

	/**
	 * UV, PV 정보 조회.
	 *
	 * @param jqGridRequest
	 * @return collection of AccessLog
	 * @throws Exception
	 */
	@Override
	public List<AccessLog> getReportUseCount(JqGridRequest jqGridRequest) throws Exception {
		return accessLogRepository.getReportUseCount(jqGridRequest);
	}
}
