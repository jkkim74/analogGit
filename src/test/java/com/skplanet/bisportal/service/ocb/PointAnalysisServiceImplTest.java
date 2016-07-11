package com.skplanet.bisportal.service.ocb;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import com.skplanet.bisportal.model.ocb.ObsRsrvPntRpt;
import com.skplanet.bisportal.model.ocb.ObsTotPntRpt;
import com.skplanet.bisportal.repository.ocb.ObsRsrvPntRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsTotPntRptRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import com.skplanet.bisportal.repository.ocb.ObsNewOcbSegRptRepository;

/**
 * The PointAnalysisServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class PointAnalysisServiceImplTest {
    @Mock
    private ObsTotPntRptRepository obsTotPntRptRepository;

    @Mock
    private ObsRsrvPntRptRepository obsRsrvPntRptRepository;

    @InjectMocks
    private PointAnalysisService pointAnalysisService = new PointAnalysisServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetTotalPointReportForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsTotPntRpt> obsTotPntRptList = obsTotPntRptRepository.getTotalPointReport(jqGridRequest);
        assertNotNull(obsTotPntRptList);
        assertThat(obsTotPntRptList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetReservingPointReportForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsRsrvPntRpt> obsRsrvPntRptList = obsRsrvPntRptRepository.getReservingPointReport(jqGridRequest);
        assertNotNull(obsRsrvPntRptList);
        assertThat(obsRsrvPntRptList.size(), greaterThanOrEqualTo(0));
    }
}
