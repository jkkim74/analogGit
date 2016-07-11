package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwCardIssue;

/**
 * Created by cookatrice on 15. 1. 9..
 */

@Repository
public class SmwCardIssueRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 멤버십발급 일별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwCardIssue> getMembershipIssuePerDay(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMembershipIssuePerDay", olapDimensionRequest);
	}

	/**
	 * 멤버십발급 주별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */

	public List<SmwCardIssue> getMembershipIssuePerWeek(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMembershipIssuePerWeek", olapDimensionRequest);
	}

	/**
	 * 멤버십발급 월별조회
	 * 
	 * @param olapDimensionRequest
	 * @return
	 */
	public List<SmwCardIssue> getMembershipIssuePerMonth(OlapDimensionRequest olapDimensionRequest) {
		return sqlSession.selectList("getMembershipIssuePerMonth", olapDimensionRequest);
	}

}
