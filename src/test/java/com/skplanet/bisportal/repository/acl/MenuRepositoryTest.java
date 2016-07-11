package com.skplanet.bisportal.repository.acl;

import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.CommonCodeRepository;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.skplanet.bisportal.testsupport.builder.MenuBuilder.aMenu;
import static junit.framework.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * The MenuRepositoryTest class.
 * 
 * @author sjune
 */
@Slf4j
@Transactional
public class MenuRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	MenuRepository repository;
	@Autowired
	CommonCodeRepository commonCodeRepository;

	@Test
	public void testGetMenuById() throws Exception {
		// Given
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RS");
		Menu paramMenu = aMenu().code("ocb-test").name("OK 캐시백").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(25L).orderIdx(30).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		Menu menu = createMenu(paramMenu);

		// When
		Menu menuById = repository.getMenuById(menu.getId());

		// Then
		assertNotNull(menuById);
	}

	@Test
	public void testGetTreeMenuServiceMap() {
		// Given
		Menu paramMenu = aMenu().visibleYn("Y").deleteYn("N").commonCodeName("RS").build();

		// When
		Map<Long, TreeMenuService> actual = repository.getTreeMenuServiceMap(paramMenu);

		// Then
		for (TreeMenuService service : actual.values()) {
			assertThat(service.getVisibleYn(), is("Y"));
			assertThat(service.getDeleteYn(), is("N"));
			assertThat(service.getCommonCodeName(), is("RS"));
		}
	}

	@Test
	public void testGetTreeMenuCategoryMap() {
		// Given
		Menu paramMenu = aMenu().visibleYn("Y").deleteYn("N").commonCodeName("RC").build();

		// When
		Map<Long, TreeMenuCategory> actual = repository.getTreeMenuCategoryMap(paramMenu);

		// Then
		for (TreeMenuCategory category : actual.values()) {
			assertThat(category.getVisibleYn(), is("Y"));
			assertThat(category.getDeleteYn(), is("N"));
			assertThat(category.getCommonCodeName(), is("RC"));
		}
	}

	@Test
	public void testGetTreeMenuWithMenuSearchOption() {
		// Given
		Menu paramMenu = aMenu().visibleYn("Y").deleteYn("N").commonCodeName("RM").build();

		// When
		List<TreeMenu> actual = repository.getTreeMenuWithMenuSearchOption(paramMenu);

		// Then
		for (TreeMenu menu : actual) {
			assertThat(menu.getVisibleYn(), is("Y"));
			assertThat(menu.getDeleteYn(), is("N"));
			assertThat(menu.getCommonCodeName(), is("RM"));

//			if ("Y".equals(menu.getMenuSearchOptionYn())) {
//				assertNotNull(menu.getMenuSearchOption());
//			}
		}
	}

	@Test
	public void testUpdateMenu() throws Exception {
		// Given
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RS");
		Menu paramMenu = aMenu().code("ocb-test").name("OK 캐시백").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(25L).orderIdx(30).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate()).auditId("TEST").build();

		Menu createdMenu = createMenu(paramMenu);
		if (createdMenu.getOprSvcId() != 25L || createdMenu.getOrderIdx() != 30) {
			fail();
		}

		// When
        createdMenu.setOprSvcId(99L);
		createdMenu.setOrderIdx(31);
		repository.updateMenu(createdMenu);

		// Then
		Menu menuById = repository.getMenuById(createdMenu.getId());
		assertThat(menuById.getOprSvcId(), is(99L));
		assertThat(menuById.getOrderIdx(), is(31));
	}

	private Menu createMenu(Menu menu) {
		repository.createMenu(menu);
		if (menu.getId() == null) {
			fail();
		}
		return menu;
	}
}
