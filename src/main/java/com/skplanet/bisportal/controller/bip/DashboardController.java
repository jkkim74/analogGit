package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.controller.ReportController;
import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.common.validator.DashboardValidator;
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.DaaEwmaStatDaily;
import com.skplanet.bisportal.model.bip.DashboardData;
import com.skplanet.bisportal.service.bip.AdminService;
import com.skplanet.bisportal.service.bip.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.isDay;
import static com.skplanet.bisportal.common.utils.DateUtil.isWeek;

/**
 * @author cookatrice
 */
@Slf4j
@Controller
@RequestMapping("/dashboard")
public class DashboardController extends ReportController {
	@Qualifier("dashboardValidator")
	@Autowired
	private DashboardValidator dashboardValidator;
	@Autowired
	private DashboardService dashboardServiceImpl;
	@Autowired
	private AdminService adminServiceImpl;

	/**
	 * 날짜 -> 주차 변환
	 *
	 * @param curDate
	 * @return
	 */
	@RequestMapping(value = "/dayToWeek", method = RequestMethod.GET)
	@ResponseBody
	public String makeDayToWeek(String curDate) {
		String resultStr = "XXXX-XX-X";
		List<String> tmpResult = dashboardServiceImpl.makeDayToWeek(curDate);
		if (tmpResult.size() > 0) {
			resultStr = tmpResult.get(0);
		}
		return resultStr;
	}

	/**
	 * 대시보드 카테고리 리스트 로드
	 *
	 * @param whereCondition
	 * @return dashboard category's condition list
	 */
	@RequestMapping(value = "/category", method = RequestMethod.GET)
	@ResponseBody
	public List<DashboardData> getDashboardCategory(WhereCondition whereCondition) {
		String startDate = whereCondition.getStartDate();
		String endDate = whereCondition.getEndDate();
		List<WhereCondition> whereConditions = dashboardServiceImpl.getDashboardCategory(whereCondition);
		String dateType = whereCondition.getDateType();

		if(whereConditions.size() <= 0){
			return null;
		}

		Condition condition = new Condition();
		condition.setStartDate(startDate);
		condition.setEndDate(endDate);
		condition.setWhereConditions(whereConditions);
		condition.setDateType(dateType);
		condition.setOneYearBefore(false);
		List<BpmDlyPrst> sparklineDataset = getDashboardSparklineDataset(condition);

		condition.setOneYearBefore(true);
		List<BpmDlyPrst> sparklineOldDataset = getDashboardSparklineDataset(condition);

		List<DashboardData> dashboardDatas = new ArrayList<>();
		for(WhereCondition tmpCondition: whereConditions){
			DashboardData tmpData = new DashboardData();
			tmpData.setId("id_" + tmpCondition.getSvcId()
					+ "_" + tmpCondition.getIdxClGrpCd()
					+ "_" + tmpCondition.getIdxClCd()
					+ "_" + tmpCondition.getIdxCttCd());
			tmpData.setChartType(tmpCondition.getChartType());
			tmpData.setBm(tmpCondition.getSvcNm());
			tmpData.setKeyIndex(tmpCondition.getIdxClCdGrpNm());
			if (StringUtils.equals(tmpCondition.getChartType(), "sparkline")) {
				tmpData.setDataSet(separateDataset(tmpCondition.getSeq(), whereConditions, sparklineDataset));
				tmpData.setOldDataSet(separateDataset(tmpCondition.getSeq(), whereConditions, sparklineOldDataset));
			} else if (StringUtils.equals(tmpCondition.getChartType(), "ewma")) {
				tmpCondition.setSearchDate(condition.getEndDate());
				tmpData.setCurrentData(searchEwmaCurrentData(tmpCondition, sparklineDataset));
				tmpData.setEwmaSet(getEwmaDataset(tmpCondition));
			}

			if (StringUtils.equals(tmpCondition.getIdxClGrpCd(), "L017")){
				WhereCondition tmpWhereCondition = new WhereCondition();
				tmpWhereCondition.setSvcId(tmpCondition.getSvcId());
				tmpWhereCondition.setIdxClGrpCd("L117");
				tmpWhereCondition.setIdxClCd(tmpCondition.getIdxClCd());
				tmpWhereCondition.setIdxCttCd(tmpCondition.getIdxCttCd());
				tmpWhereCondition.setStartDate(startDate);
				tmpWhereCondition.setEndDate(endDate);
				tmpData.setPast7DayDataSet(getDashboardSparklinePast7DayDataset(tmpWhereCondition));
			}
			tmpData.setSeq(tmpCondition.getSeq());
			dashboardDatas.add(tmpData);
		}

		return dashboardDatas;
	}

	/** 대시보드 sparkline chart 데이터셋 로드 */
	public List<BpmDlyPrst> getDashboardSparklineDataset(Condition condition) {
		return dashboardServiceImpl.getDashboardSparklineDataset(condition);
	}

	/** 대시보드 sparkline chart 데이터셋 로드 (직전 7일) */
	public List<BpmDlyPrst> getDashboardSparklinePast7DayDataset(WhereCondition whereCondition) {
		return dashboardServiceImpl.getDashboardSparklinePast7DayDataset(whereCondition);
	}

