package com.skplanet.bisportal.service.tmap;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.skplanet.bisportal.model.tmap.TmapMonKpiStc;
import com.skplanet.bisportal.repository.tmap.TmapMonKpiStcRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;
import com.skplanet.bisportal.repository.tmap.TmapDayKpiRepository;

/**
 * The KpiServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class KpiServiceImplTest {
    @Mock
    private TmapDayKpiRepository tmapDayKpiRepository;

    @Mock
    private TmapMonKpiStcRepository tmapMonKpiStcRepository;

    @InjectMocks
    private KpiService kpiService = new KpiServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetDayKpiForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setEndDate("20141231");
        jqGridRequest.setKpiCode("0");
        List<TmapDayKpi> tmapDayKpiList = tmapDayKpiRepository.getDayKpiPerDayForGrid(jqGridRequest);
        assertNotNull(tmapDayKpiList);
        assertThat(tmapDayKpiList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetMonthKpiForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<TmapMonKpiStc> tmapMonKpiStcList = tmapMonKpiStcRepository.getMonthKpiManagement(jqGridRequest);
        assertNotNull(tmapMonKpiStcList);
        assertThat(tmapMonKpiStcList.size(), greaterThanOrEqualTo(0));
    }
}
