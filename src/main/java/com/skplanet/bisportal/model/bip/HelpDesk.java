package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author cookatrice
 */
@Data
public class HelpDesk implements Serializable {
	private static final long serialVersionUID = -4341054684465578263L;
	private Long id; // ID NUMBER(20,0)
	private String boardType; // 게시판구분[공지사항-N,FAQ-F,Q&A-Q,작업요청-W] VARCHAR2(2 BYTE)
	private String category; // 분류(Q) VARCHAR2(10 BYTE)
	private String title; // 제목 VARCHAR2(100 BYTE)
	private String context; // 내용 VARCHAR2(2000 BYTE)
	private String password; // 비밀번호 VARCHAR2(30 BYTE)
	private Long hit; // 조회수 NUMBER(10,0)
	private Long recommendCount; // 추천수 NUMBER(10,0)
	private String writer; // 작성자 VARCHAR2(30 BYTE)
	private String writerId; // 작성자ID VARCHAR2(20 BYTE)
	private String writeDate; // 작성일자 CHAR(14 BYTE)
	private String writeIp; // 작성IP VARCHAR2(15 BYTE)
	private String updater; // 수정자 VARCHAR2(30 BYTE)
	private String updaterId; // 수정자ID VARCHAR2(20 BYTE)
	private String updateDate; // 수정일자 CHAR(14 BYTE)
	private String updateIp; // 수정IP VARCHAR2(15 BYTE)
	private String deleteYn; // 삭제여부 (Y/N) CHAR(1 BYTE)
	private String workRegistrator; // 작업등록자 VARCHAR2(30 BYTE)
	private String workManager; // 작업담당자 VARCHAR2(30 BYTE)
	private String workStatus; // 작업상태 VARCHAR2(10 BYTE)
	private String workRegistrationDate; // 작업등록일자 VARCHAR2(8 BYTE)
	private String workCompleteDate; // 작업완료일자 VARCHAR2(8 BYTE)
	private String replyCompletionYn; // 답변완료여부(Y/N) CHAR(1 BYTE)
	private String popupStartDate; // 팝업시작일자
	private String popupEndDate; // 팝업종료일자
	private String popupYn; // 팝업게시여부
	private String popupOrder; // 팝업순서

	private String newYn; // notice, FaQ새글 여부(7일기준 new 표시), 컬럼X
	private String replyCount; // Q&A 리플 수, 컬럼X
	private String categoryName; // Q&A 카테고리명, 컬럼X

	private Long rnum; // rownum
	private Long totalRecourdCount; // totalRecourdCount

	private String newItem; // 등록/수정 flag
	private List<HelpDeskFileUpload> attachFiles; // 첨부파일

}
