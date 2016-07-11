package com.skplanet.bisportal.controller.bip;

import com.skplanet.bisportal.service.bip.StatisticsService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * The StatisticsControllerTest class.
 *
 * @author kyoungoh lee
 */
public class StatisticsControllerTest {
    MockMvc mockMvc;
    @InjectMocks
    StatisticsController statisticsController;

    @Mock
    StatisticsService statisticsServiceImpl;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.mockMvc = standaloneSetup(statisticsController).alwaysExpect(handler().handlerType(StatisticsController.class)).build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDayVisitor() throws Exception {
        this.mockMvc.perform(
        get("/statistics/dayVisitor/?startDate=20141003&endDate=20141013&_search=false&nd=1413168398859&rows=-1&page=1&sidx=visitDate&sord=asc").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(handler().methodName("getDayVisitor")
        );
    }

    @Test
    public void testDownloadExcelForDayVisitor() throws Exception {
        this.mockMvc.perform(
            post("/statistics/downloadExcelForDayVisitor").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(handler().methodName("downloadExcelForDayVisitor")
        );
    }
}