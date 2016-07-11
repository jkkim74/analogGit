package com.skplanet.bisportal.controller.syrup;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;
import com.skplanet.bisportal.service.syrup.MbrJoinService;
import com.skplanet.bisportal.testsupport.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Slf4j
@Transactional
public class SyrupDashboardControllerTest {
    private MockMvc mocMvc;

    @InjectMocks
    private SyrupDashboardController syrupDashboardController;

    @Mock
    private MbrJoinService mbrJoinServiceImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mocMvc = standaloneSetup(syrupDashboardController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCustomerSituationPivot() throws Exception {

    }

    @Test
    public void testGetMbrJoinForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setDateType(ReportDateType.DAY);
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        jqGridRequest.setRows(-1);
        jqGridRequest.setSidx("stdDt");
        jqGridRequest.setSord("asc");

        List<SmwMbrJoin> smwMbrJoinList = Lists.newArrayList();
        when(mbrJoinServiceImpl.getMbrJoinForGrid(jqGridRequest)).thenReturn(smwMbrJoinList);
        this.mocMvc.perform(
            get("/syrup/dashboard/mbrjoin/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)
        );
    }

    @Test
    public void testInitBinder() throws Exception {

    }
}