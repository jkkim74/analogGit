package com.skplanet.bisportal.service.bip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.model.SummaryReportRowMap;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.common.utils.DateUtil;
import com.skplanet.bisportal.common.utils.ObjectMapperFactory;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.BpmDlyPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmMthStcPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmWkStcPrstRepository;
import com.skplanet.bisportal.repository.bip.DaaEwmaStatDailyRepository;
import com.skplanet.bisportal.repository.bip.DashboardRepository;
import com.skplanet.bisportal.repository.bip.HandInputRepository;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.*;

@Service
public class DashboardServiceImpl implements DashboardService {
	private final ObjectMapper objectMapper = ObjectMapperFactory.get();

	@Autowired
	private BpmDlyPrstRepository bpmDlyPrstRepository;

	@Autowired
	private DaaEwmaStatDailyRepository daaEwmaStatDailyRepository;

	@Autowired
	private CommonCodeService commonCodeServiceImpl;

	@Autowired
	private BpmWkStcPrstRepository bpmWkStcPrstRepository;

	@Autowired
	private BpmMthStcPrstRepository bpmMthStcPrstRepository;

	@Autowired
	private DashboardRepository dashboardRepository;

	@Autowired
	private HandInputRepository handInputRepository;

	@Override
	public List<BpmDlyPrst> getDashboardData(WhereCondition whereCondition) {
		return bpmDlyPrstRepository.getDashboardData(whereCondition);
	}

	@Override
	public List<DaaEwmaStatDaily> getEwmaChartData(WhereCondition whereCondition) {
		return daaEwmaStatDailyRepository.getEwmaChartData(whereCondition);
	}

	@Override
	public List<DaaEwmaStatDaily> getOcbEwmaChartData(WhereCondition whereCondition) {
		return daaEwmaStatDailyRepository.getOcbEwmaChartData(whereCondition);
	}

	@Override
	public ModelAndView getDashboardReportView(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("platformExcelView");
		try {
			Condition condition = new Condition();
			List<WhereCondition> whereConditions = objectMapper.readValue(
					URLDecoder.decode(jqGridRequest.getWhereConditions(), CharEncoding.UTF_8),
					new TypeReference<List<WhereCondition>>() {
					});
			condition.setWhereConditions(whereConditions);
			// 기준주차
			WhereCondition whereCondition = new WhereCondition();
			whereCondition.setSearchDate(jqGridRequest.getBasicDate());

			if (isWeek(jqGridRequest.getDateType())) {
				condition.setSearchDate(jqGridRequest.getBasicDate());
				whereCondition.setSearchDiff(14);
				SummaryHeader<BpmWkStrdInfo> weekHeader = new SummaryHeader<BpmWkStrdInfo>();
				weekHeader.setWeekNumber(commonCodeServiceImpl.getBasicDateWeekNumber(jqGridRequest.getBasicDate()));
				weekHeader.setPeriods(bpmWkStcPrstRepository.getWeeksAgoBpmWkStrdInfo(whereCondition));
				modelAndView.addObject("periodData", weekHeader);
				modelAndView.addObject("summaryData", this.getDashboardWeeklyResult(condition));
			} else if (isMonth(jqGridRequest.getDateType())) {
				condition.setSearchDate(jqGridRequest.getBasicDate().substring(0, 6));
				whereCondition.setStartDate(addMonths(jqGridRequest.getBasicDate(), -13));
				whereCondition.setEndDate(jqGridRequest.getBasicDate());
				SummaryHeader<BpmMthStrdInfo> mthHeader = new SummaryHeader<BpmMthStrdInfo>();
				mthHeader.setPeriods(bpmMthStcPrstRepository.getBpmMthStrdInfos(whereCondition));
				modelAndView.addObject("periodData", mthHeader);
				modelAndView.addObject("summaryData", this.getDashboardMonthlyResult(condition));
			} else {
				condition.setSearchDate(jqGridRequest.getBasicDate());
				whereCondition.setStartDate(addDays(jqGridRequest.getBasicDate(), -7));
				whereCondition.setEndDate(jqGridRequest.getBasicDate());
				SummaryHeader<BpmDayStrdInfo> dayHeader = new SummaryHeader<BpmDayStrdInfo>();
				dayHeader.setPeriods(bpmDlyPrstRepository.getBpmDayStrdInfos(whereCondition));
				modelAndView.addObject("periodData", dayHeader);
				modelAndView.addObject("summaryData", this.getDashboardDailyResult(condition));
			}
		} catch (Exception e) {
			throw new Exception("report view error!");
		}
		return modelAndView;
	}

