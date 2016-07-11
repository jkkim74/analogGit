package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * Created by pepsi on 2014. 8. 4..
 */
@Data
public class BpmIdxCttMappInfoTmp implements Serializable {
	private static final long serialVersionUID = -3558364851390079852L;
	/** IDX_CTT_CD */
	private String idxCttCd;

	/** IDX_CL_CD */
	private String idxClCd;

	/** IDX_CL_GRP_CD */
	private String idxClGrpCd;

	/** SVC_ID */
	private BigDecimal svcId;

	/** MAPP_STRD_DT */
	private String mappStrdDt;

	/** AUDIT_ID */
	private String auditId;

	/** AUDIT_DTM */
	private Date auditDtm;

	/** RSLT_VAL */
	private BigDecimal rsltVal;

	/** IDX_CTT_CD_DESC */
	private String idxCttCdDesc;

	/** TRMS_FL_NM */
	private String trmsFlNm;

	/** LNKG_CYCL_CD */
	private String lnkgCyclCd;

	/** IDX_CL_CD_GRP_NM */
	private String idxClCdGrpNm;

	/** IDX_CL_CD_VAL */
	private String idxClCdVal;
}
