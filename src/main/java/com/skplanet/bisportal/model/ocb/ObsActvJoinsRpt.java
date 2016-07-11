package com.skplanet.bisportal.model.ocb;

import com.skplanet.bisportal.common.model.BasePivot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by cookatrice on 2014. 8. 11..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsActvJoinsRpt extends BasePivot implements Serializable {
    private static final long serialVersionUID = 2842129260534902131L;
    private String stndrdYm; // 기준년월 VARCHAR2(6 BYTE)
	private String rptIndCd; // 보고서구분코드 VARCHAR2(10 BYTE)
	private String rptIndNm; // 보고서구분명 VARCHAR2(50 BYTE)
	private String rptIndDetlCd; // 보고서구분상세코드 VARCHAR2(10 BYTE)
	private String rptIndDetlNm; // 보고서구분상세명 VARCHAR2(50 BYTE)
	private BigDecimal actvJoinsCnt; // 활성화가맹점수 NUMBER(15,0)
	private String loadDt; // 적재일자 VARCHAR2(8 BYTE)

}
