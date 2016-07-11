package com.skplanet.bisportal.repository.acl;

import java.util.List;

import javax.annotation.Resource;

import com.skplanet.bisportal.model.acl.LoginLog;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.acl.AccessLog;

/**
 * Created by pepsi on 2014. 5. 28..
 */
@Repository
public class AccessLogRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public int createAccessLog(AccessLog accessLog) {
		return sqlSession.insert("createAccessLog", accessLog);
	}

	public int createLoginLog(LoginLog loginLog) {
		return sqlSession.insert("createLoginLog", loginLog);
	}

	public List<AccessLog> getReportUseCount(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getReportUseCount", jqGridRequest);
	}
}
