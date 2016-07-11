package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * The BpmIdxCttMappInfo class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class BpmIdxCttMappInfo implements Serializable {
	private static final long serialVersionUID = 7417558283052636114L;
	/** 상태플러그 */
	private String flag;

	/** STRD_DT */
	private String mappStrdDt;

	/** SVC_ID */
	private BigDecimal svcId;

	/** IDX_CL_GRP_CD */
	private String idxClGrpCd;

	/** IDX_CL_CD */
	private String idxClCd;

	/** IDX_CTT_CD */
	private String idxCttCd;
	/** IDX_CTT_CD_VAL */
	private String idxCttCdVal;

	/** AUDIT_ID */
	private String auditId;

	/** AUDIT_DTM */
	private Date auditDtm;

	/** RSLT_VAL1 */
	private BigDecimal rsltVal;

	/** 조회조건 */
	private BigDecimal searchDate;

	/** 연동주기 */
	private String lnkgCyclCd;

	private String idxClCdGrpNm;
	private String idxClCdVal;
}
