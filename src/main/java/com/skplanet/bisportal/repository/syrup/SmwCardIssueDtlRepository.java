package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssueDtl;

/**
 * Created by lko on 2014-11-27.
 */
@Repository
public class SmwCardIssueDtlRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<SmwCardIssueDtl> getCardIssueMem(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCardIssueMem", jqGridRequest);
	}

	public List<SmwCardIssueDtl> getCardIssuePaVou(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCardIssuePaVou", jqGridRequest);
	}
}
