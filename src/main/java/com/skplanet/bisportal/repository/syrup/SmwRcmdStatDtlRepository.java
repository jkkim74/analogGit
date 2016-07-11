package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwRcmdStatDtl;

/**
 * Created by lko on 2014-12-05.
 */
@Repository
public class SmwRcmdStatDtlRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<SmwRcmdStatDtl> getSmwRcmdStatDtlPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSmwRcmdStatDtlPerDay", jqGridRequest);
	}

	public List<SmwRcmdStatDtl> getSmwRcmdStatDtlPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSmwRcmdStatDtlPerWeek", jqGridRequest);
	}

	public List<SmwRcmdStatDtl> getSmwRcmdStatDtlPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSmwRcmdStatDtlPerMonth", jqGridRequest);
	}
}
