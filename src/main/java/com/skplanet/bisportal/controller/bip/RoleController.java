package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.common.model.JqGridResponse;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.service.acl.MenuService;
import com.skplanet.bisportal.service.acl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The RoleController class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController {
	@Autowired
	private UserService userServiceImpl;
	@Autowired
	private MenuService menuServiceImpl;

	/**
	 * 사용자 권한 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/gets", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ComRole> getComRoles() throws Exception {
		JqGridResponse<ComRole> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(userServiceImpl.getComRoles(null));
		return jqGridResponse;
	}

	/**
	 * 조직 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/gets", method = RequestMethod.POST)
	@ResponseBody
	public JqGridResponse<ComRole> getOrgGrid(@RequestBody ComRole comRole) throws Exception {
		if (StringUtils.isNotEmpty(comRole.getName())) {
			comRole.setName("%" + comRole.getName() + "%");
		}
		JqGridResponse<ComRole> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(userServiceImpl.getComRoles(comRole));
		return jqGridResponse;
	}

	/**
	 * 조직 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addComRole(@RequestBody ComRole comRole) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addComRole(comRole);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addComRole() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자 전체 권한 정보 조회
	 * 
	 * @return
	 */
	@RequestMapping(value = "/userRoleMenus", method = RequestMethod.GET)
	@ResponseBody
	public List<ComUserMenu> getComUserMenus(String loginId) throws Exception {
		return userServiceImpl.getComUserMenus(loginId);
	}

	/**
	 * 사용자 권한 정보 전체 조회
	 *
	 * @return collection of ComRole
	 */
	@RequestMapping(value = "/userRoles", method = RequestMethod.GET)
	@ResponseBody
	public List<ComRole> getUserRoles(String loginId) throws Exception {
		return userServiceImpl.getUserRoles(loginId);
	}

	/**
	 * 사용자의 해당 권한 정보 조회
	 *
	 * @return ComRoleUser
	 */
	@RequestMapping(value = "/comRoleUser", method = RequestMethod.GET)
	@ResponseBody
	public ComRoleUser getComRoleUser(ComRoleUser comRoleUser) throws Exception {
		return userServiceImpl.getComRoleUser(comRoleUser);
	}

	/**
	 * 사용자 메뉴 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/userMenus", method = RequestMethod.GET)
	@ResponseBody
	public List<Menu> getUserMenus(String loginId) throws Exception {
		return userServiceImpl.getUserMenus(loginId);
	}

	/**
	 * 권한 테이블 수정.
	 *
	 * @param roleRequest
	 * @return
	 */
	@RequestMapping(value = "/updateComRoles", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult updateComRoles(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.updateComRole(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("updateComRoles() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 권한별 메뉴 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/roleMenus", method = RequestMethod.POST)
	@ResponseBody
	public JqGridResponse<Menu> getRoleMenus(@RequestBody RoleRequest roleRequest) throws Exception {
		JqGridResponse<Menu> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(menuServiceImpl.getMenuByBoss(roleRequest));
		return jqGridResponse;
	}

	/**
	 * 조직 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/orgs", method = RequestMethod.POST)
	@ResponseBody
	public JqGridResponse<ComDept> getOrgGrid(@RequestBody OrgUser orgUser) throws Exception {
		if (StringUtils.equals(orgUser.getSearchCondition(), "orgNm")
				&& StringUtils.isNotEmpty(orgUser.getSearchKeyword())) {
			orgUser.setSearchKeyword("%" + orgUser.getSearchKeyword() + "%");
		}
		JqGridResponse<ComDept> jqGridResponse = new JqGridResponse<>();
		jqGridResponse.setRows(userServiceImpl.getOrgGrid(orgUser));
		return jqGridResponse;
	}

	/**
	 * 권한별 메뉴 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/orgRoles", method = RequestMethod.GET)
	@ResponseBody
	public JqGridResponse<ComRole> getOrgRoles(String orgCd) throws Exception {
		JqGridResponse<ComRole> jqGridResponse = new JqGridResponse<>();
		if (StringUtils.isNotEmpty(orgCd)) {
			jqGridResponse.setRows(userServiceImpl.getOrgRoles(orgCd));
		}
		return jqGridResponse;
	}

	/**
	 * 사용자 권한 등록.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/addUserRole", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addUserRole(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addUserRole(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addUserRole() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자 메뉴 권한 등록.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/addUserMenu", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addUserMenu(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addUserMenu(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addUserRole() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자 메뉴 권한 등록.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/addRoleMenu", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addRoleMenu(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addRoleMenu(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addRoleMenu() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 권한별 메뉴 삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/removeComRoleMenus", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult removeComRoleMenus(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.removeComRoleMenus(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("removeComRoleMenus() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자별 권한 삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/removeComRoleUsers", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult removeComRoleUsers(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.removeComRoleUsers(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("removeComRoleUsers() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자별 메뉴 권한 삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/removeComUserMenus", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult removeComUserMenus(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.removeComUserMenus(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("removeComUserMenus() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자 권한 등록.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/addRoleOrg", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult addRoleOrg(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addRoleOrg(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addRoleOrg() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 사용자 관리 화면용 권한별 사용자 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/roleUserCount", method = RequestMethod.POST)
	@ResponseBody
	public ComRoleUsers getRoleUserCount(@RequestBody RoleRequest roleRequest) throws Exception {
		return userServiceImpl.getUserRolesOrCount(roleRequest);
	}

	/**
	 * 사용자 관리 화면용 권한별 조직 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/roleOrgCount", method = RequestMethod.POST)
	@ResponseBody
	public ComRoleOrgs getRoleOrgCount(@RequestBody RoleRequest roleRequest) throws Exception {
		return userServiceImpl.getOrgRolesOrCount(roleRequest);
	}

	/**
	 * 권한 관리 화면용 권한별 메뉴 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/roleMenuCount", method = RequestMethod.POST)
	@ResponseBody
	public ComRoleMenus getRoleMenuCount(@RequestBody RoleRequest roleRequest) throws Exception {
		return userServiceImpl.getMenuRolesOrCount(roleRequest);
	}

	/**
	 * 사용자 권한 등록/삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/handleComRoleUser", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult handleComRoleUser(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addOrRemoveComRoleUsers(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("handleComRoleUser() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("db_fail");
		}
		return ajaxResult;
	}

	/**
	 * 부서 권한 등록/삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/handleComRoleOrg", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult handleComRoleOrg(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addOrRemoveComRoleOrgs(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("handleComRoleOrg() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("db_fail");
		}
		return ajaxResult;
	}

	/**
	 * 권한의 메뉴 등록/삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/handleComRoleMenu", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult handleComRoleMenu(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addOrRemoveComRoleMenus(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("handleComRoleMenu() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("db_fail");
		}
		return ajaxResult;
	}

	/**
	 * 권한 등록된 사용자 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/getUserByRole", method = RequestMethod.POST)
	@ResponseBody
	public List<ComRoleUser> getUserByRole(@RequestBody RoleRequest roleRequest) throws Exception {
		return userServiceImpl.getComRoleUserByRoleIds(roleRequest);
	}

	/**
	 * 권한 관리 화면용 메뉴별 권한 정보 조회
	 *
	 * @return
	 */
	@RequestMapping(value = "/menuRoleCount", method = RequestMethod.POST)
	@ResponseBody
	public ComRoleMenus getMenuRoleCount(@RequestBody RoleRequest roleRequest) throws Exception {
		return userServiceImpl.getRoleMenusOrCount(roleRequest);
	}

	/**
	 * 메뉴의 권한 등록/삭제.
	 *
	 * @param roleRequest
	 * @return AjaxResult
	 */
	@RequestMapping(value = "/handleComMenuRole", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult handleComMenuRole(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			userServiceImpl.addOrRemoveComMenuRoles(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("handleComMenuRole() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("db_fail");
		}
		return ajaxResult;
	}
}
