package com.skplanet.bisportal.model.bip;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

/**
 * @author cookatrice
 */
@Data
public class DaaEwmaStatDaily implements Serializable {
	private static final long serialVersionUID = -3880196820499788291L;
	private String statDate; // 기준일자 CHAR(8 BYTE)
	private String svcId; // 서비스코드 VARCHAR2(10 BYTE)
	private String kpiClassId; // 지표분류코드 VARCHAR2(50 BYTE)
	private String kpiId; // 지표코드 VARCHAR2(10 BYTE)
	private String bossSvcCd; // 서비스 코드
	private BigDecimal kpiValue = BigDecimal.ZERO; // 지표기준수 NUMBER(14,2)
	private BigDecimal ewmaValue = BigDecimal.ZERO; // EWMA기준수 NUMBER(14,2)
	private BigDecimal ewmaAvg = BigDecimal.ZERO; // EWMA 평균 NUMBER(14,2)
	private BigDecimal ewmaStdev = BigDecimal.ZERO; // EWMA 표준편차 NUMBER(14,2)
	private BigDecimal lclValue = BigDecimal.ZERO; // 하한관리구간 NUMBER(14,2)
	private BigDecimal uclValue = BigDecimal.ZERO; // 상한관리구간 NUMBER(14,2)
	private String alarmType; // ALARM구분코드 CHAR(2 BYTE)
	private String regDtm; // 작업일시 DATE
}
