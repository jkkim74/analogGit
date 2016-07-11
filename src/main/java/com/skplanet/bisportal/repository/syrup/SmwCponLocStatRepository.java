package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCponLocStat;

/**
 * Created by lko on 2014-11-28.
 */
@Repository
public class SmwCponLocStatRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<SmwCponLocStat> getCponLocStatPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponLocStatPerDay", jqGridRequest);
	}

	public List<SmwCponLocStat> getCponLocStatPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponLocStatPerWeek", jqGridRequest);
	}

	public List<SmwCponLocStat> getCponLocStatPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponLocStatPerMonth", jqGridRequest);
	}
}
