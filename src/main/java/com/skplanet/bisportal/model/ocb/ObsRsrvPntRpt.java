package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.skplanet.bisportal.common.model.BasePivot;

/**
 * Created by cookatrice on 2014. 8. 11..
 * 
 * @author DongWoo-Ha (cookatrice@wiseeco.com)
 * 
 * @Desc Table
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ObsRsrvPntRpt extends BasePivot implements Serializable {
	private static final long serialVersionUID = -4987446481129306725L;
	private String stndrdDt; // 기준일자 VARCHAR2(8 BYTE)
	private String cylIndCd; // 주기구분코드 VARCHAR2(1 BYTE)
	private String rptIndCd; // 보고서구분코드 VARCHAR2(10 BYTE)
	private String rptIndNm; // 보고서구분명 VARCHAR2(100 BYTE)
	private String rptIndDetlCd; // 보고서구분상세코드 VARCHAR2(10 BYTE)
	private String rptIndDetlNm; // 보고서구분상세명 VARCHAR2(100 BYTE)
	private Long custCnt; // 고객수 NUMBER(15,0)
	private Long trCnt; // TR건수 NUMBER(15,0)
	private Long pntSum; // 포인트합계 NUMBER(15,0)
	private Long cmmsnSum; // 수수료합계 NUMBER(15,0)
	private String loadDt; // 적재일자 VARCHAR2(8 BYTE)

}
