package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;

/**
 * The ComRoleMenu class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComRoleMenu implements Serializable {
	private static final long serialVersionUID = -459824625662192071L;
	private Long roleId;
	private int count;
	private Long menuId;
	private String menuName;
	private String name;
	private String roleIdroleName;
	private String auditId;
	private String auditDtm;
}
