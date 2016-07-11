package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by pepsi on 2014. 7. 24..
 */
@Data
public class BpmMgntRsltBatOpPrst implements Serializable {
  private static final long serialVersionUID = -9067025100885334635L;
  private String svcNm; /* 서비스 이름 */
  private String insClCd; /* 입력구분코드 */
  private String trmsFlNm; /* 전송파일명 */
  private long exptCnt;
  private long dataCnt; /* 데이터건수 */
  private long rcvItmCnt; /* 수신항목건수 */
  private long rcvExptItmCnt; /* 수신예상항목건수 */
  private String errCtt; /* 오류내용 */
  private String errYn; /* 오류여부 */
  private long operExecId; /* 작업실행ID */
  private String rcvYn; /* 수신여부 */
  private String rcvExptTm; /* 수신예상시각 */
  private String auditDtm; /* 최종변경일시 */
  private String auditId; /* 최종변경자ID */
  private String lnkgCyclCd; /* 연동주기코드 */
  private long svcId; /* 서비스ID */
  private String stcStaDt; /* 통계시점일자 */
  private long seq; /* 순번 */
  private String logId; /* 로그ID */
  private String status;
  private String errDtl;
  private String exptCd;
  private String prtPrtySeq;
  private String dispStrdCd;
  private String rmk;
}
