package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsCntntFlyrDetlSta;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cookatrice
 */
@Repository
public class ObsCntntFlyrDetlStaRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 상권전단내 매장전단 DAY - pivot
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntFlyrDetlSta> getStoreFlyerInTradeAreaFlyerForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerInTradeAreaFlyerForPivotPerDay", jqGridRequest);
	}
	/**
	 * 상권전단내 매장전단 WEEK - pivot
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntFlyrDetlSta> getStoreFlyerInTradeAreaFlyerForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerInTradeAreaFlyerForPivotPerWeek", jqGridRequest);
	}
	/**
	 * 상권전단내 매장전단 MONTH - pivot
	 * @param jqGridRequest
	 * @return
	 */
	public List<ObsCntntFlyrDetlSta> getStoreFlyerInTradeAreaFlyerForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getStoreFlyerInTradeAreaFlyerForPivotPerMonth", jqGridRequest);
	}
}