	@Override
	public List<SummaryReportRow> getDashboardDailyResult(Condition condition) throws Exception {
		condition.setWhereConditions(setConditions(condition));
		List<BpmDlyPrst> summaryDailyResult = bpmDlyPrstRepository.getSummaryDailyResult(condition);
		String basicDate = condition.getSearchDate();
		SummaryReportRowMap rowMap = new SummaryReportRowMap();
		for (BpmDlyPrst bpmDlyPrst : summaryDailyResult) {
			String svcId = bpmDlyPrst.getSvcId().toString();
			String svcNm = bpmDlyPrst.getSvcNm().toString();
			String measure = bpmDlyPrst.getEisSvcComCd().getIdxCttCdVal();
			BigDecimal measureValue = bpmDlyPrst.getDlyRsltVal();
			String measureDate = bpmDlyPrst.getDlyStrdDt();

			SummaryReportRow row = rowMap.addOrGetRow(svcId + "_" + measure);
			// 서비스명
			row.setSvcNm(svcNm);
			// 지표
			row.setMeasure(measure);
			// 기준일
			if (basicDate.equals(measureDate)) {
				if (basicDate.equals(getCurrentYmd())) {
					// TODO 우선 기준일경우 하이픈(-) 처리를 위함, 추후 메타 테이블과 연동
					row.setBasicMeasureValue(null);
				} else {
					row.setBasicMeasureValue(measureValue);
				}
			}
			// 8일간 추이
			if (isBeforeWeek(basicDate, measureDate, 1)) {
				row.getPeriodMeasureValues().add(measureValue);
				row.getPeriodMeasureMap().put(measureDate, measureValue);
			}
			// 1일전
			if (isYesterday(basicDate, measureDate)) {
				row.setOneDayAgoMeasureValue(measureValue);
			}
			// 1주전
			if (isOneWeekAgo(basicDate, measureDate)) {
				row.setOneWeekAgoMeasureValue(measureValue);
			}
			// 1달전
			if (isOneMonthAgo(basicDate, measureDate)) {
				row.setOneMonthAgoMeasureValue(measureValue);
			}
		}
		return rowMap.toList();
	}

	@Override
	public List<SummaryReportRow> getDashboardWeeklyResult(Condition condition) throws Exception {
		condition.setWhereConditions(setConditions(condition));
		SummaryReportRowMap rowMap = new SummaryReportRowMap();
		String basicDate = condition.getSearchDate();
		String todayWeekNumber = commonCodeServiceImpl.getTodayWeekNumber();
		BasicDateWeekNumber basicDateWeekNumber = commonCodeServiceImpl.getBasicDateWeekNumber(basicDate);

		// 주차를 이용하여 리포트 정보 조회
		condition.setStartWeekNumber(basicDateWeekNumber.getFourteenWeekAgo()); // 시작주차 (14주전)
		condition.setEndWeekNumber(basicDateWeekNumber.getBasic()); // 마지막 주차 (기준일 주차)
		condition.setOneYearAgoWeekNumber(basicDateWeekNumber.getOneYearAgo()); // 1년전 주차
		List<BpmWkStcPrst> summaryWeeklyResult = bpmWkStcPrstRepository.getSummaryWeeklyResult(condition);
		for (BpmWkStcPrst bpmWkStcPrst : summaryWeeklyResult) {
			String measure = bpmWkStcPrst.getEisSvcComCd().getIdxCttCdVal();
			String weekNumber = bpmWkStcPrst.getWkStcStrdYmw();
			BigDecimal measureValue = bpmWkStcPrst.getWkStcRsltVal();
			SummaryReportRow row = rowMap.addOrGetRow(measure);
			// 기준주차
			if (basicDateWeekNumber.getBasic().equals(weekNumber)) {
				if (todayWeekNumber.equals(weekNumber)) {
					// TODO 기준주차인경우 하이픈(-) 처리를 위함, 추후 메타 테이블과 연동
					row.setBasicMeasureValue(null);
				} else {
					row.setBasicMeasureValue(measureValue);
				}
			}
			// 14주간 추이
			if (isFourteenWeekAgoWeekNumber(basicDateWeekNumber.getFourteenWeekAgo(), weekNumber)) {
				row.getPeriodMeasureValues().add(measureValue);
				row.getPeriodMeasureMap().put(weekNumber, measureValue);
			}
			// 1주전
			if (basicDateWeekNumber.getOneWeekAgo().equals(weekNumber)) {
				row.setOneWeekAgoMeasureValue(measureValue);
			}
			// 1년전
			if (basicDateWeekNumber.getOneYearAgo().equals(weekNumber)) {
				row.setOneYearAgoMeasureValue(measureValue);
			}
		}
		return rowMap.toList();
	}

