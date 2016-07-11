package com.skplanet.bisportal.service.bip;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import com.skplanet.bisportal.model.bip.DashboardData;

/**
 * @author cookatrice
 */
public interface DashboardService {
	List<BpmDlyPrst> getDashboardData(WhereCondition whereCondition);

	List<DaaEwmaStatDaily> getEwmaChartData(WhereCondition whereCondition);

	List<DaaEwmaStatDaily> getOcbEwmaChartData(WhereCondition whereCondition);


	/**
	 * 일별 지표 실적 리스트
	 *
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getDashboardDailyResult(Condition condition) throws Exception;

	/**
	 * 주별 지표 실적 리스트
	 *
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getDashboardWeeklyResult(Condition condition) throws Exception;

	/**
	 * 월별 지표 실적 리스트
	 *
	 * @param condition
	 * @return
	 */
	List<SummaryReportRow> getDashboardMonthlyResult(Condition condition) throws Exception;

	/**
	 * 엑셀 다운로드용 View
	 *
	 * @param jqGridRequest 요청 파라미터.
	 * @return
	 */
	ModelAndView getDashboardReportView(JqGridRequest jqGridRequest) throws Exception;

	/**
	 * 날짜 -> 주차 변환
	 *
 	 * @param curDate
	 * @return
	 */
	List<String> makeDayToWeek(String curDate);

	/**
	 * 대시보드 카테고리 리스트 로드
	 *
	 * @param whereCondition
	 * @return
	 */
	List<WhereCondition> getDashboardCategory(WhereCondition whereCondition);

	/**
	 * 대시보드 sparkline chart 데이터셋 로드
	 *
	 * @param condition
	 * @return
	 */
	List<BpmDlyPrst> getDashboardSparklineDataset(Condition condition);

	/**
	 * 대시보드 sparkline chart 데이터셋 로드 (직전7일)
	 *
	 * @param whereCondition
	 * @return
	 */
	List<BpmDlyPrst> getDashboardSparklinePast7DayDataset(WhereCondition whereCondition);

	/**
	 * 경영실적 일별 데이터 저장
	 *
	 * @param dashboardData
	 * @return 배치 실행 파일 이름
	 */
	String saveBpmIdxCttMappInfoPerDay(DashboardData dashboardData);

	/**
	 * 경영실적 월별 데이터 저장
	 *
	 * @param dashboardData
	 * @return 배치 실행 파일 이름
	 */
	String saveBpmIdxCttMappInfoPerMonth(DashboardData dashboardData);

	/**
	 * 경영실적 주별 데이터 저장
	 *
	 * @param dashboardData
	 * @return 배치 실행 파일 이름
	 */
	String saveBpmIdxCttMappInfoPerWeek(DashboardData dashboardData);
}
