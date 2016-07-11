package com.skplanet.bisportal.model.ocb;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class ObsNewOcbSegRpt extends BasePivot implements Serializable {
	private static final long serialVersionUID = 1898118803606760180L;
	private String stndrdYm; // 기준년월 VARCHAR2(6 BYTE)
	private String rptIndCd; // 보고서구분코드 VARCHAR2(10 BYTE)
	private String rptIndNm; // 보고서구분명 VARCHAR2(50 BYTE)
	private BigDecimal totCustCnt; // 전체고객수 NUMBER(15,0)
	private BigDecimal totTrCnt; // 전체TR건수 NUMBER(15,0)
	private BigDecimal totPfSum; // 전체PF합계 NUMBER(15,0)
	private BigDecimal rsrvCustCnt; // 적립고객수 NUMBER(15,0)
	private BigDecimal rsrvTrCnt; // 적립TR건수 NUMBER(15,0)
	private BigDecimal rsrvPntSum; // 적립포인트합계 NUMBER(15,0)
	private BigDecimal useCustCnt; // 사용고객수 NUMBER(15,0)
	private BigDecimal useTrCnt; // 사용TR건수 NUMBER(15,0)
	private BigDecimal usePntSum; // 사용포인트합계 NUMBER(15,0)
	private String loadDt; // 적재일자 VARCHAR2(8 BYTE)

}
