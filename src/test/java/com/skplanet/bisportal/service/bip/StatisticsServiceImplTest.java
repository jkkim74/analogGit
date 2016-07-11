package com.skplanet.bisportal.service.bip;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.DayVisitor;
import com.skplanet.bisportal.repository.bip.StatisticsRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The StatisticsServiceImplTest class.
 *
 * @author kyoungoh lee
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceImplTest {
    @Mock
    private StatisticsRepository statisticsRepository;

    @InjectMocks
    private StatisticsService statisticsService = new StatisticsServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDayVisitor() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<DayVisitor> dayVisitorList = statisticsService.getDayVisitor(jqGridRequest);
        assertNotNull(dayVisitorList);
    }
}
