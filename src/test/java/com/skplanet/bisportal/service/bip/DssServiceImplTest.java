package com.skplanet.bisportal.service.bip;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.skplanet.bisportal.model.bip.*;
import com.skplanet.bisportal.repository.bip.DssRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.repository.acl.MenuRepository;

/**
 * The DssServiceImplTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class DssServiceImplTest {
	@Mock
	private MenuRepository menuRepository;

	@Mock
	private DssRepository dssRepository;

	@InjectMocks
	private DeptPersonService deptPersonService = new DeptPersonServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetBmList() throws Exception {
		Menu menuParam = new Menu();
		menuParam.setCommonCodeName(CommonCode.MenuCommonCodeName.BM.name());
		menuParam.setParentId(74l);

		// When
		List<TreeMenu> treeMenuList = menuRepository.getTreeMenus(menuParam);

		// Then
		assertThat(treeMenuList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetLatestDss() throws Exception {
		// When
		Dss dsss = dssRepository.getLatestDss();

		// Then
		assertNull(dsss);
	}
}
