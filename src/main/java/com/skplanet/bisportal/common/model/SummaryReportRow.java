package com.skplanet.bisportal.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The SummaryReport Domain model.
 * 
 * @author sjune
 */
@Data
public class SummaryReportRow implements Serializable {
	private static final long serialVersionUID = 3755007074504431653L;
	/** 지표값 추이 list */
	private List<BigDecimal> periodMeasureValues;

	/** 지표값 추이 map */
	private Map<String, BigDecimal> periodMeasureMap;

	/** 서비스명 */
	private String svcNm;

	/** 지표 */
	private String measure;

	/** 기준일에 대한 지표 값 */
	private BigDecimal basicMeasureValue;

	/** 1일 전 지표 값 */
	private BigDecimal oneDayAgoMeasureValue;

	/** 1주 전 지표 값 */
	private BigDecimal oneWeekAgoMeasureValue;

	/** 1달 전 지표 값 */
	private BigDecimal oneMonthAgoMeasureValue;

	/** 1년 전 지표 값 */
	private BigDecimal oneYearAgoMeasureValue;

	/**
	 * Constructor of SummaryReportRow
	 * 
	 * @param measure
	 */
	public SummaryReportRow(String measure) {
		this.measure = measure;
		this.periodMeasureValues = Lists.newArrayList();
		this.periodMeasureMap = Maps.newHashMap();
	}
}
