package com.skplanet.bisportal.service.ocb;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsActvCustRpt;
import com.skplanet.bisportal.model.ocb.ObsActvJoinsRpt;
import com.skplanet.bisportal.repository.ocb.ObsActvCustRptRepository;
import com.skplanet.bisportal.repository.ocb.ObsActvJoinsRptRepository;

/**
 * The ActivityAnalysisServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityAnalysisServiceImplTest {
    @Mock
    private ObsActvCustRptRepository obsActvCustRptRepository;

    @Mock
    private ObsActvJoinsRptRepository obsActvJoinsRptRepository;

    @InjectMocks
    private ActivityAnalysisService activityAnalysisService = new ActivityAnalysisServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetActivityCustomerReportForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("2014-01");
        jqGridRequest.setEndDate("2014-12");
        List<ObsActvCustRpt> obsActvCustRptList = obsActvCustRptRepository.getActivityCustomerReportPerMonth(
                jqGridRequest);
        assertNotNull(obsActvCustRptList);
        assertThat(obsActvCustRptList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetActivityJoinsReportForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("2014-01");
        jqGridRequest.setEndDate("2014-12");
        List<ObsActvJoinsRpt> obsActvJoinsRptList = obsActvJoinsRptRepository.getActivityJoinsReportPerMonth(jqGridRequest);
        assertNotNull(obsActvJoinsRptList);
        assertThat(obsActvJoinsRptList.size(), greaterThanOrEqualTo(0));
    }
}
