package com.skplanet.bisportal.model.bip;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * The 권한 요청 공통 class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest implements Serializable {
	private static final long serialVersionUID = 8587257615404667875L;
	private String username;
	private String searchString;
	private Long roleId;
	private String loginId;
	private List<String> loginIds;
	private List<String> orgCds;
	private List<String> roleIds;
	private List<String> menuIds;
	private List<ComRole> comRoles;
	private List<ComRoleUser> comRoleUsers;
	private List<ComUserMenu> comUserMenus;
	private List<ComRoleOrg> comRoleOrgs;
	private List<Menu> menus;
	private List<String> addRoles;
	private List<String> removeRoles;
	private List<String> addMenus;
	private List<String> removeMenus;
}
