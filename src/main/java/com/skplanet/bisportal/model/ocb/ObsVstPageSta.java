package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 방문자페이지 model.
 * 
 * @author sjune
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstPageSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -4016312972889743292L;
	private String stdDt; // STD_DT 기준일자 VARCHAR2(8)
	private String stdYm; // STD_YMD 기준일자 VARCHAR2(6)
	private String pageId; // PAGE_ID 페이지 아이디 VARCHAR2(20)
	private String lngCd; // LNG_CD 언어 코드 VARCHAR2(2)
	private long uv; // UV UV NUMBER(15)
	private long lv; // LV LV NUMBER(15)
	private long pv; // PV PV NUMBER(15)
	private long vstCnt; // VST_CNT Visits NUMBER(15)
	private long timeSptFVst; // TIME_SPT_F_VST 체류 기간 NUMBER(10)
    private String depth1Nm;
    private String depth2Nm;
    private String depth3Nm;
    private String depth4Nm;
    private String depth5Nm;
}
