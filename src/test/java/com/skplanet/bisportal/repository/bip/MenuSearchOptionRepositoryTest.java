package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.utils.Utils;
import com.skplanet.bisportal.model.bip.CommonCode;
import com.skplanet.bisportal.model.bip.Menu;
import com.skplanet.bisportal.model.bip.MenuSearchOption;
import com.skplanet.bisportal.repository.acl.MenuRepository;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.skplanet.bisportal.testsupport.builder.MenuBuilder.aMenu;
import static com.skplanet.bisportal.testsupport.builder.MenuSearchOptionBuilder.aMenuSearchOption;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * The MenuSearchOptionRepositoryTestTest class.
 *
 * @author sjune
 */
@Transactional
public class MenuSearchOptionRepositoryTest extends AbstractContextLoadingTest {

	@Autowired
	MenuSearchOptionRepository repository;
	@Autowired
	MenuRepository menuRepository;
	@Autowired
	CommonCodeRepository commonCodeRepository;

	@Test
	public void testUpdateMenuSearchOption() throws Exception {
		// Given (insert)
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RS");
		Menu menu = aMenu().code("ocb-test").name("OK 캐시백").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(25L).orderIdx(30).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate()).auditId("TEST")
				.menuSearchOptionYn("Y").build();

		menuRepository.createMenu(menu);

		MenuSearchOption menuSearchOption = aMenuSearchOption().menuId(menu.getId()).addType("select")
				.calendarType("period").codeUrl("/commonCode/measure/visitMeasureCodes").label("Measure")
				.dateTypes("day,week,month").lastUpdate(Utils.getCreateDate()).build();

		// When & Then
		repository.updateMenuSearchOption(menuSearchOption);

		MenuSearchOption actual = repository.getMenuSearchOptionByMenuId(menuSearchOption.getMenuId());
		assertThat(actual.getAddType(), is("select"));
		assertThat(actual.getCalendarType(), is("period"));
		assertThat(actual.getDateTypes(), is("day,week,month"));
		assertThat(actual.getCodeUrl(), is("/commonCode/measure/visitMeasureCodes"));
		assertThat(actual.getLabel(), is("Measure"));

		// Given (for update)
		menuSearchOption.setDateTypes("month");
		menuSearchOption.setCalendarType("single");

		// When & Then
		repository.updateMenuSearchOption(menuSearchOption);

		MenuSearchOption actual2 = repository.getMenuSearchOptionByMenuId(menuSearchOption.getMenuId());
		assertThat(actual2.getDateTypes(), is("month"));
		assertThat(actual2.getCalendarType(), is("single"));
	}

	@Test
	public void testDeleteMenuSearchOption() throws Exception {
		// Given (insert)
		CommonCode commonCode = commonCodeRepository.getCommonCodeByName("RS");
		Menu menu = aMenu().code("ocb-test").name("OK 캐시백").imageUrl("resources/images/tit/logo_okcashbag.png")
				.oprSvcId(25L).orderIdx(30).commonCodeId(commonCode.getId()).lastUpdate(Utils.getCreateDate())
				.menuSearchOptionYn("Y").auditId("TEST").build();

		menuRepository.createMenu(menu);

		MenuSearchOption menuSearchOption = aMenuSearchOption().menuId(menu.getId()).addType("select")
				.calendarType("period").codeUrl("/commonCode/measure/visitMeasureCodes").label("Measure")
				.dateTypes("day,week,month").lastUpdate(Utils.getCreateDate()).build();

		// When
		repository.deleteMenuSearchOption(menuSearchOption.getMenuId());

		// Then
		assertNull(repository.getMenuSearchOptionByMenuId(menuSearchOption.getMenuId()));
	}
}
