package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The ComDept class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComDept implements Serializable {
	private static final long serialVersionUID = -256583368531745472L;
	private String auditDtm;     
	private String auditId;     
	private String actvnYn;     
	private String aplyEndDt;     
	private String aplyStaDt;     
	private String tsosok;     
	private String sosok;
	private String orgClCd;     
	private String supOrgNm;     
	private String supOrgCd;     
	private String levelcd;     
	private String orgPrtSeq;     
	private String dirLoginid;     
	private String lowyn;
	private String orgAbbrNm;     
	private String orgNm;     
	private String orgCd;
	private String groupType;
	private String flag;
}
