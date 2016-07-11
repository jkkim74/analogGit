package com.skplanet.bisportal.common.model;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The HelpDeskRequest class.
 *
 * @author cookatrice
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpDeskRequest implements Serializable {
	private static final long serialVersionUID = -4701268961736472035L;
	private String[] hdSearchCategory; // 분류
    private String hdSearchCategorySingle; //분류 싱글
	private String hdSearchType; // 타입
	private String hdSearchString; // 검색어
	private String hdTitle; // 작업제목
	private String hdRegistrator; // 작업등록자
	private String hdManager; // 작업담당자
	private String hdStatus; // 작업상태
	private String hdRegistrationStartDate; // 작업등록 시작일자
	private String hdRegistrationEndDate; // 작업등록 종료일자
	private String hdCompleteStartDate; // 작업완료 시작일자
	private String hdCompleteEndDate; // 작업완료 종료일자

	private String id;
	private String startIndex;
	private String endIndex;

	private String boardType; // N, F, Q, W
	private String navigator; // previous, next

	//infinite scroll value
	private int page;
	private int pageSize;
}
