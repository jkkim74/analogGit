package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.common.acl.CookieUtils;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.acl.LoginLog;
import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.model.acl.UserSignMngmt;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.acl.UserRepository;
import com.skplanet.bisportal.repository.bip.OrgUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * The UserServiceImpl class(BipUser 처리 클래스).
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private LdapService ldapServiceImpl;
	@Autowired
	private AccessService accessServiceImpl;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrgUserRepository orgUserRepository;

	/**
	 * 로그인 처리
	 *
	 * @param username, password
	 * @return BipUser
	 * @throws Exception
	 */
	@Override
	@Transactional
	public BipUser login(String username, String password, String ipAddr) throws Exception {
		BipUser ldapUser;
		try {
			ldapUser = ldapServiceImpl.login(username, password);
			if (ldapUser != null) {
				String today = Utils.getCreateDate();
				ldapUser.setVoyagerToken(UUID.randomUUID().toString() + "-" + username);
				ldapUser.setLastupDate(today);
				ldapUser.setIp(ipAddr);
				// 아이디는 대문자로 저장됨.
				List<BipUser> users = userRepository.getBipUser(username);
				if (CollectionUtils.isNotEmpty(users)) {
					ldapUser.setId(users.get(0).getId());
					userRepository.updateBipUser(ldapUser);
				} else {
					ldapUser.setOrganizationId(1);
					ldapUser.setSignupDate(today);
					userRepository.createBipUser(ldapUser);
				}
			} else {
				log.error("Ldap Login failed. {}", username);
				throw new Exception("Login failed.");
			}
		} catch (Exception e) {
			log.error("BisPortal Login failed. {}", e);
			throw new Exception("Login failed.");
		}
		return ldapUser;
	}

	/**
	 * SSO 처리
	 *
	 * @param username
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void sso(String username, String ipAddr, String agent, HttpServletResponse response) {
		BipUser ldapUser;
		try {
			ldapUser = ldapServiceImpl.getBipUserUser(username.toUpperCase());
			if (ldapUser != null) {
				String today = Utils.getCreateDate();
				ldapUser.setLastupDate(today);
				ldapUser.setIp(ipAddr);
				// 아이디는 대문자로 저장됨.
				List<BipUser> users = userRepository.getBipUser(username.toUpperCase());
				if (CollectionUtils.isNotEmpty(users)) {
					ldapUser.setId(users.get(0).getId());
					userRepository.updateBipUser(ldapUser);
				} else {
					ldapUser.setOrganizationId(1);
					ldapUser.setSignupDate(today);
					userRepository.createBipUser(ldapUser);
				}
				CookieUtils.setVoyagerCookie(response, ldapUser, false);
				// 접근 로그 저장.
				LoginLog loginLog = new LoginLog();
				loginLog.setUsername(username.toUpperCase());
				loginLog.setIp(ipAddr);
				loginLog.setAgent(agent);
				loginLog.setLastUpdate(today);
				accessServiceImpl.createLoginLog(loginLog);
			} else {
				log.info("{} not found.", username.toUpperCase());
			}
		} catch (Exception e) {
			log.error("BisPortal SSO failed. {}", e);
		}
	}

	/**
	 * 사용자별 매뉴 정보 조회
	 *
	 * @param loginId
	 * @return collection of ComUserMenu
	 * @throws Exception
	 */
	@Override
	public List<ComUserMenu> getComUserMenus(String loginId) throws Exception {
		return userRepository.getComUserMenus(loginId);
	}

	/**
	 * 사용자별 권한 정보 조회
	 *
	 * @param loginId
	 * @return collection of ComRole
	 * @throws Exception
	 */
	@Override
	public List<ComRole> getUserRoles(String loginId) throws Exception {
		return userRepository.getUserRoles(loginId);
	}

	/**
	 * 사용자별 권한 정보 조회
	 *
	 * @param comRoleUser
	 * @return ComRoleUser
	 * @throws Exception
	 */
	@Override
	public ComRoleUser getComRoleUser(ComRoleUser comRoleUser) throws Exception {
		return userRepository.getComRoleUser(comRoleUser);
	}

	/**
	 * 멀티 사용자별 권한 정보 조회
	 *
	 * @param roleRequest
	 * @return ComRoleUsers
	 * @throws Exception
	 */
	@Override
	public ComRoleUsers getUserRolesOrCount(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getLoginIds())) {
			log.info("loginIds is null");
			return null;
		}
		String[] loginIdArray = roleRequest.getLoginIds().toArray(new String[roleRequest.getLoginIds().size()]);
		ComRoleUsers comRoleUsers = new ComRoleUsers();
		comRoleUsers.setRoleCounts(userRepository.getRoleCountByLoginIds(loginIdArray));
		comRoleUsers.setRoleUsers(userRepository.getComRoleUserByLoginids(loginIdArray));
		return comRoleUsers;
	}

	/**
	 * 멀티 사용자 조직별 권한 정보 조회
	 *
	 * @param roleRequest
	 * @return ComRoleOrgs
	 * @throws Exception
	 */
	@Override
	public ComRoleOrgs getOrgRolesOrCount(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getOrgCds())) {
			log.info("orgCds is null");
			return null;
		}
		String[] orgCdArray = roleRequest.getOrgCds().toArray(new String[roleRequest.getOrgCds().size()]);
		ComRoleOrgs comRoleOrgs = new ComRoleOrgs();
		comRoleOrgs.setRoleCounts(userRepository.getRoleCountByOrgCds(orgCdArray));
		comRoleOrgs.setRoleOrgs(userRepository.getComRoleOrgByOrgCds(orgCdArray));
		return comRoleOrgs;
	}

	/**
	 * 멀티 권한별 메뉴 정보 조회
	 *
	 * @param roleRequest
	 * @return ComRoleMenus
	 * @throws Exception
	 */
	@Override
	public ComRoleMenus getMenuRolesOrCount(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getRoleIds())) {
			log.info("menuIds is null");
			return null;
		}
		String[] roleIdArray = roleRequest.getRoleIds().toArray(new String[roleRequest.getRoleIds().size()]);
		ComRoleMenus comRoleMenus = new ComRoleMenus();
		comRoleMenus.setRoleCounts(userRepository.getRoleCountByRoleIds(roleIdArray));
		comRoleMenus.setRoleMenus(userRepository.getComRoleMenuByRoleIds(roleIdArray));
		return comRoleMenus;
	}

	/**
	 * 멀티 메뉴별 권한 메뉴 정보 조회
	 *
	 * @param roleRequest
	 * @return ComRoleMenus
	 * @throws Exception
	 */
	@Override
	public ComRoleMenus getRoleMenusOrCount(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getMenuIds())) {
			log.info("menuIds is null");
			return null;
		}
		String[] menuIdArray = roleRequest.getMenuIds().toArray(new String[roleRequest.getMenuIds().size()]);
		ComRoleMenus comRoleMenus = new ComRoleMenus();
		comRoleMenus.setMenuCounts(userRepository.getMenuCountByMenuIds(menuIdArray));
		comRoleMenus.setMenuRoles(userRepository.getComRoleMenuByMenuIds(menuIdArray));
		return comRoleMenus;
	}

	/**
	 * 사용자별 메뉴 정보 조회
	 *
	 * @param loginId
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getUserMenus(String loginId) throws Exception {
		return userRepository.getUserMenus(loginId);
	}

	/**
	 * 조직별 권한 정보 조회
	 *
	 * @param orgCd
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<ComRole> getOrgRoles(String orgCd) throws Exception {
		return userRepository.getOrgRoles(orgCd);
	}

	/**
	 * 권한 정보 조회
	 *
	 * @param comRole
	 * @return collection of ComRole
	 * @throws Exception
	 */
	@Override
	public List<ComRole> getComRoles(ComRole comRole) throws Exception {
		return userRepository.getComRoles(comRole);
	}

	/**
	 * 조직 정보 조회
	 *
	 * @param orgUser
	 * @return collection of ComDept
	 * @throws Exception
	 */
	@Override
	public List<ComDept> getOrgGrid(OrgUser orgUser) throws Exception {
		return orgUserRepository.getOrgGrid(orgUser);
	}

	/**
	 * 권한 아이디별로 권한 사용자 정보 조회
	 *
	 * @param roleRequest
	 * @return collection of ComRoleUser
	 * @throws Exception
	 */
	@Override
	public List<ComRoleUser> getComRoleUserByRoleIds(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getRoleIds())) {
			log.info("roleIds is null");
			return null;
		}
		String[] roleIdArray = roleRequest.getRoleIds().toArray(new String[roleRequest.getRoleIds().size()]);
		return userRepository.getComRoleUserByRoleIds(roleIdArray);
	}

	/**
	 * 권한 정보 변경
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateComRole(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isNotEmpty(roleRequest.getComRoles())) {
			for (ComRole comRole : roleRequest.getComRoles()) {
				if (StringUtils.isNotEmpty(comRole.getState())) {
					if (StringUtils.equals("활성", comRole.getState())) {
						comRole.setDeleteYn("N");
					} else if (StringUtils.equals("비활성", comRole.getState())) {
						comRole.setDeleteYn("P");
					} else {
						comRole.setDeleteYn("Y");
					}
				}
				comRole.setAuditId(roleRequest.getUsername());
				userRepository.updateComRole(comRole);
			}
		}
	}

	/**
	 * 권한 등록
	 *
	 * @param comRole
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addComRole(ComRole comRole) throws Exception {
		userRepository.addComRole(comRole);
	}

	/**
	 * 권한별 사용자 등록
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addUserRole(RoleRequest roleRequest) throws Exception {
		for (ComRoleUser comRoleUser : roleRequest.getComRoleUsers()) {
			comRoleUser.setAuditId(roleRequest.getUsername());
			ComRoleUser existsComRoleUser = userRepository.getComRoleUser(comRoleUser);
			if (existsComRoleUser == null) {
				userRepository.createComRoleUser(comRoleUser);
			}
		}
	}

	/**
	 * 권한별 조 등록
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addRoleOrg(RoleRequest roleRequest) throws Exception {
		for (ComRoleOrg comRoleOrg : roleRequest.getComRoleOrgs()) {
			comRoleOrg.setAuditId(roleRequest.getUsername());
			ComRoleOrg existsComRoleOrg = userRepository.getComRoleOrg(comRoleOrg);
			if (existsComRoleOrg == null) {
				userRepository.createComRoleOrg(comRoleOrg);
			}
		}
	}

	/**
	 * 사용자별 메뉴 등록
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addUserMenu(RoleRequest roleRequest) throws Exception {
		for (ComUserMenu comUserMenu : roleRequest.getComUserMenus()) {
			comUserMenu.setAuditId(roleRequest.getUsername());
			ComUserMenu existsComUserMenu = userRepository.getComUserMenu(comUserMenu);
			if (existsComUserMenu == null) {
				userRepository.createComUserMenu(comUserMenu);
			}
		}
	}

	/**
	 * 권한별 메뉴 등록
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addRoleMenu(RoleRequest roleRequest) throws Exception {
		for (Menu menu : roleRequest.getMenus()) {
			ComRoleMenu comRoleMenu = new ComRoleMenu();
			comRoleMenu.setRoleId(roleRequest.getRoleId());
			comRoleMenu.setMenuId(menu.getId());
			comRoleMenu.setAuditId(roleRequest.getUsername());
			ComRoleMenu existsComRoleMenu = userRepository.getComRoleMenu(comRoleMenu);
			if (existsComRoleMenu == null) {
				userRepository.createComRoleMenu(comRoleMenu);
			}
		}
	}

	/**
	 * 권한별 메뉴 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void removeComRoleMenus(RoleRequest roleRequest) throws Exception {
		for (Menu menu : roleRequest.getMenus()) {
			ComRoleMenu comRoleMenu = new ComRoleMenu();
			comRoleMenu.setRoleId(roleRequest.getRoleId());
			comRoleMenu.setMenuId(menu.getId());
			comRoleMenu.setAuditId(roleRequest.getUsername());
			userRepository.deleteComRoleMenu(comRoleMenu);
		}
	}

	/**
	 * 권한별 사용자 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void removeComRoleUsers(RoleRequest roleRequest) throws Exception {
		for (ComRole comRole : roleRequest.getComRoles()) {
			ComRoleUser comRoleUser = new ComRoleUser();
			comRoleUser.setRoleId(comRole.getId());
			comRoleUser.setLoginId(roleRequest.getLoginId());
			comRoleUser.setAuditId(roleRequest.getUsername());
			userRepository.deleteComRoleUser(comRoleUser);
		}
	}

	/**
	 * 사용자별 메뉴 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void removeComUserMenus(RoleRequest roleRequest) throws Exception {
		for (Menu menu : roleRequest.getMenus()) {
			ComUserMenu comUserMenu = new ComUserMenu();
			comUserMenu.setMenuId(menu.getId());
			comUserMenu.setLoginId(roleRequest.getLoginId());
			comUserMenu.setAuditId(roleRequest.getUsername());
			userRepository.deleteComUserMenu(comUserMenu);
		}
	}

	/**
	 * 권한별 사용자 등록 및 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addOrRemoveComRoleUsers(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isNotEmpty(roleRequest.getAddRoles())) {
			for (String roleId : roleRequest.getAddRoles()) {
				for (String loginId : roleRequest.getLoginIds()) {
					ComRoleUser comRoleUser = new ComRoleUser();
					comRoleUser.setRoleId(Long.parseLong(roleId));
					comRoleUser.setLoginId(loginId);
					ComRoleUser existUser = userRepository.getComRoleUser(comRoleUser);
					if (existUser == null) {
						comRoleUser.setAuditId(roleRequest.getUsername());
						userRepository.createComRoleUser(comRoleUser);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(roleRequest.getRemoveRoles())) {
			for (String roleId : roleRequest.getRemoveRoles()) {
				for (String loginId : roleRequest.getLoginIds()) {
					ComRoleUser comRoleUser = new ComRoleUser();
					comRoleUser.setRoleId(Long.parseLong(roleId));
					comRoleUser.setLoginId(loginId);
					ComRoleUser existUser = userRepository.getComRoleUser(comRoleUser);
					if (existUser != null) {
						userRepository.deleteComRoleUser(comRoleUser);
					}
				}
			}
		}
	}

	/**
	 * 조직별 권 등록 및 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addOrRemoveComRoleOrgs(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isNotEmpty(roleRequest.getAddRoles())) {
			for (String roleId : roleRequest.getAddRoles()) {
				for (String orgCd : roleRequest.getOrgCds()) {
					ComRoleOrg comRoleOrg = new ComRoleOrg();
					comRoleOrg.setRoleId(Long.parseLong(roleId));
					comRoleOrg.setOrgCd(orgCd);
					ComRoleOrg existOrg = userRepository.getComRoleOrg(comRoleOrg);
					if (existOrg == null) {
						comRoleOrg.setAuditId(roleRequest.getUsername());
						userRepository.createComRoleOrg(comRoleOrg);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(roleRequest.getRemoveRoles())) {
			for (String roleId : roleRequest.getRemoveRoles()) {
				for (String orgCd : roleRequest.getOrgCds()) {
					ComRoleOrg comRoleOrg = new ComRoleOrg();
					comRoleOrg.setRoleId(Long.parseLong(roleId));
					comRoleOrg.setOrgCd(orgCd);
					ComRoleOrg existOrg = userRepository.getComRoleOrg(comRoleOrg);
					if (existOrg != null) {
						userRepository.deleteComRoleOrg(comRoleOrg);
					}
				}
			}
		}
	}

	/**
	 * 권한별 메뉴 등록 및 삭제
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addOrRemoveComRoleMenus(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isNotEmpty(roleRequest.getAddMenus())) {
			for (String menuId : roleRequest.getAddMenus()) {
				for (String roleId : roleRequest.getRoleIds()) {
					ComRoleMenu comRoleMenu = new ComRoleMenu();
					comRoleMenu.setRoleId(Long.parseLong(roleId));
					comRoleMenu.setMenuId(Long.parseLong(menuId));
					ComRoleMenu existMenu = userRepository.getComRoleMenu(comRoleMenu);
					if (existMenu == null) {
						comRoleMenu.setAuditId(roleRequest.getUsername());
						userRepository.createComRoleMenu(comRoleMenu);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(roleRequest.getRemoveMenus())) {
			for (String menuId : roleRequest.getRemoveMenus()) {
				for (String roleId : roleRequest.getRoleIds()) {
					ComRoleMenu comRoleMenu = new ComRoleMenu();
					comRoleMenu.setRoleId(Long.parseLong(roleId));
					comRoleMenu.setMenuId(Long.parseLong(menuId));
					ComRoleMenu existMenu = userRepository.getComRoleMenu(comRoleMenu);
					if (existMenu != null) {
						userRepository.deleteComRoleMenu(comRoleMenu);
					}
				}
			}
		}
	}

	/**
	 * 개인정보 보호 동의여부 조회
	 *
	 * @param userSign
	 * @return int
	 * @throws Exception
	 */
	@Override
	@Transactional
	public int userSign(UserSign userSign) throws Exception {
		return userRepository.createUserSign(userSign);
	}

	/**
	 * 사용자별 개인정보 보호 동의여부 조회
	 *
	 * @param loginId
	 * @return int
	 * @throws Exception
	 */
	@Override
	public List<UserSign> getUserSign(String loginId) throws Exception {
		return userRepository.getUserSign(loginId);
	}

	@Override
	public int cntUserSign(String loginId) throws Exception {
		return userRepository.getCntUserSign(loginId);
	}

	@Override
	public int getCntSignTrm(UserSignMngmt userSignMngmt) throws Exception {
		return userRepository.getCntSignTrm(userSignMngmt);
	}

	@Override
	@Transactional
	public void addOrRemoveComMenuRoles(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isNotEmpty(roleRequest.getAddRoles())) {
			for (String roleId : roleRequest.getAddRoles()) {
				for (String menuId : roleRequest.getMenuIds()) {
					ComRoleMenu comRoleMenu = new ComRoleMenu();
					comRoleMenu.setRoleId(Long.parseLong(roleId));
					comRoleMenu.setMenuId(Long.parseLong(menuId));
					ComRoleMenu existMenu = userRepository.getComRoleMenu(comRoleMenu);
					if (existMenu == null) {
						comRoleMenu.setAuditId(roleRequest.getUsername());
						userRepository.createComRoleMenu(comRoleMenu);
					}
				}
			}
		}
		if (CollectionUtils.isNotEmpty(roleRequest.getRemoveRoles())) {
			for (String roleId : roleRequest.getRemoveRoles()) {
				for (String menuId : roleRequest.getMenuIds()) {
					ComRoleMenu comRoleMenu = new ComRoleMenu();
					comRoleMenu.setRoleId(Long.parseLong(roleId));
					comRoleMenu.setMenuId(Long.parseLong(menuId));
					ComRoleMenu existMenu = userRepository.getComRoleMenu(comRoleMenu);
					if (existMenu != null) {
						userRepository.deleteComRoleMenu(comRoleMenu);
					}
				}
			}
		}
	}
}
