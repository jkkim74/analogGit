package com.skplanet.bisportal.common.model;

import lombok.Data;

import java.io.Serializable;

/**
 * The BasicDateWeekNumber class.
 * 
 * <pre>
 * 기준일에 해당하는 주차 정보를 가진 모델 클래스
 * </pre>
 *
 * @author sjune
 */
@Data
public class BasicDateWeekNumber implements Serializable {
	private static final long serialVersionUID = 8201290126193199414L;
	/** 기준일의 주차 */
	private String basic;
	/** 기준일의 주차 시작일 */
	private String basicStartDate;
	/** 기준일의 주차 종료일 */
	private String basicEndDate;

	/** 기준 1주전 주차 */
	private String oneWeekAgo;
	/** 기준 1주전 주차 시작일 */
	private String oneWeekAgoStartDate;
	/** 기준 1주전 주차 종료일 */
	private String oneWeekAgoEndDate;

	/** 기준 14주전 주차 */
	private String fourteenWeekAgo;

	/** 기준 1년전 주차 */
	private String oneYearAgo;
	/** 기준 1년전 주차 시작일 */
	private String oneYearAgoStartDate;
	/** 기준 1년전 주차 종료일 */
	private String oneYearAgoEndDate;

}
