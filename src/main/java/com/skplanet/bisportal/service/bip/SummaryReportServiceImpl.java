package com.skplanet.bisportal.service.bip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skplanet.bisportal.common.exception.BizException;
import com.skplanet.bisportal.common.model.BasicDateWeekNumber;
import com.skplanet.bisportal.common.model.Condition;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.SummaryReportRow;
import com.skplanet.bisportal.common.model.SummaryReportRowMap;
import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.common.utils.ObjectMapperFactory;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.BpmDlyPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmMthStcPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmWkStcPrstRepository;
import com.skplanet.bisportal.repository.bip.EisSvcComCdRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.List;

import static com.skplanet.bisportal.common.utils.DateUtil.*;

/**
 * Created by sjune on 2014-07-10.
 * 
 * @author sjune
 */
@Service
@Slf4j
public class SummaryReportServiceImpl implements SummaryReportService {
	private final ObjectMapper objectMapper = ObjectMapperFactory.get();
	@Autowired
	private EisSvcComCdRepository eisSvcComCdRepository;
	@Autowired
	private BpmDlyPrstRepository bpmDlyPrstRepository;
	@Autowired
	private BpmWkStcPrstRepository bpmWkStcPrstRepository;
	@Autowired
	private BpmMthStcPrstRepository bpmMthStcPrstRepository;
	@Autowired
	private CommonCodeService commonCodeServiceImpl;

	@Override
	public List<EisSvcComCd> getMeasuresByServiceId(Long serviceId) {
		return eisSvcComCdRepository.getComCdsBySvcId(serviceId);
	}

	@Override
	public List<SummaryReportRow> getSummaryDailyResult(Condition condition) throws Exception {
		throwBizExceptionIfWhereConditionsIsNull(condition);
		List<BpmDlyPrst> summaryDailyResult = bpmDlyPrstRepository.getSummaryDailyResult(condition);
		String basicDate = condition.getSearchDate();
		SummaryReportRowMap rowMap = new SummaryReportRowMap();
		for (BpmDlyPrst bpmDlyPrst : summaryDailyResult) {
			String measure = bpmDlyPrst.getEisSvcComCd().getIdxCttCdVal();
			BigDecimal measureValue = bpmDlyPrst.getDlyRsltVal();
			String measureDate = bpmDlyPrst.getDlyStrdDt();
			SummaryReportRow row = rowMap.addOrGetRow(measure);
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
	public List<SummaryReportRow> getSummaryWeeklyResult(Condition condition) throws Exception {
		throwBizExceptionIfWhereConditionsIsNull(condition);
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
	public List<SummaryReportRow> getSummaryMonthlyResult(Condition condition) throws Exception {
		throwBizExceptionIfWhereConditionsIsNull(condition);
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

	@Override
	public ModelAndView getSummaryReportView(JqGridRequest jqGridRequest) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("summaryExcelView");
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
				modelAndView.addObject("summaryData", this.getSummaryWeeklyResult(condition));
			} else if (isMonth(jqGridRequest.getDateType())) {
				condition.setSearchDate(jqGridRequest.getBasicDate().substring(0, 6));
				whereCondition.setStartDate(addMonths(jqGridRequest.getBasicDate(), -13));
				whereCondition.setEndDate(jqGridRequest.getBasicDate());
				SummaryHeader<BpmMthStrdInfo> mthHeader = new SummaryHeader<BpmMthStrdInfo>();
				mthHeader.setPeriods(bpmMthStcPrstRepository.getBpmMthStrdInfos(whereCondition));
				modelAndView.addObject("periodData", mthHeader);
				modelAndView.addObject("summaryData", this.getSummaryMonthlyResult(condition));
			} else {
				condition.setSearchDate(jqGridRequest.getBasicDate());
				whereCondition.setStartDate(addDays(jqGridRequest.getBasicDate(), -7));
				whereCondition.setEndDate(jqGridRequest.getBasicDate());
				SummaryHeader<BpmDayStrdInfo> dayHeader = new SummaryHeader<BpmDayStrdInfo>();
				dayHeader.setPeriods(bpmDlyPrstRepository.getBpmDayStrdInfos(whereCondition));
				modelAndView.addObject("periodData", dayHeader);
				modelAndView.addObject("summaryData", this.getSummaryDailyResult(condition));
			}
		} catch (Exception e) {
			log.error("getSummaryReportView {}", e);
			throw new Exception("report view error!");
		}
		return modelAndView;
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

	private void throwBizExceptionIfWhereConditionsIsNull(Condition condition) {
		if (CollectionUtils.isEmpty(condition.getWhereConditions())) {
			throw new BizException("The whereConditions is empty.");
		}
	}
}
