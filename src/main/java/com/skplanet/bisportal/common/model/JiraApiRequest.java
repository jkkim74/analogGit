package com.skplanet.bisportal.common.model;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by cookatrice on 2014. 11. 25..
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraApiRequest implements Serializable {
	private static final long serialVersionUID = -5079932646048194470L;
	private String issueType; // 이슈코드
	private String component; // 서비스종류 (ex.ocb, Hoppin)
	private String summary; // 제목
	private String description; // 내용
	private String[] attachFiles;// 첨부파일
	private String dueDate; // 완료희망일자
	private String applicant; // 작성자
	private String approval; // 결재자
	private String[] reference; // 참조자

	// helpdesk work request
	private String id;
	private String name;

	private String loginId;
	private String userNm;
	private String jbcharge;
	private String orgCd;
	private String sosok;

	private String status; // 상태
	private String createDateTotal; // 작업등록일자 전체
	private String createStartDate;// 작업등록일자 시작
	private String createEndDate;// 작업등록일자 종료
	private String dueDateTotal; // 완료희망일자 전체
	private String dueStartDate;// 완료희망일자 시작
	private String dueEndDate;// 완료희망일자 종료
}
