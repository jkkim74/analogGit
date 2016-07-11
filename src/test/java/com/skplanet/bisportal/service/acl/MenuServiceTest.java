package com.skplanet.bisportal.service.acl;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.CommonCodeRepository;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.skplanet.bisportal.testsupport.builder.MenuBuilder.aMenu;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * The MenuServiceTest class
 *
 * @author sjune
 */
@Slf4j
@Transactional
public class MenuServiceTest extends AbstractContextLoadingTest {

	@Autowired
	MenuService menuServiceImpl;
	@Autowired
	CommonCodeRepository commonCodeRepository;

	@Test
	public void testGetReportServices() throws Exception {
		// When
		Menu reqMenu = new Menu();
		reqMenu.setLoginId("PP39093");
		List<TreeMenuService> treeMenuServices = menuServiceImpl.getReportServices(reqMenu);
		// Then
		for (TreeMenuService service : treeMenuServices) {
			assertNotNull(service.getCode());
			assertNotNull(service.getName());
			assertNotNull(service.getCategories());

			for (TreeMenuCategory category : service.getCategories()) {
				assertNotNull(category.getCode());
				assertNotNull(category.getName());
				assertNotNull(category.getMenus());

				for (TreeMenu menu : category.getMenus()) {
					assertNotNull(menu.getCode());
					assertNotNull(menu.getName());
				}
			}
		}
	}

	@Test
	public void testGetDashboardService() throws Exception {
		// When
		Menu reqMenu = new Menu();
		reqMenu.setLoginId("PP39093");
		TreeMenuService treeMenuService = menuServiceImpl.getDashboardService(reqMenu);

		// Then
		assertNotNull(treeMenuService.getCode());
		assertNotNull(treeMenuService.getName());
		assertNotNull(treeMenuService.getCategories());

		for (TreeMenuCategory category : treeMenuService.getCategories()) {
			assertNotNull(category.getCode());
			assertNotNull(category.getName());
			assertNotNull(category.getMenus());

			for (TreeMenu menu : category.getMenus()) {
				assertNotNull(menu.getCode());
				assertNotNull(menu.getName());
			}
		}

	}

	@Test
	public void testGetAdminService() throws Exception {
		// When
		Menu reqMenu = new Menu();
		reqMenu.setLoginId("PP39093");
		TreeMenuService treeMenuService = menuServiceImpl.getAdminService(reqMenu);

		// Then
		assertNotNull(treeMenuService.getCode());
		assertNotNull(treeMenuService.getName());
		assertNotNull(treeMenuService.getCategories());

		for (TreeMenuCategory category : treeMenuService.getCategories()) {
			assertNotNull(category.getCode());
			assertNotNull(category.getName());
			assertNotNull(category.getMenus());

			for (TreeMenu menu : category.getMenus()) {
				assertNotNull(menu.getCode());
				assertNotNull(menu.getName());
			}
		}
	}

	@Test
	public void testUpdateMenus() throws Exception {
		// Given
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RS");
		Menu menu1 = aMenu().code("test1").name("테스트 서비스1").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(25L).orderIdx(30).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate()).auditId("TEST").build();
		Menu menu2 = aMenu().code("test2").name("테스트 서비스2").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(26L).orderIdx(31).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		menuServiceImpl.createMenu(menu1);
		menuServiceImpl.createMenu(menu2);

		// When
		ArrayList<Menu> menus = Lists.newArrayList(menu1, menu2);
		menu1.setName("테스트 서비스3");
		menu2.setName("테스트 서비스3");
		menuServiceImpl.updateMenus(menus);

		// Then
		assertThat(menuServiceImpl.getMenuById(menu1.getId()).getName(), is("테스트 서비스3"));
		assertThat(menuServiceImpl.getMenuById(menu2.getId()).getName(), is("테스트 서비스3"));
	}

	@Test
	public void testUpdateChildMenuOrderIdx1() throws Exception {
		// Given
		Menu service = aMenu().code("test-service1").name("테스트 서비스1")
				.imageUrl("resources/images/tit/logo_okcashbag.png").oprSvcId(25L).orderIdx(30)
				.commonCodeId(commonCodeRepository.getCommonCodeByName("RS").getId()).lastUpdate(
						Utils.getCreateDate()).auditId("TEST")
				.build();

		menuServiceImpl.createMenu(service);

		Menu category = aMenu().code("test-category1").name("테스트 카테고리1").orderIdx(3000)
				.commonCodeId(commonCodeRepository.getCommonCodeByName("RC").getId()).parentId(service.getId())
				.lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		menuServiceImpl.createMenu(category);

		// When
		service.setOrderIdx(90);
		menuServiceImpl.updateMenu(service);
		menuServiceImpl.updateMenuChildrenOrderIdx(service);

		// Then
		assertThat(menuServiceImpl.getMenuById(category.getId()).getOrderIdx(), is(9000));
	}

	@Test
	public void testUpdateChildrenMenuOrderIdx2() throws Exception {
		// Given
		Menu service = aMenu().code("test-service2").name("테스트 서비스2").imageUrl(
				"resources/images/tit/logo_okcashbag.png").oprSvcId(25L).orderIdx(10).commonCodeId(
				commonCodeRepository.getCommonCodeByName("RS").getId()).lastUpdate(Utils.getCreateDate()).auditId(
				"TEST")
				.build();

		menuServiceImpl.createMenu(service);

		Menu category = aMenu().code("test-category2").name("테스트 카테고리2").orderIdx(3000)
				.commonCodeId(commonCodeRepository.getCommonCodeByName("RC").getId()).parentId(service.getId())
				.lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		menuServiceImpl.createMenu(category);

		Menu menu = aMenu().code("test-menu2").name("테스트 메뉴2").orderIdx(300001)
				.commonCodeId(commonCodeRepository.getCommonCodeByName("RM").getId()).parentId(category.getId())
                .lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		menuServiceImpl.createMenu(menu);

		// When
		category.setOrderIdx(1000);
		menuServiceImpl.updateMenu(category);
		menuServiceImpl.updateMenuChildrenOrderIdx(category);

		// Then
		assertThat(menuServiceImpl.getMenuById(menu.getId()).getOrderIdx(), is(100001));
	}
}
