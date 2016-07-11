package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by pepsi on 2014. 7. 24..
 */
@Data
public class BpmMgntRsltBatInfoRgst implements Serializable {
  private static final long serialVersionUID = -1881189618976606085L;
  private String svcNm; /* 서비스 이름 */
  private String rmk; /* 비고 */
  private String dispStrdCd; /* 표시기준코드 */
  private String mthObjYn; /* 월대상여부 */
  private String wkObjYn; /* 주대상여부 */
  private String dayObjYn; /* 일대상여부 */
  private String mthRcvExptTm; /* 월수신예상시각 */
  private String mthRcvExptDay; /* 월수신예상일 */
  private long mthRcvItmCnt; /* 월수신항목건수 */
  private String wkRcvExptTm; /* 주수신예상시각 */
  private String wkRcvExptDowCd; /* 주수신예상요일코드 */
  private String wkRcvExptDowCdNm;
  private long wkRcvExptCnt; /* 주수신예상건수 */
  private String dayRcvExptTm; /* 일수신예상시각 */
  private long dayRcvItmCnt; /* 일수신항목건수 */
  private String auditDtm; /* 최종변경일시 */
  private String auditId; /* 최종변경자ID */
  private long svcId; /* 서비스ID */
}
