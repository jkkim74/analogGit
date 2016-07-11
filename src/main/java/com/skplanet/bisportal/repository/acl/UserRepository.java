package com.skplanet.bisportal.repository.acl;

import com.skplanet.bisportal.model.acl.BipUser;
import com.skplanet.bisportal.model.acl.UserSign;
import com.skplanet.bisportal.model.acl.UserSignMngmt;
import com.skplanet.bisportal.model.bip.ComRole;
import com.skplanet.bisportal.model.bip.ComRoleMenu;
import com.skplanet.bisportal.model.bip.ComRoleOrg;
import com.skplanet.bisportal.model.bip.ComRoleUser;
import com.skplanet.bisportal.model.bip.ComUserMenu;
import com.skplanet.bisportal.model.bip.Menu;
import com.skplanet.bisportal.model.mstr.MstrSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * The UserRepository class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Repository
public class UserRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	public int createBipUser(BipUser user) {
		return sqlSession.insert("createBipUser", user);
	}

	public int updateBipUser(BipUser user) {
		return sqlSession.update("updateBipUser", user);
	}

	public List<BipUser> getBipUser(String username) {
		return sqlSession.selectList("getBipUser", username.toUpperCase());
	}
	public int createComUserMenu(ComUserMenu comUserRole) {
		return sqlSession.insert("createComUserMenu", comUserRole);
	}

	public int addComRole(ComRole comRole) {
		return sqlSession.insert("addComRole", comRole);
	}

	public int createComRoleUser(ComRoleUser comRoleUser) {
		return sqlSession.insert("createComRoleUser", comRoleUser);
	}

	public List<ComUserMenu> getComUserMenus(String loginId) {
		return sqlSession.selectList("getComUserMenus", loginId);
	}

	public List<ComRole> getUserRoles(String loginId) {
		return sqlSession.selectList("getUserRoles", loginId);
	}

	public List<ComRole> getOrgRoles(String orgCd) {
		return sqlSession.selectList("getOrgRoles", orgCd);
	}

	public List<Menu> getUserMenus(String loginId) {
		return sqlSession.selectList("getUserMenus", loginId);
	}

	public int deleteComUserMenu(ComUserMenu comUserRole) {
		return sqlSession.delete("deleteComUserMenu", comUserRole);
	}

	public List<ComRole> getComRoles(ComRole comRole) {
		return sqlSession.selectList("getComRoles", comRole);
	}

	public int updateComRole(ComRole comRole) {
		return sqlSession.update("updateComRole", comRole);
	}

	public ComRoleUser getComRoleUser(ComRoleUser comRoleUser) {
		return sqlSession.selectOne("getComRoleUser", comRoleUser);
	}

	public ComRoleOrg getComRoleOrg(ComRoleOrg comRoleOrg) {
		return sqlSession.selectOne("getComRoleOrg", comRoleOrg);
	}

	public ComUserMenu getComUserMenu(ComUserMenu comUserMenu) {
		return sqlSession.selectOne("getComUserMenu", comUserMenu);
	}

	public ComRoleMenu getComRoleMenu(ComRoleMenu comRoleMenu) {
		return sqlSession.selectOne("getComRoleMenu", comRoleMenu);
	}

	public int createComRoleMenu(ComRoleMenu comRoleMenu) {
		return sqlSession.insert("createComRoleMenu", comRoleMenu);
	}

	public int createComRoleOrg(ComRoleOrg comRoleOrg) {
		return sqlSession.insert("createComRoleOrg", comRoleOrg);
	}

	public int deleteComRoleMenu(ComRoleMenu comRoleMenu) {
		return sqlSession.delete("deleteComRoleMenu", comRoleMenu);
	}

	public int deleteComRoleUser(ComRoleUser comRoleUser) {
		return sqlSession.delete("deleteComRoleUser", comRoleUser);
	}

	public int deleteComRoleOrg(ComRoleOrg comRoleOrg) {
		return sqlSession.delete("deleteComRoleOrg", comRoleOrg);
	}

	public List<ComRoleUser> getComRoleUserByLoginids(String[] userArray) {
		return sqlSession.selectList("getComRoleUserByLoginids", userArray);
	}

	public List<ComRoleOrg> getComRoleOrgByOrgCds(String[] orgArray) {
		return sqlSession.selectList("getComRoleOrgByOrgCds", orgArray);
	}

	public List<ComRoleMenu> getComRoleMenuByRoleIds(String[] roleIdArray) {
		return sqlSession.selectList("getComRoleMenuByRoleIds", roleIdArray);
	}

	public List<ComRoleUser> getRoleCountByLoginIds(String[] userArray) {
		return sqlSession.selectList("getRoleCountByLoginIds", userArray);
	}

	public List<ComRoleOrg> getRoleCountByOrgCds(String[] orgArray) {
		return sqlSession.selectList("getRoleCountByOrgCds", orgArray);
	}

	public List<ComRoleMenu> getRoleCountByRoleIds(String[] roleIdArray) {
		return sqlSession.selectList("getRoleCountByRoleIds", roleIdArray);
	}

	public List<ComRoleUser> getComRoleUserByRoleIds(String[] roleIdArray) {
		return sqlSession.selectList("getComRoleUserByRoleIds", roleIdArray);
	}

	public List<ComRoleMenu> getMenuCountByMenuIds(String[] menuIdArray) {
		return sqlSession.selectList("getMenuCountByMenuIds", menuIdArray);
	}

	public List<ComRoleMenu> getComRoleMenuByMenuIds(String[] menuIdArray) {
		return sqlSession.selectList("getComRoleMenuByMenuIds", menuIdArray);
	}

	public int createUserSign(UserSign userSign) {
		return sqlSession.insert("createUserSign", userSign);
	}

	public List<UserSign> getUserSign(String loginId) {
		return sqlSession.selectList("getUserSign", loginId);
	}

	public int getCntUserSign(String loginId) {
		return sqlSession.selectOne("getCntUserSign", loginId);
	}

	public int getCntSignTrm(UserSignMngmt userSignMngmt) {
		return sqlSession.selectOne("getUserSignTrm", userSignMngmt);
	}

	public int createMstrSession(MstrSession mstrSession) {
		return sqlSession.insert("createMstrSession", mstrSession);
	}

	public int deleteMstrSession(MstrSession mstrSession) {
		return sqlSession.insert("deleteMstrSession", mstrSession);
	}

	public String getMstrSession(MstrSession mstrSession) {
		return sqlSession.selectOne("getMstrSession", mstrSession);
	}
}