	/** sparkline chart 데이터셋 나누기 */
	public List<BpmDlyPrst> separateDataset(long seq, List<WhereCondition> whereConditions, List<BpmDlyPrst> sparklineDataset){

		List<BpmDlyPrst> result = new ArrayList<>();
		for (WhereCondition tmpCondition : whereConditions) {
			if (tmpCondition.getSeq() == seq){
				for(BpmDlyPrst tmpData : sparklineDataset){
					if (tmpData.getSvcId().equals(tmpCondition.getSvcId())
							&& tmpData.getIdxClGrpCd().equals(tmpCondition.getIdxClGrpCd())
							&& tmpData.getIdxClCd().equals(tmpCondition.getIdxClCd())
							&& tmpData.getIdxCttCd().equals(tmpCondition.getIdxCttCd())) {
						result.add(tmpData);
					}
				}
				break;
			}
		}
		return result;
	}

	/** 대시보드 ewma chart 데이터셋 로드 */
	public List<DaaEwmaStatDaily> getEwmaDataset(WhereCondition condition) {
		if (condition.getSvcId().intValue() == 25){
			return dashboardServiceImpl.getOcbEwmaChartData(condition);
		} else {
			return dashboardServiceImpl.getEwmaChartData(condition);
		}
	}
	
	/** ewma current 데이터 구하기*/
	public String searchEwmaCurrentData(WhereCondition condition, List<BpmDlyPrst> sparklineDataset) {
		String result = "-";

		for (BpmDlyPrst tmpData : sparklineDataset) {
			if (tmpData.getSvcId().equals(condition.getSvcId())
					&& tmpData.getIdxClGrpCd().equals(condition.getIdxClGrpCd())
					&& tmpData.getIdxClCd().equals(condition.getIdxClCd())
					&& tmpData.getIdxCttCd().equals(condition.getIdxCttCd())
					&& tmpData.getDlyStrdDt().equals(condition.getSearchDate())) {
				result = tmpData.getDlyRsltVal().toString();
				break;
			}
		}
		return result;
	}

	/**
	 * 대시보드 데이터 로드
	 * 
	 * @return list of BpmDlyPrst
	 */
	@RequestMapping(value = "/data", method = RequestMethod.GET)
	@ResponseBody
	public List<BpmDlyPrst> getDashboardData(WhereCondition whereCondition) {
		return dashboardServiceImpl.getDashboardData(whereCondition);
	}

	/**
	 * ewma chart data 로드
	 * 
	 * @param whereCondition
	 * @return
	 */
	@RequestMapping(value = "/ewmaChart/data", method = RequestMethod.GET)
	@ResponseBody
	public List<DaaEwmaStatDaily> getEwmaChartData(WhereCondition whereCondition) {
		return dashboardServiceImpl.getEwmaChartData(whereCondition);
	}

	/**
	 * OCB ewma chart data 로드 (임시)
	 * 
	 * @param whereCondition
	 * @return
	 */
	@RequestMapping(value = "/OcbEwmaChart/data", method = RequestMethod.GET)
	@ResponseBody
	public List<DaaEwmaStatDaily> getOcbEwmaChartData(WhereCondition whereCondition) {
		return dashboardServiceImpl.getOcbEwmaChartData(whereCondition);
	}

	/**
	 * 대시보드 리포트 일별 조회
	 *
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/report/daily", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getDashboardDailyResult(@RequestBody Condition condition) throws Exception {
		return dashboardServiceImpl.getDashboardDailyResult(condition);
	}

	/**
	 * 대시보드 리포트 주별 조회
	 *
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/report/weekly", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getDashboardWeeklyResult(@RequestBody Condition condition) throws Exception {
		return dashboardServiceImpl.getDashboardWeeklyResult(condition);
	}

	/**
	 * 대시보드 리포트 월별 조회
	 *
	 * @param condition
	 * @return
	 */
	@RequestMapping(value = "/report/monthly", method = RequestMethod.POST)
	@ResponseBody
	public List<SummaryReportRow> getDashboardMonthlyResult(@RequestBody Condition condition) throws Exception {
		return dashboardServiceImpl.getDashboardMonthlyResult(condition);
	}

	/**
	 * Dashboard 경영실적 등록 처리.
	 *
	 * @param dashboardData
	 * @return
	 */
	@RequestMapping(value = "/data/add", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addDashboardData(@RequestBody DashboardData dashboardData, BindingResult bindingResult) {
		log.debug("dashboardData {}", dashboardData.toString());
		AjaxResult ajaxResult = new AjaxResult();
		String trmsFlNm;
		try {
			dashboardValidator.validate(dashboardData, bindingResult);
			if (bindingResult.hasErrors()) {
				log.error("{}", bindingResult.toString());
				ajaxResult.setCode(400);
				ajaxResult.setMessage("bad request");
				return ajaxResult;
			}
			if (isDay(dashboardData.getDateType())) {
				trmsFlNm = dashboardServiceImpl.saveBpmIdxCttMappInfoPerDay(dashboardData);
			} else if (isWeek(dashboardData.getDateType())) {
				trmsFlNm = dashboardServiceImpl.saveBpmIdxCttMappInfoPerWeek(dashboardData);
			} else {
				trmsFlNm = dashboardServiceImpl.saveBpmIdxCttMappInfoPerMonth(dashboardData);
			}
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
			adminServiceImpl.runBatch(trmsFlNm);
		} catch (Exception e) {
			log.error("addDashboardData {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}
}
