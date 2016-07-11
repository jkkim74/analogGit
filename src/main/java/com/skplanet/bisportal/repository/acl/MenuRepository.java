package com.skplanet.bisportal.repository.acl;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.skplanet.bisportal.model.bip.Menu;
import com.skplanet.bisportal.model.bip.RoleRequest;
import com.skplanet.bisportal.model.bip.TreeMenu;
import com.skplanet.bisportal.model.bip.TreeMenuCategory;
import com.skplanet.bisportal.model.bip.TreeMenuService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * The MenuRepository class.
 *
 * @author sjune
 */
@Repository
public class MenuRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * id로 메뉴를 조회한다.
	 * 
	 * @param id
	 * @return Menu
	 */
	public Menu getMenuById(Long id) {
		return sqlSession.selectOne("getMenuById", id);
	}

	/**
	 * TreeMenu 서비스를 조회한다.
	 * 
	 * @param paramMenu
	 * @return
	 */
	public TreeMenuService getTreeMenuService(Menu paramMenu) {
		return sqlSession.selectOne("getTreeMenuService", paramMenu);
	}

	public List<Menu> getNavigationMenu(String loginId) {
		return sqlSession.selectList("getNavigationMenu", loginId);
	}

	/**
	 * TreeMenu 서비스 map을 조회한다.
	 * 
	 * @param paramMenu
	 * @return
	 */
	public Map<Long, TreeMenuService> getTreeMenuServiceMap(Menu paramMenu) {
		List<TreeMenuService> treeMenuServices = sqlSession.selectList("getTreeMenuServices", paramMenu);
		return Maps.uniqueIndex(treeMenuServices, new Function<TreeMenuService, Long>() {
			@Override public Long apply(TreeMenuService input) {
				return input.getId();
			}
		});
	}

	/**
	 * TreeMenu 카테고리 map을 조회한다.
	 * 
	 * @param paramMenu
	 * @return
	 */
	public Map<Long, TreeMenuCategory> getTreeMenuCategoryMap(Menu paramMenu) {
		List<TreeMenuCategory> treeMenuCategories = sqlSession.selectList("getTreeMenuCategories", paramMenu);
		return Maps.uniqueIndex(treeMenuCategories, new Function<TreeMenuCategory, Long>() {
			@Override public Long apply(TreeMenuCategory input) {
				return input.getId();
			}
		});
	}

	/**
	 * TreeMenu 메뉴 목록과 메뉴 검색 옵션을 조회한다.
	 * 
	 * @param paramMenu
	 * @return
	 */
	public List<TreeMenu> getTreeMenuWithMenuSearchOption(Menu paramMenu) {
		return sqlSession.selectList("getTreeMenuWithMenuSearchOption", paramMenu);
	}

	/**
	 * TreeMenu 메뉴 목록을 조회한다.
	 *
	 * @param paramMenu
	 *
	 * @return
	 */
	public List<TreeMenu> getTreeMenus(Menu paramMenu) {
		return sqlSession.selectList("getTreeMenus", paramMenu);
	}

	/**
	 * order_idx 최대값을 조회한다
	 *
	 * @param menu
	 *
	 * @return
	 */
	public Integer getMaxOrderIdxByParentIDCommonCodeId(Menu menu) {
		return sqlSession.selectOne("getMaxOrderIdxByParentIDCommonCodeId", menu);
	}

	/**
	 * code의 최대값을 조회한다
	 *
	 * @param menu
	 *
	 * @return
	 */
	public Integer getMaxCodeByParentId(Menu menu) {
		return sqlSession.selectOne("getMaxCodeByParentId", menu);
	}

	/**
	 * 메뉴를 생성한다.
	 * 
	 * @param menu
	 *            생성할 메뉴
	 * @return
	 */
	public int createMenu(Menu menu) {
		return sqlSession.insert("createMenu", menu);
	}

	/**
	 * 메뉴를 갱신한다.
	 * 
	 * @param menu
	 *            갱신할 메뉴
	 * @return
	 */
	public int updateMenu(Menu menu) {
		return sqlSession.update("updateMenu", menu);
	}

	/**
	 * 자식 메뉴의 순서 번호를 갱신한다.
	 * 
	 * @param parentMenu
	 *            부모 메뉴
	 */
	public void updateMenuChildrenOrderIdx(Menu parentMenu) {
		sqlSession.update("updateMenuChildrenOrderIdx", parentMenu);
	}

	/**
	 * id로 메뉴를 조회한다.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMenuByBoss(RoleRequest roleRequest) {
		return sqlSession.selectList("getMenuByBoss", roleRequest);
	}

	/**
	 * 전체 메뉴를 조회한다.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMenus() {
		return sqlSession.selectList("getMenus");
	}

	/**
	 * 권하별 메뉴를 조회한다.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMenusByRole(RoleRequest roleRequest) {
		return sqlSession.selectList("getMenusByRole", roleRequest);
	}

	/**
	 * 권하별 메뉴를 조회한다.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMenusByLoginIds(String[] loginIdArray) {
		return sqlSession.selectList("getMenusByLoginIds", loginIdArray);
	}

	/**
	 * name로 메뉴를 조회한다.
	 *
	 * @param menu
	 * @return Menu
	 */
	public List<Menu> getMenuByName(Menu menu) {
		return sqlSession.selectList("getMenuByName", menu);
	}
	/**
	 * 메뉴 트리 구성을 위한 조회.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMenuTree() {
		return sqlSession.selectList("getMenuTree");
	}

	/**
	 * TreeMenu에서 메뉴 목록을 조회한다(parentId).
	 *
	 * @param menu
	 *
	 * @return
	 */
	public List<Menu> getTreeMenuBySearch(Menu menu) {
		return sqlSession.selectList("getTreeMenuBySearch", menu);
	}

	/**
	 * 서비스 메뉴 전체 조회.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getServices() {
		return sqlSession.selectList("getServices");
	}//

	/**
	 * MSTR 메뉴 조회.
	 *
	 * @return List<Menu>
	 */
	public List<Menu> getMstrMenuByCode(Menu menu) {
		return sqlSession.selectList("getMstrMenuByCode", menu);
	}

	/**
	 * 메뉴 백업 테이블에 삽입.
	 *
	 * @param menu 백업할 메뉴
	 * @return
	 */
	public int createMenuBack(Menu menu) {
		return sqlSession.insert("createMenuBack", menu);
	}

	/**
	 * menu를 제거한다.
	 *
	 * @param menuId
	 */
	public void deleteMenu(Long menuId) {
		sqlSession.delete("deleteMenu", menuId);
	}
}
