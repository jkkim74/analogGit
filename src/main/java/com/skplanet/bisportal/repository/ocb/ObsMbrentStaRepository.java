package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by cookatrice on 2014. 5. 7..
 */

@Repository
public class ObsMbrentStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 회원가입 DAY- grid
	 * 
	 * @param jqGRidRequest
	 * @return 기준일자, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterForGridPerDay(JqGridRequest jqGRidRequest) {
		return sqlSession.selectList("getEnterForGridPerDay", jqGRidRequest);
	}

	/**
	 * 회원가입 WEEK- grid
	 * 
	 * @param jqGRidRequest
	 * @return 기준일자, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterForGridPerWeek(JqGridRequest jqGRidRequest) {
		return sqlSession.selectList("getEnterForGridPerWeek", jqGRidRequest);
	}

	/**
	 * 회원가입 MONTH- grid
	 * 
	 * @param jqGRidRequest
	 * @return 기준일자, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterForGridPerMonth(JqGridRequest jqGRidRequest) {
		return sqlSession.selectList("getEnterForGridPerMonth", jqGRidRequest);
	}

	/**
	 * 회원가입(성별,연령) DAY- grid
	 * 
	 * @param jqGridRequest
	 * @return 기준일자, 성별코드, 연령코드, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterDetailForGridPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getEnterDetailForGridPerDay", jqGridRequest);
	}

	/**
	 * 회원가입(성별,연령) WEEK- grid
	 * 
	 * @param jqGridRequest
	 * @return 기준일자, 성별코드, 연령코드, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterDetailForGridPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getEnterDetailForGridPerWeek", jqGridRequest);
	}

	/**
	 * 회원가입(성별,연령) MONTH- grid
	 * 
	 * @param jqGridRequest
	 * @return 기준일자, 성별코드, 연령코드, 가입자수, 누적계
	 */
	public List<ObsMbrentSta> getEnterDetailForGridPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getEnterDetailForGridPerMonth", jqGridRequest);
	}
}
