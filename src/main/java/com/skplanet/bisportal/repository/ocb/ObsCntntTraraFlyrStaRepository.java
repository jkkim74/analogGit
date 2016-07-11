package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsCntntTraraFlyrSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 상권전단 repository
 * 
 * @author sjune
 */
@Repository
public class ObsCntntTraraFlyrStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 일별 매장전단 - pivot
	 * 
	 * @param jqGridRequest
	 * @return The list of ObsCntntTraraFlyrSta
	 */
	public List<ObsCntntTraraFlyrSta> getStoreFlyerForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerForPivotPerDay", jqGridRequest);
	}

	/**
	 * 주별 매장전단 WEEK
	 * 
	 * @param jqGridRequest
	 * @return The list of ObsCntntTraraFlyrSta
	 */
	public List<ObsCntntTraraFlyrSta> getStoreFlyerForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerForPivotPerWeek", jqGridRequest);
	}

	/**
	 * 월별 매장전단 - pivot
	 * 
	 * @param jqGridRequest
	 * @return The list of ObsCntntTraraFlyrSta
	 */
	public List<ObsCntntTraraFlyrSta> getStoreFlyerForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerForPivotPerMonth", jqGridRequest);
	}
}