	@Override
	public List<SummaryReportRow> getDashboardMonthlyResult(Condition condition) throws Exception {
		condition.setWhereConditions(setConditions(condition));
		List<BpmMthStcPrst> summaryWeeklyResult = bpmMthStcPrstRepository.getSummaryMonthlyResult(condition);
		String basicDate = condition.getSearchDate();
		SummaryReportRowMap rowMap = new SummaryReportRowMap();
		for (BpmMthStcPrst bpmMthStcPrst : summaryWeeklyResult) {
			String measure = bpmMthStcPrst.getEisSvcComCd().getIdxCttCdVal();
			BigDecimal measureValue = bpmMthStcPrst.getMthStcRsltVal();
			String measureDate = bpmMthStcPrst.getMthStcStrdYm();
			SummaryReportRow row = rowMap.addOrGetRow(measure);
			// 기준달
			if (basicDate.equals(measureDate)) {
				if (isCurrentYm(basicDate)) {
					// TODO 기준달일 경우 하이픈(-) 처리를 위함, 추후 메타 테이블과 연동
					row.setBasicMeasureValue(null);
				} else {
					row.setBasicMeasureValue(measureValue);
				}
			}
			// 13개월간 추이
			if (isBeforeMonth(basicDate, measureDate, 13)) {
				row.getPeriodMeasureValues().add(measureValue);
				row.getPeriodMeasureMap().put(measureDate, measureValue);
			}
			// 1달전
			if (isOneMonthAgo(basicDate + "01", measureDate + "01")) {
				row.setOneMonthAgoMeasureValue(measureValue);
			}
			// 1년전
			if (isOneYearAgo(basicDate + "01", measureDate + "01")) {
				row.setOneYearAgoMeasureValue(measureValue);
			}
		}
		return rowMap.toList();
	}

	/**
	 * targetWeekNumber 가 14주간에 속한지 확인한다.
	 *
	 * @param fourteenWeekAgoWeekNumber
	 * @param targetWeekNumber
	 * @return
	 */
	private boolean isFourteenWeekAgoWeekNumber(String fourteenWeekAgoWeekNumber, String targetWeekNumber) {
		return (Integer.parseInt(fourteenWeekAgoWeekNumber) <= Integer.parseInt(targetWeekNumber));
	}

	private List<WhereCondition> setConditions(Condition condition) {
		List<WhereCondition> whereConditions = new ArrayList<WhereCondition>();
		if (!CollectionUtils.isEmpty(condition.getWhereConditions())) {
			whereConditions = condition.getWhereConditions();
		}
		// Tstore UV
		WhereCondition whereCondition = new WhereCondition();
		whereCondition.setIdxClCd("M001");
		whereCondition.setIdxCttCd("S001");
		whereCondition.setIdxClGrpCd("L017");
		whereCondition.setSvcId(1l);
		whereConditions.add(whereCondition);
		return whereConditions;
	}

	/**
	 * 날짜 -> 주차 변환
	 *
	 * @param curDate
	 * @return
	 */
	@Override
	public List<String> makeDayToWeek(String curDate) {
		return dashboardRepository.makeDayToWeek(curDate);
	}

	/**
	 * 대시보드 카테고리 리스트 로드
	 * 
	 * @param whereCondition
	 * @return
	 */
	@Override
	public List<WhereCondition> getDashboardCategory(WhereCondition whereCondition) {
		return dashboardRepository.getDashboardCategory(whereCondition);
	}

	/**
	 * 대시보드 sparkline chart 데이터셋 로드
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<BpmDlyPrst> getDashboardSparklineDataset(Condition condition) {
		return dashboardRepository.getDashboardSparklineDataset(condition);
	}

	/**
	 * 대시보드 sparkline chart 데이터셋 로드 (직전7일)
	 *
	 * @param whereCondition
	 * @return
	 */
	@Override
	public List<BpmDlyPrst> getDashboardSparklinePast7DayDataset(WhereCondition whereCondition) {
		return dashboardRepository.getDashboardSparklinePast7DayDataset(whereCondition);
	}

