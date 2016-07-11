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
public class ObsPresntSumRpt extends BasePivot implements Serializable {
	private static final long serialVersionUID = 8910411301481520570L;
	private String stndrdDt; // 기준일자 VARCHAR2(8 BYTE)
	private String totYn; // 전체여부 VARCHAR2(1 BYTE)
	private String cylIndCd; // 주기구분코드 VARCHAR2(1 BYTE)
	private BigDecimal presntSndAcmCustCnt = BigDecimal.ZERO; // 선물발송누적고객수 NUMBER(15,0)
	private BigDecimal presntSndCustCnt = BigDecimal.ZERO; // 선물발송고객수 NUMBER(15,0)
	private BigDecimal presntSndTrCnt = BigDecimal.ZERO; // 선불발송TR건수 NUMBER(15,0)
	private BigDecimal presntSndPntSum = BigDecimal.ZERO; // 선물발송포인트합계 NUMBER(15,0)
	private BigDecimal presntSndAcmPntSum = BigDecimal.ZERO; // 선물발송누적포인트합계 NUMBER(15,0)
	private BigDecimal presntRcvAcmCustCnt = BigDecimal.ZERO; // 선물수신누적고객수 NUMBER(15,0)
	private BigDecimal presntRcvCustCnt = BigDecimal.ZERO; // 선물수신고객수 NUMBER(15,0)
	private BigDecimal presntRcvTrCnt = BigDecimal.ZERO; // 선물수신TR건수 NUMBER(15,0)
	private BigDecimal presntRcvPntSum = BigDecimal.ZERO; // 선물수신포인트합계 NUMBER(15,0)
	private BigDecimal entrRqstCustCnt = BigDecimal.ZERO; // 가입신청고객수 NUMBER(15,0)
	private BigDecimal yachvCustCnt = BigDecimal.ZERO; // 유실적고객수 NUMBER(15,0)
	private BigDecimal yachvAcmCustCnt = BigDecimal.ZERO; // 유실적누적고객수 NUMBER(15,0)
	private BigDecimal ownPntUseAcmCustCnt = BigDecimal.ZERO; // 본인포인트사용누적고객수 NUMBER(15,0)
	private BigDecimal ownPntUseCustCnt = BigDecimal.ZERO; // 본인포인트사용고객수 NUMBER(15,0)
	private BigDecimal ownPntUseTrCnt = BigDecimal.ZERO; // 본인포인트사용TR건수 NUMBER(15,0)
	private BigDecimal ownPntUsePntSum = BigDecimal.ZERO; // 본인포인트사용포인트합계 NUMBER(15,0)
	private BigDecimal ownPntUseAcmPntSum = BigDecimal.ZERO; // 본인포인트사용누적포인트합계 NUMBER(15,0)
	private BigDecimal othPntUseAcmCustCnt = BigDecimal.ZERO; // 타인포인트사용누적고객수 NUMBER(15,0)
	private BigDecimal othPntUseCustCnt = BigDecimal.ZERO; // 타인포인트사용고객수 NUMBER(15,0)
	private BigDecimal othPntUseTrCnt = BigDecimal.ZERO; // 타인포인트사용TR건수 NUMBER(15,0)
	private BigDecimal othPntUsePntSum = BigDecimal.ZERO; // 타인포인트사용포인트합계 NUMBER(15,0)
	private BigDecimal othPntUseAcmPntSum = BigDecimal.ZERO; // 타인포인트사용누적포인트합계 NUMBER(15,0)
	private String loadDt; // 적재일자 VARCHAR2(8 BYTE)

}
