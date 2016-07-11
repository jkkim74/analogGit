package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by cookatrice on 2014. 5. 8..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table : OBS_D_VST_DVC_MDL_STA // PRIMARY KEY : STD_DT, POC_IND_CD, DVC_MDL
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstDvcMdlSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = -598973479730181913L;
	private String stdDt;   // STD_DT 기준일자 VARCHAR2(8) N
	private String pocIndCd;    //POC_IND_CD POC 구분 코드 VARCHAR2(2) N
	private String dvcMdl;  //DVC_MDL 단말 모델 VARCHAR2(50) N
	private Long uv;    //UV UV NUMBER(15) Y
	private Long lv;    //LV LV NUMBER(15) Y
	private Long pv;    //PV PV NUMBER(15) Y
	private Long vstCnt;    //VST_CNT Visits NUMBER(15) Y

}
