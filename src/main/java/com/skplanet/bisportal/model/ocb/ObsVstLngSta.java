package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 방문자(언어) model.
 * 
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstLngSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 455387440443817167L;
	private String stdDt; // STD_DT 기준일자 VARCHAR2(8) N
	private String stdYm; // STD_YMD 기준일자 VARCHAR2(6) N
	private String pocIndCd; // POC_IND_CD POC 구분 코드 VARCHAR2(2) N
	private String lngCd; // LNG_CD 언어 코드 VARCHAR2(2) N
	private long uv; // UV UV NUMBER(15) Y
	private long lv; // LV LV NUMBER(15) Y
	private long pv; // PV PV NUMBER(15) Y
	private long vstCnt; // VST_CNT Visits NUMBER(15) Y
}
