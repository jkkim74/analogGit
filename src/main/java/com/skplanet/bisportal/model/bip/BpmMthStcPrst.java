package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author cookatrice
 */
@Data
public class BpmMthStcPrst implements Serializable {
    private static final long serialVersionUID = -7052039287381265827L;
    private String mthStcStrdYm; // 주별통계기준년월주 VARCHAR2(8 BYTE)
	private Long svcId; // 서비스ID NUMBER(18,0)
	private String idxClGrpCd; // 지표구분그룹코드 VARCHAR2(30 BYTE)
	private String idxClCd; // 지표구분코드 VARCHAR2(30 BYTE)
	private String idxCttCd; // 지표내용코드 VARCHAR2(30 BYTE)
	private String auditId; // 최종변경자ID VARCHAR2(30 BYTE)
	private String auditDtm; // 최종변경일시 DATE
	private BigDecimal mthStcRsltVal = BigDecimal.ZERO; // 월간통계실적값 NUMBER(20,5)
	private String stcStaEndDt; // 통계시점종료일자 VARCHAR2(8 BYTE)
    private transient EisSvcComCd eisSvcComCd;
    private String idxClCdGrpNm;
    private String idxClCdVal;
    private String idxCttCdVal;
}
