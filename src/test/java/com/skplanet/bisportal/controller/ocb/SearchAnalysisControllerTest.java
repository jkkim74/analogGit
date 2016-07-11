package com.skplanet.bisportal.controller.ocb;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.skplanet.bisportal.service.ocb.SearchAnalysisService;

/**
 * The SearchAnalysisControllerTest class.
 * 
 * @author cookatrice
 * 
 */
@Slf4j
@Transactional
public class SearchAnalysisControllerTest {
	private MockMvc mockMvc;

	@InjectMocks
	private SearchAnalysisController searchAnalysisController;

	@Mock
	private SearchAnalysisService searchAnalysisServiceImpl;

	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = standaloneSetup(searchAnalysisController).build();
	}

	// TODO Search Analysis controller test

}
