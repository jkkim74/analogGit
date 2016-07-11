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

import com.skplanet.bisportal.model.bip.CommonGroupCode;
import com.skplanet.bisportal.repository.bip.CommonGroupCodeRepository;

/**
 * The CommonGroupCodeServiceTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class CommonGroupCodeServiceImplTest {
	@Mock
	private CommonGroupCodeRepository commonGroupCodeRepository;

	@InjectMocks
	private CommonGroupCodeService commonGroupCodeService = new CommonGroupCodeServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetCodesByGroupCodeId() throws Exception {
		// When
		List<CommonGroupCode> groupCodes = commonGroupCodeRepository.getCodesByGroupCodeId("HD_SEARCH_TYPE");

		// Then
		assertThat(groupCodes.size(), greaterThanOrEqualTo(0));
	}
}
