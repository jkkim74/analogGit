package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.RtdDau;

/**
 * The RtdDauRepository class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Repository
public class RtdDauRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜기준 DAU 정보 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau
	 */
	public List<RtdDau> getRtdDauByStdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getRtdDauByStdDt", rtdDau);
	}

	/**
	 * 날짜기준 DAU 정보 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau
	 */
	public List<RtdDau> getOcbRtdDauByStdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getOcbRtdDauByStdDt", rtdDau);
	}

	/**
	 * 날짜기준 DAU 정보 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau
	 */
	public List<RtdDau> getSyrupRtdDauByStdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getSyrupRtdDauByStdDt", rtdDau);
	}

	/**
	 * 전날 OCB DAU 정보 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau
	 */
	public List<RtdDau> getOcbRtdDauByPreStrdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getOcbRtdDauByPreStrdDt", rtdDau);
	}

	/**
	 * 전날 Syrup DAU 정보 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau
	 */
	public List<RtdDau> getSyrupRtdDauByPreStrdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getSyrupRtdDauByPreStrdDt", rtdDau);
	}

	/**
	 * 날짜기준 DAU 정보 존재 유무 조회.
	 *
	 * @param rtdDau
	 * @return Collection of RtdDau count
	 */
	public List<RtdDau> getRtdDauCountByStdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getRtdDauCountByStdDt", rtdDau);
	}

	/**
	 * OCB DAU(KPI 기준) DAY - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getOcbDauForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getOcbDauForPivotPerDay", jqGridRequest);
	}

	/**
	 * OCB DAU(KPI 기준) WEEK - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getOcbDauForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getOcbDauForPivotPerWeek", jqGridRequest);
	}

	/**
	 * OCB DAU(KPI 기준) MONTH - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getOcbDauForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getOcbDauForPivotPerMonth", jqGridRequest);
	}

	/**
	 * Syrup DAU DAY - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getSyrupDauForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getSyrupDauForPivotPerDay", jqGridRequest);
	}

	/**
	 * App Stickness DAY - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getAppSticknessForPivotPerDay(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getAppSticknessForPivotPerDay", jqGridRequest);
	}

	/**
	 * App Stickness WEEK - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getAppSticknessForPivotPerWeek(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getAppSticknessForPivotPerWeek", jqGridRequest);
	}

	/**
	 * App Stickness MONTH - grid(세로형)
	 *
	 * @param jqGridRequest
	 * @return
	 */
	public List<RtdDau> getAppSticknessForPivotPerMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getAppSticknessForPivotPerMonth", jqGridRequest);
	}

	public List<RtdDau> getOcbSyrupRtdDauCountByStdDt(RtdDau rtdDau) {
		return sqlSession.selectList("getOcbSyrupRtdDauCountByStdDt", rtdDau);
	}
}
