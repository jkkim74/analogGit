package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 방문자(해상도) model.
 *
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstRsltnSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 2937030118885489042L;
	private String stdDt; // STD_DT 기준일자 VARCHAR2(8) N
	private String stdYm; // STD_YMD 기준일자 VARCHAR2(6) N
	private String pocIndCd; // POC_IND_CD POC 구분 코드 VARCHAR2(2) N
	private String scrnRsltn; // SCRN_RSLTN 화면 해상도 VARCHAR2(30) N
	private Long uv; // UV UV NUMBER(15) Y
	private Long lv; // LV LV NUMBER(15) Y
	private Long pv; // PV PV NUMBER(15) Y
	private Long vstCnt; // VST_CNT Visits NUMBER(15) Y
}
