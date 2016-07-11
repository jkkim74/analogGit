package com.skplanet.bisportal.controller.syrup;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
import com.skplanet.bisportal.service.syrup.EtcService;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
@Transactional
public class EtcControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private EtcController etcController;

    @Mock
    private EtcService etcServiceImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = standaloneSetup(etcController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSmwPushTickerStatDtlGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setDateType(ReportDateType.DAY);
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        jqGridRequest.setRows(-1);
        jqGridRequest.setSidx("stdDt");
        jqGridRequest.setSord("asc");

        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = Lists.newArrayList();
        when(etcServiceImpl.getSmwPushTickerStatDtlGrid(jqGridRequest)).thenReturn(smwPushTickerStatDtlList);
        this.mockMvc.perform(
                get("/syrup/etc/pushTickerStatDtl/grid?dateType=day&startDate=20141129&endDate=20141208&sidx=stdDt&sord=asc")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)
        );
    }

    @Test
    public void testDownloadExcelForPushTickerStat() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setDateType(ReportDateType.DAY);
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        jqGridRequest.setRows(-1);
        jqGridRequest.setSidx("stdDt");
        jqGridRequest.setSord("asc");

        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = Lists.newArrayList();
        when(etcServiceImpl.getSmwPushTickerStatDtlGrid(jqGridRequest)).thenReturn(smwPushTickerStatDtlList);
        this.mockMvc.perform(
            post("/syrup/etc/downloadExcelForPushTickerStat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(("{\"dateType\":\"day\", \"startDate\":\"20141129\", \"endDate\":\"20141208\", \"sidx\":\"stdDt\", \"sord\":\"asc\"}")
            .getBytes()))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(handler().methodName("downloadExcelForPushTickerStat")
        );
    }

    @Test
    public void testInitBinder() throws Exception {

    }
}