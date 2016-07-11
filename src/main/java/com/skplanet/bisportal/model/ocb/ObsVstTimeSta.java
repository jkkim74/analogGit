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
 * @Desc Table : OBS_D_VST_TIME_STA // PRIMARY KEY : STD_DT, POC_IND_CD, HH
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsVstTimeSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 4279769456663666766L;
	private String stdDt;   //STD_DT 기준일자 VARCHAR2(8) N
    private String stdYm;   //STD_YM 기준일자 VARCHAR2(6) N
	private String pocIndCd;    //POC_IND_CD POC 구분 코드 VARCHAR2(2) N
	private String hh;  //HH 시간대 VARCHAR2(2) N
	private Long uv;    //UV UV NUMBER(15) Y
	private Long lv;    //LV LV NUMBER(15) Y
	private Long pv;    //PV PV NUMBER(15) Y
	private Long vstCnt;    //VST_CNT Visits NUMBER(15) Y
}
