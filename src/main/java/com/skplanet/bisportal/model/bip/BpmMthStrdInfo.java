package com.skplanet.bisportal.model.bip;

import java.io.Serializable;

import lombok.Data;

/**
 * The BpmMthStrdInfo class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmMthStrdInfo implements Serializable {
  private static final long serialVersionUID = 6454569932284996402L;
  private String mthEndDt; /* 월종료일자 */
  private String mthStaDt; /* 월시작일자 */
  private long cdSeq; /* 코드순번 */
  private String mthStrdVal; /* 월기준값 */
  private String auditDtm; /* 최종변경일시 */
  private String auditId; /* 최종변경자ID */
  private String mthStcStrdYm; /* 월간통계기준년월 */
}
