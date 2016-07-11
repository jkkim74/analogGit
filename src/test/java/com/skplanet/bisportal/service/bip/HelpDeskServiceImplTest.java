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

import com.skplanet.bisportal.common.model.HelpDeskRequest;
import com.skplanet.bisportal.model.bip.HelpDesk;
import com.skplanet.bisportal.repository.bip.HelpDeskRepository;

/**
 * The HelpDeskServiceImplTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class HelpDeskServiceImplTest {
	@Mock
	private HelpDeskRepository helpDeskRepository;

	@InjectMocks
	private HelpDeskService helpDeskService = new HelpDeskServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testGetHelpDeskNoticeList() throws Exception {
		HelpDeskRequest helpDeskRequest = new HelpDeskRequest();
		helpDeskRequest.setPageSize(1);

		// When
		List<HelpDesk> helpDeskList = helpDeskRepository.getHelpDeskNoticeList(helpDeskRequest);

		// Then
		assertThat(helpDeskList.size(), greaterThanOrEqualTo(0));
	}
}
