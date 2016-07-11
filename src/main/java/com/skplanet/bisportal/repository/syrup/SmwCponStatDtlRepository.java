package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCponStatDtl;

/**
 * Created by lko on 2014-11-21.
 */
@Repository
public class SmwCponStatDtlRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	public List<SmwCponStatDtl> getCponStatDtlPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerDay", jqGridRequest);
	}

	public List<SmwCponStatDtl> getCponStatDtlPerDayForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerDayForExcel", jqGridRequest);
	}

	public List<SmwCponStatDtl> getCponStatDtlPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerWeek", jqGridRequest);
	}

	public List<SmwCponStatDtl> getCponStatDtlPerWeekForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerWeekForExcel", jqGridRequest);
	}

	public List<SmwCponStatDtl> getCponStatDtlPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerMonth", jqGridRequest);
	}

	public List<SmwCponStatDtl> getCponStatDtlPerMonthForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getCponStatDtlPerMonthForExcel", jqGridRequest);
	}
}