	/**
	 * 경영실적 데이터 처리.
	 *
	 * @param dashboardData
	 * @return 배치 실행 파일 이름
	 */
	@Override
	@Transactional
	public String saveBpmIdxCttMappInfoPerDay(DashboardData dashboardData) {
		String trmsFlNm = "SKP_MGT_DAY_" + dashboardData.getSvcId() + "_"
				+ DateUtil.getCurrentDate(Constants.DATE_24TIME_FORMAT) + "_TM";
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		// TMP 데이터 삭제.
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(dashboardData.getSvcId());
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);
		BpmIdxCttMappInfo cttMappInfo;
		for (BpmDlyPrst bpmDlyPrst : dashboardData.getDataSet()) {
			cttMappInfo = new BpmIdxCttMappInfo();
			cttMappInfo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			cttMappInfo.setSvcId(dashboardData.getSvcId());
			cttMappInfo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			cttMappInfo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			cttMappInfo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			cttMappInfo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			cttMappInfo.setLnkgCyclCd("D");
			cttMappInfo.setAuditId("API");
			handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

			tmpVo = new BpmIdxCttMappInfoTmp();
			tmpVo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			tmpVo.setSvcId(dashboardData.getSvcId());
			tmpVo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			tmpVo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			tmpVo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			tmpVo.setIdxCttCdDesc(bpmDlyPrst.getIdxCttCdVal());
			tmpVo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			tmpVo.setLnkgCyclCd("DAY");
			tmpVo.setIdxClCdGrpNm(bpmDlyPrst.getIdxClCdGrpNm());
			tmpVo.setIdxClCdVal(bpmDlyPrst.getIdxClCdVal());
			tmpVo.setAuditId("API");
			tmpVo.setTrmsFlNm(trmsFlNm);
			handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
		}
		return trmsFlNm;
	}

	@Override
	@Transactional
	public String saveBpmIdxCttMappInfoPerMonth(DashboardData dashboardData) {
		String trmsFlNm = "SKP_MGT_MTH_" + dashboardData.getSvcId() + "_"
				+ DateUtil.getCurrentDate(Constants.DATE_24TIME_FORMAT) + "_TM";
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(dashboardData.getSvcId());
		// 기존 데이터 delete
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);
		for (BpmDlyPrst bpmDlyPrst : dashboardData.getDataSet()) {
			cttMappInfo = new BpmIdxCttMappInfo();
			cttMappInfo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			cttMappInfo.setSvcId(dashboardData.getSvcId());
			cttMappInfo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			cttMappInfo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			cttMappInfo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			cttMappInfo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			cttMappInfo.setLnkgCyclCd("M");
			cttMappInfo.setAuditId("API");
			handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);
			tmpVo = new BpmIdxCttMappInfoTmp();
			tmpVo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			tmpVo.setSvcId(dashboardData.getSvcId());
			tmpVo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			tmpVo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			tmpVo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			tmpVo.setIdxCttCdDesc(bpmDlyPrst.getIdxCttCdVal());
			tmpVo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			tmpVo.setLnkgCyclCd("MTH");
			tmpVo.setIdxClCdGrpNm(bpmDlyPrst.getIdxClCdGrpNm());
			tmpVo.setIdxClCdVal(bpmDlyPrst.getIdxClCdVal());
			tmpVo.setAuditId("API");
			tmpVo.setTrmsFlNm(trmsFlNm);
			handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
		}
		return trmsFlNm;
	}

	@Override
	@Transactional
	public String saveBpmIdxCttMappInfoPerWeek(DashboardData dashboardData) {
		String trmsFlNm = "SKP_MGT_WEK_" + dashboardData.getSvcId() + "_"
				+ DateUtil.getCurrentDate(Constants.DATE_24TIME_FORMAT) + "_TM";
		BpmIdxCttMappInfo cttMappInfo;
		BpmIdxCttMappInfoTmp tmpVo = new BpmIdxCttMappInfoTmp();
		// TMP 데이터 삭제.
		tmpVo.setTrmsFlNm(trmsFlNm);
		tmpVo.setSvcId(dashboardData.getSvcId());
		handInputRepository.deleteBpmIdxCttMappInfoTmp(tmpVo);
		for (BpmDlyPrst bpmDlyPrst : dashboardData.getDataSet()) {
			cttMappInfo = new BpmIdxCttMappInfo();
			cttMappInfo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			cttMappInfo.setSvcId(dashboardData.getSvcId());
			cttMappInfo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			cttMappInfo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			cttMappInfo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			cttMappInfo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			cttMappInfo.setLnkgCyclCd("W");
			cttMappInfo.setAuditId("API");
			handInputRepository.saveBpmIdxCttMappInfo(cttMappInfo);

			tmpVo = new BpmIdxCttMappInfoTmp();
			tmpVo.setMappStrdDt(bpmDlyPrst.getDlyStrdDt());
			tmpVo.setSvcId(dashboardData.getSvcId());
			tmpVo.setIdxClGrpCd(bpmDlyPrst.getIdxClGrpCd());
			tmpVo.setIdxClCd(bpmDlyPrst.getIdxClCd());
			tmpVo.setIdxCttCd(bpmDlyPrst.getIdxCttCd());
			tmpVo.setIdxCttCdDesc(bpmDlyPrst.getIdxCttCdVal());
			tmpVo.setRsltVal(bpmDlyPrst.getDlyRsltVal());
			tmpVo.setLnkgCyclCd("WEK");
			tmpVo.setIdxClCdGrpNm(bpmDlyPrst.getIdxClCdGrpNm());
			tmpVo.setIdxClCdVal(bpmDlyPrst.getIdxClCdVal());
			tmpVo.setAuditId("API");
			tmpVo.setTrmsFlNm(trmsFlNm);
			handInputRepository.insertBpmIdxCttMappInfoTmp(tmpVo);
		}
		return trmsFlNm;
	}
}
