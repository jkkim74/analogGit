package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;

/**
 * The SmwSyrupDauFunnelsRepository class.
 *
 * Created by ophelisis on 2015. 7. 27..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 */
@Repository
public class SmwSyrupDauFunnelsRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * Syrup DAU 기준 유입경로 일별조회
	 * 
	 * @param jqGridRequest
	 * @return Collection of SmwSyrupDauFunnels
	 */
	public List<SmwSyrupDauFunnels> getInflRtVisitSituationPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getInflRtVisitSituationPerDay", jqGridRequest);
	}
}
