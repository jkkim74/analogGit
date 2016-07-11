package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author cookatrice
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BpmDlyPrst implements Serializable {
	private static final long serialVersionUID = 4100155731697340058L;
	private String dlyStrdDt; // 일통계기준일자 VARCHAR2(8 BYTE)
	private Long svcId; // 서비스ID NUMBER(18,0)
	private String svcNm; // 서비스명
	private String idxClGrpCd; // 지표구분그룹코드 VARCHAR2(30 BYTE)
	private String idxClCd; // 지표구분코드 VARCHAR2(30 BYTE)
	private String idxCttCd; // 지표내용코드 VARCHAR2(30 BYTE)
	private String auditId; // 최종변경자ID VARCHAR2(30 BYTE)
	private String auditDtm; // 최종변경일시 DATE
	private BigDecimal dlyRsltVal; // 일통계실적값 NUMBER(20,5)
	private transient EisSvcComCd eisSvcComCd;
	private String idxClCdGrpNm;
	private String idxClCdVal;
	private String idxCttCdVal;
	private Long ordIdx;// 정렬순서
}
