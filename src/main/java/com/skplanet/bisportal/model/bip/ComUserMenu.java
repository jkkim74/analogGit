package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The ComRoleUser class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComUserMenu implements Serializable {
	private static final long serialVersionUID = 3283571043060719033L;
	private String loginId;
	private long menuId;
	private String createYn;
	private String readYn;
	private String updateYn;
	private String deleteYn;
	private String auditId;
	private String auditDtm;
}
