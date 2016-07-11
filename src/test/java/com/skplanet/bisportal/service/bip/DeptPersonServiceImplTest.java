package com.skplanet.bisportal.service.bip;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.model.bip.ComDept;
import com.skplanet.bisportal.model.bip.ComPerson;
import com.skplanet.bisportal.repository.bip.DeptPersonRepository;

/**
 * The DeptPersonServiceImplTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class DeptPersonServiceImplTest {
	@Mock
	private DeptPersonRepository deptPersonRepository;

	@InjectMocks
	private DeptPersonService deptPersonService = new DeptPersonServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetDeptInfo() throws Exception {
		// When
		List<ComDept> comDeptList = deptPersonRepository.getDeptInfo("20140101");

		// Then
		assertThat(comDeptList.size(), greaterThanOrEqualTo(0));
	}

	@Test
	public void testGetPersonInfo() throws Exception {
		// When
		List<ComPerson> comPersonList = deptPersonRepository.getPersonInfo("20140101");

		// Then
		assertThat(comPersonList.size(), greaterThanOrEqualTo(0));
	}
}
