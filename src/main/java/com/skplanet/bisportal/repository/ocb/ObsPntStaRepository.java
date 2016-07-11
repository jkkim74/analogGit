package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsPntSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 포인트 이용통계 Repository.
 * 
 * @author sjune
 */
@Repository
public class ObsPntStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 포인트 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPntSta> getPntsPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getPntsPerDay", jqGridRequest);
	}

	/**
	 * 주별 포인트 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPntSta> getPntsPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getPntsPerMonth", jqGridRequest);
	}

	/**
	 * 월별 포인트 조회
	 * 
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsPntSta> getPntsPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getPntsPerWeek", jqGridRequest);
	}
}
