package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 5. 14..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstOsSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 8615057375551233880L;
	private String stdDt;   // STD_DT 기준일자 VARCHAR2(8) N
    private String stdYm;   //STD_YM 기준일자 VARCHAR2(6) N
    private String pocIndCd;    //POC_IND_CD POC 구분 코드 VARCHAR2(2) N
    private String osNm;    //OS_NM OS 명 VARCHAR2(30) N
    private String osVer;   //OS_VER OS 버전 VARCHAR2(20) N
    private Long uv;    //UV UV NUMBER(15) Y
    private Long lv;    //LV LV NUMBER(15) Y
    private Long pv;    //PV PV NUMBER(15) Y
    private Long vstCnt;    //VST_CNT Visits NUMBER(15) Y
}
