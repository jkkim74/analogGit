package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The ComRoleOrg class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComRoleOrg implements Serializable {
	private static final long serialVersionUID = 7693530261005679815L;
	private long roleId;
	private int count;
	private String orgCd;
	private String orgNm;
	private String name;
	private String orgCdOrgNm;
	private String auditId;
	private String auditDtm;
}
