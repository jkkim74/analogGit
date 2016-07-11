package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 5. 13..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table : OBS_D_MBRENT_STA, PRIMARY KEY : STD_DT, SEX_IND_CD, AGE_LGRP_CD
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsMbrentSta extends BasePivot implements Serializable {
	private static final long serialVersionUID = 7162644336875443246L;
	private String stdDt; // STD_DT  기준일자 VARCHAR2(8) N
    private String stdYm; // STD_YM  기준일자 VARCHAR2(6) N
	private String sexIndCd; // SEX_IND_CD  성별 구분 코드 VARCHAR2(2) N
	private String ageLgrpCd; // AGE_LGRP_CD  연령 대분류 코드 VARCHAR2(2) N
	private Long newEntrCnt; // NEW_ENTR_CNT  신규가입자수 NUMBER(15) Y
    private Long acmEntrCnt; // ACM_ENTR_CNT  누적가입자수 NUMBER(15) Y
    private String sexIndNm;
    private String ageLgrpNm;
}
