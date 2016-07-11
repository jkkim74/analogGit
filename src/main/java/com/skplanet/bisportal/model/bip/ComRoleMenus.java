package com.skplanet.bisportal.model.bip;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The ComRoleMenus class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
public class ComRoleMenus implements Serializable {
	private static final long serialVersionUID = -2542795950602393843L;
	private List<ComRoleMenu> roleCounts;
	private List<ComRoleMenu> roleMenus;
	private List<ComRoleMenu> menuCounts;
	private List<ComRoleMenu> menuRoles;
}
