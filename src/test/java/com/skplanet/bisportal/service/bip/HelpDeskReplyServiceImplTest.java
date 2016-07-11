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
import com.skplanet.bisportal.model.bip.HelpDeskReply;
import com.skplanet.bisportal.repository.bip.HelpDeskReplyRepository;

/**
 * The HelpDeskReplyServiceImplTest class.
 * 
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class HelpDeskReplyServiceImplTest {
	@Mock
	private HelpDeskReplyRepository helpDeskReplyRepository;

	@InjectMocks
	private HelpDeskReplyService helpDeskReplyService = new HelpDeskReplyServiceImpl();

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void testSelectHelpDeskQnaReply() throws Exception {
		HelpDeskRequest helpDeskRequest = new HelpDeskRequest();
		helpDeskRequest.setId("9");

		// When
		List<HelpDeskReply> helpDeskReplyList = helpDeskReplyRepository.selectHelpDeskQnaReply(helpDeskRequest);

		// Then
		assertThat(helpDeskReplyList.size(), greaterThanOrEqualTo(0));
	}
}
