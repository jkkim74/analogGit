package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.ComStdDt;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.BleNewTech;

/**
 * Created by pepsi on 2014. 12. 16..
 */
@Repository
public class BleNewTechRepository {
	@Resource(name = "ocbSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * MERCHANT 정보 조회
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getMerchantDetail(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getMerchantDetail", jqGridRequest);
	}

	/**
	 * 주차 정보 조회
	 *
	 * @param jqGridRequest
	 * @return Collection of ComStdDt
	 */
	public List<ComStdDt> getWeekNumberOfMonth(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getWeekNumberOfMonth", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 - 합계
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechBySumTotal(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechBySumTotal", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 액셀 다운로드용 - 합계
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechBySumTotalForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechBySumTotalForExcel", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 - 일별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByDailyTotal(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByDailyTotal", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 엑셀다운로드 - 일별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByDailyTotalForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByDailyTotalForExcel", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 - 주별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByWeeklyTotal(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByWeeklyTotal", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 엑셀다운로드 - 주별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByWeeklyTotalForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByWeeklyTotalForExcel", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 - 월별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByMonthlyTotal(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByMonthlyTotal", jqGridRequest);
	}

	/**
	 * BLE 서비스 전체 PV/UV 통계 엑셀다운로드 - 월별
	 *
	 * @param jqGridRequest
	 * @return Collection of BleNewTech
	 */
	public List<BleNewTech> getNewTechByMonthlyTotalForExcel(JqGridRequest jqGridRequest) {
		return sqlSession.selectList("getNewTechByMonthlyTotalForExcel", jqGridRequest);
	}

}
