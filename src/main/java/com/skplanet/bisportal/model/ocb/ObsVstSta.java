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
 * @Desc Table : OBS_D_VST_STA // PRIMARY KEY : STD_DT, POC_IND_CD, UV, LV, PV, RV
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 661778259371978488L;
	private String stdDt;// 기준일자 VARCHAR2(8) N
    private String stdYm;// 기준일자 VARCHAR2(6) N
	private String pocIndCd;// POC 구분 코드 VARCHAR2(2) N
	private BigDecimal uv;// UV NUMBER(15) Y
	private BigDecimal lv;// LV NUMBER(15) Y
	private BigDecimal pv;// PV NUMBER(15) Y
	private BigDecimal rv;// RV NUMBER(15) Y
	private BigDecimal vstCnt;// Visits NUMBER(15) Y
	private BigDecimal timeSptFVst;// 체류시간 DOUBLE PRECISION Y
	private BigDecimal buncRate;// Bounce rate DOUBLE PRECISION Y

}
