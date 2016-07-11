package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.model.acl.UserSignMngmt;
import com.skplanet.bisportal.model.bip.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The UserService interface(BipUser 처리 인터페이스).
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
public interface UserService {
	BipUser login(String username, String password, String ipAddr) throws Exception;

	void sso(String username, String ipAddr, String agent, HttpServletResponse response);

	List<ComUserMenu> getComUserMenus(String loginId) throws Exception;

	List<ComRole> getUserRoles(String loginId) throws Exception;

	List<Menu> getUserMenus(String loginId) throws Exception;

	List<ComRole> getOrgRoles(String orgCd) throws Exception;

	List<ComRole> getComRoles(ComRole comRole) throws Exception;

	List<ComDept> getOrgGrid(OrgUser orgUser) throws Exception;

	ComRoleUser getComRoleUser(ComRoleUser comRoleUser) throws Exception;

	void updateComRole(RoleRequest roleRequest) throws Exception;

	void addComRole(ComRole conRole) throws Exception;

	void addUserRole(RoleRequest roleRequest) throws Exception;

	void addRoleOrg(RoleRequest roleRequest) throws Exception;

	void addUserMenu(RoleRequest roleRequest) throws Exception;

	void addRoleMenu(RoleRequest roleRequest) throws Exception;

	void removeComRoleMenus(RoleRequest roleRequest) throws Exception;

	void removeComRoleUsers(RoleRequest roleRequest) throws Exception;

	void removeComUserMenus(RoleRequest roleRequest) throws Exception;

	ComRoleUsers getUserRolesOrCount(RoleRequest roleRequest) throws Exception;

	ComRoleOrgs getOrgRolesOrCount(RoleRequest roleRequest) throws Exception;

	ComRoleMenus getMenuRolesOrCount(RoleRequest roleRequest) throws Exception;

	ComRoleMenus getRoleMenusOrCount(RoleRequest roleRequest) throws Exception;

	void addOrRemoveComRoleUsers(RoleRequest roleRequest) throws Exception;

	void addOrRemoveComRoleOrgs(RoleRequest roleRequest) throws Exception;

	void addOrRemoveComRoleMenus(RoleRequest roleRequest) throws Exception;

	List<ComRoleUser> getComRoleUserByRoleIds(RoleRequest roleRequest) throws Exception;

	int userSign(UserSign userSign) throws Exception;

	List<UserSign> getUserSign(String loginId) throws Exception;

	int cntUserSign(String loginId) throws Exception;

	int getCntSignTrm(UserSignMngmt userSignMngmt) throws Exception;

	void addOrRemoveComMenuRoles(RoleRequest roleRequest) throws Exception;
}
