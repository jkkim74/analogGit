package com.skplanet.bisportal.service.acl;

import com.google.common.collect.Lists;
import com.microstrategy.web.objects.WebDisplayUnit;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.skplanet.bisportal.common.consts.Constants;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.model.mstr.MstrMenu;
import com.skplanet.bisportal.model.mstr.MstrRequest;
import com.skplanet.bisportal.model.mstr.MstrResponse;
import com.skplanet.bisportal.repository.acl.MenuRepository;
import com.skplanet.bisportal.repository.acl.UserRepository;
import com.skplanet.bisportal.repository.bip.FavoriteRepository;
import com.skplanet.bisportal.repository.bip.MenuSearchOptionRepository;
import com.skplanet.bisportal.service.mstr.MstrService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * The MenuServiceImpl class.
 *
 * @author sjune
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private MenuSearchOptionRepository menuSearchOptionRepository;
	@Autowired
	private MstrService mstrServiceImpl;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FavoriteRepository favoriteRepository;

	/**
	 * 리포트 메뉴 데이터 조회
	 *
	 * @param menu
	 * @return TreeMenuService of Collection
	 * @throws Exception
	 */
	@Override
	public List<TreeMenuService> getReportServices(Menu menu) throws Exception {
		// menu.setDeleteYn(Constants.NO);
		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.RS.name());
		Map<Long, TreeMenuService> reportService = menuRepository.getTreeMenuServiceMap(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.RC.name());
		Map<Long, TreeMenuCategory> reportCategories = menuRepository.getTreeMenuCategoryMap(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.RM.name());
		List<TreeMenu> reportMenus = menuRepository.getTreeMenuWithMenuSearchOption(menu);
		// menus를 categories에 담는다.
		addToCategory(reportCategories, reportMenus);
		// categories를 services에 담는다.
		addToService(reportService, reportCategories);
		return Utils.mapToList(reportService);
	}

	/**
	 * 대시보드 메뉴 데이터 조회
	 *
	 * @param menu
	 * @return TreeMenuService
	 * @throws Exception
	 */
	@Override
	public TreeMenuService getDashboardService(Menu menu) throws Exception {
		// menu.setDeleteYn(Constants.NO);
		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.BS.name());
		TreeMenuService dashboardService = menuRepository.getTreeMenuService(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.BC.name());
		Map<Long, TreeMenuCategory> dashboardCategories = menuRepository.getTreeMenuCategoryMap(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.BM.name());
		List<TreeMenu> dashboardMenus = menuRepository.getTreeMenus(menu);
		// menus를 categories에 담는다.
		addToCategory(dashboardCategories, dashboardMenus);
		// category를 service에 담는다.
		List<TreeMenuCategory> categories = Utils.mapToList(dashboardCategories);
		dashboardService.setCategories(categories);
		return dashboardService;
	}

	/**
	 * 관리자 페이지 메뉴 데이터 조회
	 *
	 * @param menu
	 * @return TreeMenuService
	 * @throws Exception
	 */
	@Override
	public TreeMenuService getAdminService(Menu menu) throws Exception {
		// menu.setDeleteYn(Constants.NO);
		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.AS.name());
		TreeMenuService adminService = menuRepository.getTreeMenuService(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.AC.name());
		Map<Long, TreeMenuCategory> adminCategories = menuRepository.getTreeMenuCategoryMap(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.AM.name());
		List<TreeMenu> adminMenus = menuRepository.getTreeMenus(menu);
		// menus를 categories에 담는다.
		addToCategory(adminCategories, adminMenus);
		// category를 service에 담는다.
		List<TreeMenuCategory> categories = Utils.mapToList(adminCategories);
		adminService.setCategories(categories);
		return adminService;
	}

	/**
	 * HelpDesk 페이지 메뉴 데이터 조회
	 *
	 * @param menu
	 * @return TreeMenuService
	 * @throws Exception
	 */
	@Override
	public TreeMenuService getHelpDeskService(Menu menu) throws Exception {
		// menu.setDeleteYn(Constants.NO);
		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.HS.name());
		TreeMenuService helpDeskService = menuRepository.getTreeMenuService(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.HC.name());
		Map<Long, TreeMenuCategory> helpDeskCategories = menuRepository.getTreeMenuCategoryMap(menu);

		menu.setCommonCodeName(CommonCode.MenuCommonCodeName.HM.name());
		List<TreeMenu> helpDeskMenus = menuRepository.getTreeMenus(menu);
		// menus 를 categories에 담는다.
		addToCategory(helpDeskCategories, helpDeskMenus);
		// category를 service에 담는다.
		List<TreeMenuCategory> categories = Utils.mapToList(helpDeskCategories);
		helpDeskService.setCategories(categories);
		return helpDeskService;
	}

	/**
	 * Navigation 메뉴의 권한 처리를 위한 정보 조회
	 *
	 * @param loginId
	 * @return Navigation
	 * @throws Exception
	 */
	@Override
	public Navigation getNavigationMenu(String loginId) throws Exception {
		List<Menu> menus = menuRepository.getNavigationMenu(loginId);
		Navigation navigation = new Navigation();
		navigation.setAdminMenu(menus.get(0).getAuthority());
		navigation.setSsbiMenu(menus.get(1).getAuthority());
		navigation.setSsbiEduMenu(menus.get(2).getAuthority());
		navigation.setHelpDeskAdmin(menus.get(3).getAuthority());
		return navigation;
	}

	/**
	 * 메뉴 변경 처리.
	 *
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateMenus(List<Menu> menus) throws Exception {
		String toDate = Utils.getCreateDate();
		for (Menu menu : menus) {
			menu.setLastUpdate(toDate);
			updateMenu(menu);
			updateMenuChildrenOrderIdx(menu);
		}
	}

	/**
	 * 권한 화면에서 요청한 메뉴 변경 처리.
	 *
	 * @param roleRequest
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateMenusByRole(RoleRequest roleRequest) throws Exception {
		String toDate = Utils.getCreateDate();
		for (Menu menu : roleRequest.getMenus()) {
			if (StringUtils.isNotEmpty(menu.getState())) {
				if (StringUtils.equals("활성", menu.getState())) {
					menu.setVisibleYn("Y");
				} else if (StringUtils.equals("비활성", menu.getState())) {
					menu.setVisibleYn("N");
				} else {
					menu.setVisibleYn("P");
				}
			}
			menu.setLastUpdate(toDate);
			menuRepository.updateMenu(menu);
		}
	}

	/**
	 * 메뉴 아이디로 메뉴 정보 조회.
	 *
	 * @param id
	 * @return Menu
	 * @throws Exception
	 */
	@Override
	public Menu getMenuById(Long id) {
		return menuRepository.getMenuById(id);
	}

	/**
	 * 메뉴 순서 정보 변경 처라.
	 *
	 * @param parentMenu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateMenuChildrenOrderIdx(Menu parentMenu) {
		menuRepository.updateMenuChildrenOrderIdx(parentMenu);
	}

	/**
	 * 메뉴 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void createMenu(Menu menu) throws Exception {
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
	}

	/**
	 * 리포트 서비스 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addReportService(Menu menu) throws Exception {
		menu.setCommonCodeId(5l);
		menu.setOrderIdx(menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu) + 1);
		menu.setDeleteYn(Constants.NO);
		menu.setMenuSearchOptionYn(Constants.NO);
		menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
	}

	/**
	 * 리포트 카테고리 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addReportCategory(Menu menu) throws Exception {
		menu.setCommonCodeId(6l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		if (maxOrderIdx == null) {
			Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
		}
		if (StringUtils.isEmpty(menu.getCode())) {
			Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
			menu.setCode(String.format("%02d", maxCode + 1));
		}
		menu.setDeleteYn(Constants.NO);
		menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
	}

	/**
	 * 관리자 페이지 카테고리 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addAdminCategory(Menu menu) throws Exception {
		menu.setCommonCodeId(9l);
		menu.setParentId(83l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		if (maxOrderIdx == null) {
			Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
		}
		if (StringUtils.isEmpty(menu.getCode())) {
			Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
			menu.setCode(String.format("%02d", maxCode + 1));
		}
		menu.setDeleteYn(Constants.NO);
		menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
	}

	/**
	 * 리포트의 메뉴 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addReportMenu(Menu menu) throws Exception {
		menu.setCommonCodeId(7l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
		if (maxOrderIdx == null) {
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
			if (StringUtils.isEmpty(menu.getCode())) {
				menu.setCode(existsMenu.getCode() + "01");
			}
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
			if (StringUtils.isEmpty(menu.getCode())) {
				Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
				if (maxCode == 0) {
					menu.setCode(existsMenu.getCode() + "01");
				} else {
					menu.setCode(String.format("%04d", maxCode + 1));
				}
			}
		}
		menu.setDeleteYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getMenuSearchOptionYn()))
			menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
		if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
			MenuSearchOption menuSearchOption = menu.getMenuSearchOption();
			menuSearchOption.setMenuId(menu.getId());
			menuSearchOption.setLastUpdate(menu.getLastUpdate());
			menuSearchOption.setAuditId(menu.getAuditId());
			menuSearchOptionRepository.updateMenuSearchOption(menuSearchOption);
			// 리포트 전체 공개 권한 메뉴에 추가.
			int orderIdx = menu.getOrderIdx().intValue() / 10000;
			if (StringUtils.equals("mstr", menuSearchOption.getAddType()) &&
					StringUtils.equals(menu.getVisibleYn(), Constants.YES) &&
					!StringUtils.equals("report", menu.getCode())) {
				ComRoleMenu comRoleMenu = new ComRoleMenu();
				if (orderIdx == 44) {
					comRoleMenu.setRoleId(12l);
				} else {
					comRoleMenu.setRoleId(0l);
				}
				comRoleMenu.setMenuId(menu.getId());
				comRoleMenu.setAuditId(menu.getAuditId());
				userRepository.createComRoleMenu(comRoleMenu);
			}
		}
	}

	/**
	 * 대시보드 카테고리 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addDashboardCategory(Menu menu) throws Exception {
		menu.setCommonCodeId(3l);
		menu.setParentId(73l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		if (maxOrderIdx == null) {
			Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
		}
		if (StringUtils.isEmpty(menu.getCode())) {
			Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
			menu.setCode(String.format("%02d", maxCode + 1));
		}
		menu.setDeleteYn(Constants.NO);
		menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
	}

	/**
	 * 대시보드 메뉴 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addDashboardMenu(Menu menu) throws Exception {
		menu.setCommonCodeId(4l);
		// menu.setParentId(74l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
		if (maxOrderIdx == null) {
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
			if (StringUtils.isEmpty(menu.getCode())) {
				menu.setCode(existsMenu.getCode() + "01");
			}
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
			if (StringUtils.isEmpty(menu.getCode())) {
				Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
				if (maxCode == 0) {
					menu.setCode(existsMenu.getCode() + "01");
				} else {
					menu.setCode(String.format("%04d", maxCode + 1));
				}
			}
		}
		menu.setDeleteYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getMenuSearchOptionYn()))
			menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
		if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
			MenuSearchOption menuSearchOption = menu.getMenuSearchOption();
			menuSearchOption.setMenuId(menu.getId());
			menuSearchOption.setLastUpdate(menu.getLastUpdate());
			menuSearchOption.setAuditId(menu.getAuditId());
			menuSearchOptionRepository.updateMenuSearchOption(menuSearchOption);
		}
	}

	/**
	 * 관리자 페이지 메뉴 생성 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void addAdminMenu(Menu menu) throws Exception {
		menu.setCommonCodeId(10l);
		Integer maxOrderIdx = menuRepository.getMaxOrderIdxByParentIDCommonCodeId(menu);
		Menu existsMenu = menuRepository.getMenuById(menu.getParentId());
		if (maxOrderIdx == null) {
			menu.setOrderIdx(Integer.parseInt(existsMenu.getOrderIdx() + "00"));
			if (StringUtils.isEmpty(menu.getCode())) {
				menu.setCode(existsMenu.getCode() + "01");
			}
		} else {
			menu.setOrderIdx(maxOrderIdx + 1);
			if (StringUtils.isEmpty(menu.getCode())) {
				Integer maxCode = menuRepository.getMaxCodeByParentId(menu);
				if (maxCode == 0) {
					menu.setCode(existsMenu.getCode() + "01");
				} else {
					menu.setCode(String.format("%04d", maxCode + 1));
				}
			}
		}
		menu.setDeleteYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getMenuSearchOptionYn()))
			menu.setMenuSearchOptionYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getSummaryReportYn()))
			menu.setSummaryReportYn(Constants.NO);
		if (StringUtils.isEmpty(menu.getVisibleYn()))
			menu.setVisibleYn(Constants.NO);
		menu.setLastUpdate(Utils.getCreateDate());
		menuRepository.createMenu(menu);
		if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
			MenuSearchOption menuSearchOption = menu.getMenuSearchOption();
			menuSearchOption.setMenuId(menu.getId());
			menuSearchOption.setLastUpdate(menu.getLastUpdate());
			menuSearchOption.setAuditId(menu.getAuditId());
			menuSearchOptionRepository.updateMenuSearchOption(menuSearchOption);
		}
	}

	/**
	 * 메뉴 변경 처리.
	 *
	 * @param menu
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional
	public void updateMenu(Menu menu) throws Exception {
		MenuSearchOption menuSearchOption;
		if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
			menuSearchOption = menu.getMenuSearchOption();
		} else {
			menuSearchOption = new MenuSearchOption();
		}
		menuSearchOption.setMenuId(menu.getId());
		menuSearchOption.setLastUpdate(Utils.getCreateDate());
		menuSearchOption.setAuditId(menu.getAuditId());
		if (StringUtils.equals(Constants.DELETED, menu.getVisibleYn())) {
			if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
				menuSearchOptionRepository.createMenuSearchOptionBack(menuSearchOption);
				menuSearchOptionRepository.deleteMenuSearchOption(menu.getId());
			}
			menuRepository.createMenuBack(menu);
			menuRepository.deleteMenu(menu.getId());
			if (menu.getCommonCodeId() == 4 || menu.getCommonCodeId() == 7 ||
					menu.getCommonCodeId() == 10) {
				// favorite 테이블 삭제
				Favorite favorite = new Favorite();
				favorite.setMenuId(Long.toString(menu.getId()));
				favorite.setCommonCodeId(menu.getCommonCodeId());
				int favCount = favoriteRepository.getFavoriteCountByCommonCodeIdMenuId(favorite);
				if (favCount > 0) {
					favoriteRepository.deleteFavoriteByCommonCodeIdMenuId(favorite);
				}
				ComRoleMenu comRoleMenu = new ComRoleMenu();
				comRoleMenu.setMenuId(menu.getId());
				userRepository.deleteComRoleMenu(comRoleMenu);
			}
		} else {
			menuRepository.updateMenu(menu);
			ComRoleMenu comRoleMenu = new ComRoleMenu();
			comRoleMenu.setMenuId(menu.getId());
			int orderIdx = menu.getOrderIdx().intValue() / 10000;
			if (Constants.YES.equals(menu.getMenuSearchOptionYn())) {
				menuSearchOptionRepository.updateMenuSearchOption(menuSearchOption);
				if (StringUtils.equals(Constants.YES, menu.getVisibleYn()) &&
					StringUtils.equals("mstr", menuSearchOption.getAddType()) &&
					!StringUtils.equals("report", menu.getCode()) &&
					menu.getCommonCodeId() == 7) {
					// com_role_menu의 리포트 전체 공개 권한에 메뉴 등록.
					if (orderIdx == 44) {// BI EDU용 MSTR
						comRoleMenu.setRoleId(12l);
					} else {// 일반 리포트용 MSTR
						comRoleMenu.setRoleId(0l);
					}
					comRoleMenu.setAuditId(menu.getAuditId());
					userRepository.createComRoleMenu(comRoleMenu);
				}
			} else {
				menuSearchOptionRepository.createMenuSearchOptionBack(menuSearchOption);
				menuSearchOptionRepository.deleteMenuSearchOption(menu.getId());
			}
			if (StringUtils.equals(Constants.NO, menu.getVisibleYn()) && menu.getCommonCodeId() == 7) {
				// com_role_menu의 리포트 전체 공개 권한에서 메뉴 삭제
				userRepository.deleteComRoleMenu(comRoleMenu);
			}
		}
	}

	/**
	 * 권한별 메뉴 목록 조회.
	 *
	 * @param roleRequest
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMenuByBoss(RoleRequest roleRequest) throws Exception {
		return menuRepository.getMenuByBoss(roleRequest);
	}

	private void addToService(Map<Long, TreeMenuService> treeMenuServiceMap,
			Map<Long, TreeMenuCategory> treeMenuCategoryMap) throws Exception {
		List<TreeMenuCategory> categories = Utils.mapToList(treeMenuCategoryMap);
		for (TreeMenuCategory category : categories) {
			if (treeMenuServiceMap.get(category.getParentId()) != null) {
				treeMenuServiceMap.get(category.getParentId()).getCategories().add(category);
			}
		}
	}

	/**
	 * Voyager 메뉴 전체 정보 조회.
	 *
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMenus() throws Exception {
		//List<Menu> menus = menuRepository.getServices();
		return menuRepository.getMenus();
	}

	/**
	 * 권한별 메뉴 정보 조회.
	 *
	 * @param roleRequest
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMenusByRole(RoleRequest roleRequest) throws Exception {
		return menuRepository.getMenusByRole(roleRequest);
	}

	/**
	 * 사용자별 메뉴 정보 조회.
	 *
	 * @param roleRequest
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMenusByLoginIds(RoleRequest roleRequest) throws Exception {
		if (CollectionUtils.isEmpty(roleRequest.getLoginIds())) {
			log.info("loginIds is null");
			return null;
		}
		String[] loginIdArray = roleRequest.getLoginIds().toArray(new String[roleRequest.getLoginIds().size()]);
		return menuRepository.getMenusByLoginIds(loginIdArray);
	}

	/**
	 * 메뉴 트리 생성을 위한 메뉴 정보 조회.
	 *
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMenuTree() throws Exception {
		List<Menu> menus = menuRepository.getMenuTree();
		String key, title;
		Menu menuVo, tree;
		boolean expand = true;
		List<Menu> trees = Lists.newArrayList();
		for (Menu menu : menus) {
			if (StringUtils.isEmpty(menu.getMenuId())) {
				key = Long.toString(menu.getId());
				title = menu.getName();
			} else {
				key = menu.getMenuId();
				title = menu.getMenuNm();
			}
			menuVo = new Menu();
			menuVo.setFolder(true);
			menuVo.setTitle(title);
			menuVo.setKey(key);
			menuVo.setExpand(expand);
			menuVo.setId(menu.getId());
			menuVo.setCode(menu.getCode());
			menuVo.setName(menu.getName());
			menuVo.setMenuId(StringUtils.EMPTY);
			menuVo.setMenuNm(StringUtils.EMPTY);
			menuVo.setVisibleYn(menu.getVisibleYn());
			menuVo.setDeleteYn(menu.getDeleteYn());
			menuVo.setParentId(menu.getParentId());
			menuVo.setCommonCodeId(menu.getCommonCodeId());
			menuVo.setImageUrl(menu.getImageUrl());
			menuVo.setOprSvcId(menu.getOprSvcId());
			// com_menu가 되면 1로 셋팅.
			if (StringUtils.equals("0", menu.getLvl())) {
				trees.add(menuVo);
				expand = false;
			} else {
				int treeSize = trees.size();
				for (int j = 0; j < treeSize; j++) {
					tree = trees.get(j);
					// com_menu로 이전될 경우 제거.
					if (menu.getParentId() == null)
						menu.setParentId(996l);
					if (StringUtils.equals(Long.toString(menu.getParentId()), tree.getKey())) {
						tree.getChildren().add(menuVo);
						trees.add(menuVo);
					}
				}
			}
		}
		int treesSize = trees.size();
		for (int i = treesSize - 1; i > 0; i--) {
			trees.remove(i);
		}

		return trees;
	}

	/**
	 * 메뉴 검색으로 정보 조회.
	 *
	 * @param menu
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getTreeMenuBySearch(Menu menu) {
		if (StringUtils.isNotEmpty(menu.getDeleteYn())) {
			menu.setDeleteYn("Y");
			menu.setVisibleYn(null);
		}
		return menuRepository.getTreeMenuBySearch(menu);
	}

	/**
	 * MSTR ROOT 메뉴 정보 조회.
	 *
	 * @param mstrMenu
	 * @return collection of MstrMenu
	 * @throws Exception
	 */
	@Override
	public List<MstrMenu> getMstrRootTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu)
			throws Exception {
		// mstrMenu.setSessionId(CookieUtils.getMstrCookie(request, mstrMenu.getProjectId()));
		return getSubFolderTree(request, response, mstrMenu);
	}

	/**
	 * MSTR 하위 메뉴 정보 조회.
	 *
	 * @param mstrMenu
	 * @return collection of MstrMenu
	 * @throws Exception
	 */
	@Override
	public List<MstrMenu> getSubMenuTree(HttpServletRequest request, HttpServletResponse response, MstrMenu mstrMenu)
			throws Exception {
		// mstrMenu.setSessionId(CookieUtils.getMstrCookie(request, mstrMenu.getProjectId()));
		return getMstrMenuTree(request, response, mstrMenu);
	}

	private void addToCategory(Map<Long, TreeMenuCategory> treeMenuCategoryMap, List<TreeMenu> menus) {
		for (TreeMenu menu : menus) {
			if (treeMenuCategoryMap.get(menu.getParentId()) != null) {
				treeMenuCategoryMap.get(menu.getParentId()).getMenus().add(menu);
			}
		}
	}

	/**
	 * MSTR 로투 포함/하위 폴더 목록 생성
	 *
	 * @param paramMstr
	 * @return
	 */
	private List<MstrMenu> getSubFolderTree(HttpServletRequest request, HttpServletResponse response, MstrMenu paramMstr) {
		MstrMenu childMenu, mstrMenu;
		boolean expand = true;
		WebObjectInfo obj;
		WebFolder rootFolder;
		List<MstrMenu> trees = null;

		try {
			if (StringUtils.isEmpty(paramMstr.getProjectId())) {
				return null;
			}
			trees = Lists.newArrayList();
			MstrRequest mstrRequest = new MstrRequest();
			mstrRequest.setProjectId(paramMstr.getProjectId());
			MstrResponse serverInfo = mstrServiceImpl.getCheckSession(request, response, mstrRequest);
			WebObjectsFactory objFactory = serverInfo.getObjFactory();
			WebObjectSource objSource = objFactory.getObjectSource();

			obj = objSource.getObject(objSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports),
					EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			rootFolder = (WebFolder) obj;
			rootFolder.populate();
			log.info("rootFolder {}", rootFolder);
			mstrMenu = new MstrMenu();
			mstrMenu.setIsFolder(true);
			mstrMenu.setExpand(expand);
			mstrMenu.setKey(rootFolder.getID());
			mstrMenu.setTitle(rootFolder.getDisplayName());
			mstrMenu.setObjectId(rootFolder.getID());
			mstrMenu.setObjectName(rootFolder.getDisplayName());
			mstrMenu.setObjectType(rootFolder.getType());
			mstrMenu.setParentObjectId(StringUtils.EMPTY);
			mstrMenu.setSubType(rootFolder.getSubType());
			mstrMenu.setLvl("1");
			expand = false;
			int rootFolderSize = rootFolder.size();
			if (rootFolderSize > 0) {
				WebDisplayUnits units = rootFolder.getChildUnits();
				WebObjectInfo childObj;
				WebFolder childFolder;
				int unitSize = units.size();
				for (int i = 0; i < unitSize; i++) {
					WebDisplayUnit child = units.get(i);
					if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
						childObj = objSource.getObject(child.getID(), EnumDSSXMLObjectTypes.DssXmlTypeFolder);
						childFolder = (WebFolder) childObj;
						childFolder.populate();
						log.info("childFolder {}", childFolder);
						childMenu = new MstrMenu();
						childMenu.setIsFolder(true);
						childMenu.setExpand(expand);
						childMenu.setKey(childFolder.getID());
						childMenu.setTitle(childFolder.getDisplayName());
						childMenu.setObjectId(childFolder.getID());
						childMenu.setObjectName(childFolder.getDisplayName());
						childMenu.setObjectType(childFolder.getType());
						childMenu.setParentObjectId(StringUtils.EMPTY);
						childMenu.setSubType(childFolder.getSubType());
						childMenu.setUnitType(childFolder.getDisplayUnitType());
						childMenu.setLvl("2");
						mstrMenu.getChildren().add(childMenu);
					} else if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition ||
							child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
						childObj = objSource.getObject(child.getID(), child.getDisplayUnitType());
						childObj.populate();
						log.info("childObj {}", childObj);
						if (childObj.getSubType() != 768 && childObj.getSubType() != 14081)// 리포트와 대시보드/Document만 메뉴로 등록됨.
							continue;
						childMenu = new MstrMenu();
						childMenu.setIsFolder(false);
						childMenu.setExpand(expand);
						childMenu.setKey(childObj.getID());
						childMenu.setTitle(childObj.getDisplayName());
						childMenu.setObjectId(childObj.getID());
						childMenu.setObjectName(childObj.getDisplayName());
						/**
						 * ophelisis@wiseeco.com
						 *
						 * ObjectType 처리
						 * Report : 3
						 * Document : 55
						 * Dashboard : 99 (원래 55이지만 구분하기 위해 임의로 처리
						 */
						int objectType = childObj.getType();
						if (objectType == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition
								&& (childObj.getViewMediaSettings().getDefaultMode() == 2048 || childObj.getViewMediaSettings().getDefaultMode() == 8192)) {
							objectType = 99;
						}
						childMenu.setObjectType(objectType);
						childMenu.setParentObjectId(mstrMenu.getKey());
						childMenu.setSubType(childObj.getSubType());
						childMenu.setUnitType(childObj.getDisplayUnitType());
						childMenu.setLvl("2");
						mstrMenu.getChildren().add(childMenu);
					}
				}
			}
			trees.add(mstrMenu);
		} catch (Exception e) {
			log.error("getSubFolderTree {}", e);
		}

		return trees;
	}

	/**
	 * MSTR 로투 포함/하위 폴더 목록 생성
	 *
	 * @param paramMstr
	 * @return
	 */
	private List<MstrMenu> getMstrMenuTree(HttpServletRequest request, HttpServletResponse response, MstrMenu paramMstr) {
		MstrMenu mstrMenu;
		boolean expand = false;
		WebObjectInfo obj;
		WebFolder rootFolder;
		List<MstrMenu> trees = null;

		try {
			if (StringUtils.isEmpty(paramMstr.getObjectId())) {
				return null;
			}
			trees = Lists.newArrayList();
			MstrRequest mstrRequest = new MstrRequest();
			mstrRequest.setProjectId(paramMstr.getProjectId());
			MstrResponse serverInfo = mstrServiceImpl.getCheckSession(request, response, mstrRequest);
			WebObjectsFactory objFactory = serverInfo.getObjFactory();
			WebObjectSource objSource = objFactory.getObjectSource();

			obj = objSource.getObject(paramMstr.getObjectId(), EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			rootFolder = (WebFolder) obj;
			rootFolder.populate();
			log.info("rootFolder {}", rootFolder);
			int rootFolderSize = rootFolder.size();
			if (rootFolderSize > 0) {
				WebDisplayUnits units = rootFolder.getChildUnits();
				WebObjectInfo childObj;
				WebFolder childFolder;
				int unitSize = units.size();
				for (int i = 0; i < unitSize; i++) {
					WebDisplayUnit child = units.get(i);
					log.debug("child={}", child);

					if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeFolder) {
						childObj = objSource.getObject(child.getID(), EnumDSSXMLObjectTypes.DssXmlTypeFolder);
						childFolder = (WebFolder) childObj;
						childFolder.populate();
						log.info("childFolder {}", childFolder);
						log.info("getDisplayUnitType {}", child.getDisplayUnitType());
						mstrMenu = new MstrMenu();
						mstrMenu.setIsFolder(true);
						mstrMenu.setExpand(expand);
						mstrMenu.setKey(childFolder.getID());
						mstrMenu.setTitle(childFolder.getDisplayName());
						mstrMenu.setObjectId(childFolder.getID());
						mstrMenu.setObjectName(childFolder.getDisplayName());
						mstrMenu.setObjectType(childFolder.getType());
						mstrMenu.setParentObjectId(paramMstr.getObjectId());
						mstrMenu.setSubType(childFolder.getSubType());
						mstrMenu.setUnitType(childFolder.getDisplayUnitType());
						mstrMenu.setLvl("3");
						trees.add(mstrMenu);
					} else if (child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition ||
							child.getDisplayUnitType() == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
						childObj = objSource.getObject(child.getID(), child.getDisplayUnitType());
						childObj.populate();
						log.info("childObj {}", childObj);
						if (childObj.getSubType() != 768 && childObj.getSubType() != 14081)
							continue;
						mstrMenu = new MstrMenu();
						mstrMenu.setIsFolder(false);
						mstrMenu.setExpand(expand);
						mstrMenu.setKey(childObj.getID());
						mstrMenu.setTitle(childObj.getDisplayName());
						mstrMenu.setObjectId(childObj.getID());
						mstrMenu.setObjectName(childObj.getDisplayName());
						/**
						 * ophelisis@wiseeco.com
						 *
						 * ObjectType 처리
						 * Report : 3
						 * Document : 55
						 * Dashboard : 99 (원래 55이지만 구분하기 위해 임의로 처리
						 */
						int objectType = childObj.getType();
						if (objectType == EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition
								&& (childObj.getViewMediaSettings().getDefaultMode() == 2048 || childObj.getViewMediaSettings().getDefaultMode() == 8192)) {
							objectType = 99;
						}
						mstrMenu.setObjectType(objectType);
						mstrMenu.setParentObjectId(paramMstr.getObjectId());
						mstrMenu.setSubType(childObj.getSubType());
						mstrMenu.setUnitType(childObj.getDisplayUnitType());
						mstrMenu.setLvl("3");
						trees.add(mstrMenu);
					}
				}
			}
		} catch (Exception e) {
			log.error("getMstrMenuTree {}", e);
		}

		return trees;
	}

	/**
	 * 메뉴 코드로 MSTR 메뉴 정보 조회.
	 *
	 * @param menu
	 * @return collection of Menu
	 * @throws Exception
	 */
	@Override
	public List<Menu> getMstrMenuByCode(Menu menu) {
		return menuRepository.getMstrMenuByCode(menu);
	}
}
