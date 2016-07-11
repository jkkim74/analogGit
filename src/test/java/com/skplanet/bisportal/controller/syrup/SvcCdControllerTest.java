package com.skplanet.bisportal.controller.syrup;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.service.syrup.SvcCdService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@Slf4j
@Transactional
public class SvcCdControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private SvcCdController svcCdController;

    @Mock
    private SvcCdService svcCdServiceImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = standaloneSetup(svcCdController).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWkStrd() throws Exception {
        SmwCldrWk smwCldrWk = new SmwCldrWk();
        smwCldrWk.setSearchDate("20141208");
        smwCldrWk.setStrdYear("2014");
        List<SmwCldrWk> smwCldrWkList = Lists.newArrayList();
        when(svcCdServiceImpl.getWkStrd(smwCldrWk)).thenReturn(smwCldrWkList);
        this.mockMvc.perform(
            get("/svcCd/wkStrd?searchDate=20141121&strdYear=2014")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)
        );
    }

    @Test
    public void testGetWkStrds() throws Exception {
        SmwCldrWk smwCldrWk = new SmwCldrWk();
        smwCldrWk.setStrdYear("2014");
        List<SmwCldrWk> smwCldrWkList = Lists.newArrayList();
        when(svcCdServiceImpl.getWkStrds(smwCldrWk)).thenReturn(smwCldrWkList);
        this.mockMvc.perform(
            get("/svcCd/wkStrds?strdYear=2014")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().isOk())
            .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8)
        );
    }
}