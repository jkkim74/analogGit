package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.common.model.AjaxResult;
import com.skplanet.bisportal.model.bip.Menu;
import com.skplanet.bisportal.model.bip.Navigation;
import com.skplanet.bisportal.model.bip.RoleRequest;
import com.skplanet.bisportal.model.bip.TreeMenuService;
import com.skplanet.bisportal.model.mstr.MstrMenu;
import com.skplanet.bisportal.service.acl.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * The MenuController class.
 *
 * @author sjune
 */
@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuController {
	@Autowired
	private MenuService menuServiceImpl;

	/**
	 * 리포트 서비스 목록을 조회한다.
	 * 
	 * @return list of reportService
	 */
	@RequestMapping(value = "/reportServices/{loginId}", method = RequestMethod.GET)
	public @ResponseBody List<TreeMenuService> getReportServices(@PathVariable String loginId) throws Exception {
		Menu menu = new Menu();
		menu.setLoginId(loginId);
		return menuServiceImpl.getReportServices(menu);
	}

	/**
	 * 대시보드 서비스 목록을 조회한다.
	 *
	 * @return dashboardService
	 */
	@RequestMapping(value = "/dashboardService/{loginId}", method = RequestMethod.GET)
	public @ResponseBody TreeMenuService getDashboardService(@PathVariable String loginId) throws Exception {
		Menu menu = new Menu();
		menu.setLoginId(loginId);
		return menuServiceImpl.getDashboardService(menu);
	}

	/**
	 * 관리자 서비스 목록을 조회한다.
	 *
	 * @return adminService
	 */
	@RequestMapping(value = "/adminService/{loginId}", method = RequestMethod.GET)
	public @ResponseBody TreeMenuService getAdminService(@PathVariable String loginId) throws Exception {
		Menu menu = new Menu();
		menu.setLoginId(loginId);
		return menuServiceImpl.getAdminService(menu);
	}

	/**
	 * helpDesk 서비스 목록을 조회한다.
	 *
	 * @return helpDeskService
	 */
	@RequestMapping(value = "/helpDeskService/{loginId}", method = RequestMethod.GET)
	public @ResponseBody TreeMenuService getHelpDeskService(@PathVariable String loginId) throws Exception {
		Menu menu = new Menu();
		menu.setLoginId(loginId);
		return menuServiceImpl.getHelpDeskService(menu);
	}

	/**
	 * helpDesk 서비스 목록을 조회한다.
	 *
	 * @return helpDeskService
	 */
	@RequestMapping(value = "/navigationMenu/{loginId}", method = RequestMethod.GET)
	public @ResponseBody Navigation getNavigationMenu(@PathVariable String loginId) throws Exception {
		return menuServiceImpl.getNavigationMenu(loginId);
	}

	/**
	 * 단건의 메뉴를 업데이트한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody void update(@RequestBody Menu menu) throws Exception {
		menuServiceImpl.updateMenu(menu);
	}

	/**
	 * 여러건의 메뉴를 업데이트한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/updateMenus", method = RequestMethod.POST)
	public @ResponseBody void updateMenus(@RequestBody List<Menu> menus) throws Exception {
		menuServiceImpl.updateMenus(menus);
	}

	/**
	 * 여러건의 메뉴를 업데이트한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/updateMenusByRole", method = RequestMethod.POST)
	public @ResponseBody AjaxResult updateMenusByRole(@RequestBody RoleRequest roleRequest) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.updateMenusByRole(roleRequest);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("updateMenusByRole() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 전체 메뉴 목록을 조회한다.
	 *
	 * @return list of all menu
	 */
	@RequestMapping(value = "/gets", method = RequestMethod.GET)
	public @ResponseBody List<Menu> getMenus() throws Exception {
		return menuServiceImpl.getMenus();
	}

	/**
	 * 리포트의 서비스를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addReportService", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addReportService(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addReportService(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addService() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 리포트의 카테고리를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addReportCategory", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addReportCategory(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addReportCategory(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addReportCategory() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 리포트의 메뉴를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addReportMenu", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addReportMenu(@RequestBody Menu menu) throws Exception {
		log.debug("addReportMenu {}", menu);
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addReportMenu(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addReportMenu() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * Dashboard 카테고리를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addDashboardCategory", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addDashboardCategory(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addDashboardCategory(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addDashboardCategory() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * Admin 카테고리를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addAdminCategory", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addAdminCategory(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addAdminCategory(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addAdminCategory() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 경영실적 메뉴를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addDashboardMenu", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addDashboardMenu(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addDashboardMenu(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addDashboardMenu() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * Admin 메뉴를 동록한다.
	 *
	 * @return
	 */
	@RequestMapping(value = "/addAdminMenu", method = RequestMethod.POST)
	public @ResponseBody AjaxResult addAdminMenu(@RequestBody Menu menu) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			menuServiceImpl.addAdminMenu(menu);
			ajaxResult.setCode(200);
			ajaxResult.setMessage("success");
		} catch (Exception e) {
			log.error("addAdminMenu() {}", e);
			ajaxResult.setCode(999);
			ajaxResult.setMessage("dbfail");
		}
		return ajaxResult;
	}

	/**
	 * 권한에 해당하는 메뉴 목록을 조회한다.
	 *
	 * @return list of menu
	 */
	@RequestMapping(value = "/getMenusByRole", method = RequestMethod.POST)
	public @ResponseBody List<Menu> getMenusByRole(@RequestBody RoleRequest roleRequest) throws Exception {
		return menuServiceImpl.getMenusByRole(roleRequest);
	}

	/**
	 * 사용자에 해당하는 메뉴 목록을 조회한다.
	 *
	 * @return list of menu
	 */
	@RequestMapping(value = "/getMenusByLoginIds", method = RequestMethod.POST)
	public @ResponseBody List<Menu> getMenusByLoginIds(@RequestBody RoleRequest roleRequest) throws Exception {
		return menuServiceImpl.getMenusByLoginIds(roleRequest);
	}

	/**
	 * 메뉴 트리 목록을 조회.
	 *
	 * @return List<Menu>
	 */
	@RequestMapping(value = "/menuTrees", method = RequestMethod.POST)
	@ResponseBody
	public List<Menu> getMenuTrees() throws Exception {
		return menuServiceImpl.getMenuTree();
	}

	/**
	 * 조직에 속한 사용자를 조회.
	 *
	 * @return List<TreeMenu>
	 */
	@RequestMapping(value = "/getTreeMenus", method = RequestMethod.GET)
	@ResponseBody
	public List<Menu> getTreeMenus(Menu menu) throws Exception {
		return menuServiceImpl.getTreeMenuBySearch(menu);
	}

	/**
	 * MSTR 메뉴 트리 목록을 조회.
	 *
	 * @return List<Menu>
	 */
	@RequestMapping(value = "/mstrMenuTrees", method = RequestMethod.POST)
	@ResponseBody
	public List<MstrMenu> getMstrRootTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu) throws Exception {
		return menuServiceImpl.getMstrRootTree(request, response, mstrMenu);
	}

	/**
	 * MSTR 메뉴 트리 목록을 조회.
	 *
	 * @return List<Menu>
	 */
	@RequestMapping(value = "/mstrSubMenuTrees", method = RequestMethod.GET)
	@ResponseBody
	public List<MstrMenu> getSubMenuTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu) throws Exception {
		return menuServiceImpl.getSubMenuTree(request, response, mstrMenu);
	}

	/**
	 * MSTR 메뉴 조회.
	 *
	 * @return List<Menu>
	 */
	@RequestMapping(value = "/mstrMenus", method = RequestMethod.GET)
	@ResponseBody
	public List<Menu> getMstrMenuByCode(Menu menu) throws Exception {
		return menuServiceImpl.getMstrMenuByCode(menu);
	}
}
