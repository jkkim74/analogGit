package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cookatrice on 2014. 5. 8..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table : OBS_D_VST_SEX_AGE_STA // PRIMARY KEY : STD_DT, POC_IND_CD, SEX_IND_CD, AGE_LGRP_CD
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstSexAgeSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -1478198376590845703L;
	private String stdYm;// 기준일자 VARCHAR2(8) N
	private String stdDt;// 기준일자 VARCHAR2(8) N
	private String pocIndCd;// POC 구분 코드 VARCHAR2(2) N
	private String sexIndCd;// 성별 구분 코드 VARCHAR2(2) N
	private String ageLgrpCd;// 연령 대분류 코드 VARCHAR2(2) N
	private BigDecimal uv;// UV NUMBER(15) Y
	private BigDecimal lv;// LV NUMBER(15) Y
	private BigDecimal pv;// PV NUMBER(15) Y
	private BigDecimal rv;// RV NUMBER(15) Y
	private BigDecimal vstCnt;// Visits NUMBER(15) Y
	private BigDecimal timeSptFVst;// 체류시간 DOUBLE PRECISION Y
	private BigDecimal buncRate;// Bounce rate DOUBLE PRECISION Y
    private String sexIndNm;
    private String ageLgrpNm;
}
