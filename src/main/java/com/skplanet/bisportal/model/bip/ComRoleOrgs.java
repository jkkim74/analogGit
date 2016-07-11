package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The ComRoleOrgs class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComRoleOrgs implements Serializable {
	private static final long serialVersionUID = -4857941952674869619L;
	private List<ComRoleOrg> roleCounts;
	private List<ComRoleOrg> roleOrgs;
}
