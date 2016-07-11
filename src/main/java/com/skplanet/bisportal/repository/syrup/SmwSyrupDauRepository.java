package com.skplanet.bisportal.repository.syrup;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.RtdDau;

/**
 * The SmwSyrupDauFunnelsRepository class.
 *
 * Created by ophelisis on 2015. 7. 27..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 */
@Repository
public class SmwSyrupDauRepository {
	@Resource(name = "syrupSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * Syrup DAU WEEK - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getSyrupDauForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSyrupDauForPivotPerWeek", jqGridRequest);
	}

	/**
	 * Syrup DAU MONTH - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getSyrupDauForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSyrupDauForPivotPerMonth", jqGridRequest);
	}
}
