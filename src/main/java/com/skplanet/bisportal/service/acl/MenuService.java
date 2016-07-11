package com.skplanet.bisportal.service.acl;

import com.skplanet.bisportal.model.bip.Menu;
import com.skplanet.bisportal.model.bip.Navigation;
import com.skplanet.bisportal.model.bip.RoleRequest;
import com.skplanet.bisportal.model.bip.TreeMenuService;
import com.skplanet.bisportal.model.mstr.MstrMenu;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The MenuService interface.
 * 
 * @author sjune
 */
public interface MenuService {
	/**
	 * 리포트 서비스 목록을 조회한다.
	 * 
	 * @return the list of TreeMenuService
	 */
	List<TreeMenuService> getReportServices(Menu menu) throws Exception;

	/**
	 * 대시보드 서비스를 조회한다.
	 * 
	 * @return TreeMenuService
	 */
	TreeMenuService getDashboardService(Menu menu) throws Exception;

	/**
	 * 관리자 서비스를 조회한다.
	 * 
	 * @return TreeMenuService
	 */
	TreeMenuService getAdminService(Menu menu) throws Exception;

	/**
	 * helpDesk 서비스를 조회한다.
	 *
	 * @return TreeMenuService
	 */
	TreeMenuService getHelpDeskService(Menu menu) throws Exception;

	/**
	 * navigation 메뉴 권한 처라를 위한 데이터를 조회한다.
	 *
	 * @return Navigation
	 */
	Navigation getNavigationMenu(String loginId) throws Exception;
	/**
	 * Menu를 생성한다.
	 * 
	 * @param menu
	 */
	void createMenu(Menu menu) throws Exception;

	/**
	 * 리포트용 서비스를 생성한다.
	 *
	 * @param menu
	 */
	void addReportService(Menu menu) throws Exception;

	/**
	 * 리포트용 카테고리를 생성한다.
	 *
	 * @param menu
	 */
	void addReportCategory(Menu menu) throws Exception;

	/**
	 * Admin 카테고리를 생성한다.
	 *
	 * @param menu
	 */
	void addAdminCategory(Menu menu) throws Exception;

	/**
	 * 리포트용 메뉴를 생성한다.
	 *
	 * @param menu
	 */
	void addReportMenu(Menu menu) throws Exception;

	/**
	 * 경영실적(대시보드) 메뉴를 생성한다.
	 *
	 * @param menu
	 */
	void addDashboardMenu(Menu menu) throws Exception;

	/**
	 * Admin 메뉴를 생성한다.
	 *
	 * @param menu
	 */
	void addAdminMenu(Menu menu) throws Exception;

	/**
	 * Menu를 업데이트한다.
	 * 
	 * @param menu
	 */
	void updateMenu(Menu menu) throws Exception;

	/**
	 * Menu들을 일괄 업데이트한다.
	 * 
	 * @param menus
	 */
	void updateMenus(List<Menu> menus) throws Exception;

	/**
	 * 권한 메뉴에서 Menu들을 일괄 업데이트한다.
	 *
	 * @param roleRequest
	 */
	void updateMenusByRole(RoleRequest roleRequest) throws Exception;

	/**
	 * id로 메뉴를 조회한다.
	 * 
	 * @param id
	 * @return Menu by id
	 */
	Menu getMenuById(Long id);

	/**
	 * 자식 메뉴의 순서 번호를 갱신한다.
	 * 
	 * @param parentMenu
	 */
	void updateMenuChildrenOrderIdx(Menu parentMenu);

	/**
	 * 경영실적 권한에 속하는 메뉴 정보를 조회한다.
	 *
	 * @return List<Menu>
	 */
	List<Menu> getMenuByBoss(RoleRequest roleRequest) throws Exception;

	/**
	 * 전체 메뉴 목록을 조회한다.
	 *
	 * @return the list of all menus
	 */
	List<Menu> getMenus() throws Exception;

	/**
	 * 권한별로 메뉴 목록을 조회한다.
	 *
	 * @return the list of menus
	 */
	List<Menu> getMenusByRole(RoleRequest roleRequest) throws Exception;

	/**
	 * 사용자별로 메뉴 목록을 조회한다.
	 *
	 * @return the list of menus
	 */
	List<Menu> getMenusByLoginIds(RoleRequest roleRequest) throws Exception;

	/**
	 * 경영실적(대시보드) 카테고리를 생성한다.
	 *
	 * @param menu
	 */
	void addDashboardCategory(Menu menu) throws Exception;

	List<Menu> getMenuTree() throws Exception;

	List<Menu> getTreeMenuBySearch(Menu menu);

	List<MstrMenu> getMstrRootTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu) throws Exception;

	List<MstrMenu> getSubMenuTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu) throws Exception;

	List<Menu> getMstrMenuByCode(Menu menu);
}
