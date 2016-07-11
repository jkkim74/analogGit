package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * The OcbDayVisitStc class.
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table : OCB_DAY_VISIT_STC, PRIMARY KEY : BASE_DT,POC_TYPE_CD
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class OcbDayVisitStc extends BasePivot implements Serializable {
	private static final long serialVersionUID = 130012171334731102L;
	// PRIMARY KEY (BASE_DT,POC_TYPE_CD)
	private String baseDt; // 기준일자, VARCHAR2(8) NOT NULL
	private String pocTypeCd; // POC구분코드, VARCHAR2(2) NOT NULL
	private int uvCnt; // UV, NUMBER(15)
	private int lvCnt; // LV, NUMBER(15)
	private int newVisitCnt; // 신규방문자수, NUMBER(15)
	private int reVisitCnt; // 재방문자수, NUMBER(15)
	private int totalRecordCount;
}
