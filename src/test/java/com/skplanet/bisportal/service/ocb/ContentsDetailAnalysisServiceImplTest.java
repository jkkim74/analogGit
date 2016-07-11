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
import com.skplanet.bisportal.model.ocb.ObsFeedSta;
import com.skplanet.bisportal.model.ocb.ObsStoreDetlSta;
import com.skplanet.bisportal.model.ocb.ObsStoreOvrlSta;
import com.skplanet.bisportal.repository.ocb.ObsFeedStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsStoreDetlStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsStoreOvrlStaRepository;

/**
 * The ContentsDetailAnalysisServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class ContentsDetailAnalysisServiceImplTest {
    @Mock
    private ObsFeedStaRepository obsFeedStaRepository;

    @Mock
    private ObsStoreOvrlStaRepository obsStoreOvrlStaRepository;

    @Mock
    private ObsStoreDetlStaRepository obsStoreDetlStaRepository;

    @InjectMocks
    private ContentsDetailAnalysisService contentsDetailAnalysisService = new ContentsDetailAnalysisServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetFeedsExposureForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsFeedSta> obsFeedStaList = obsFeedStaRepository.getFeedsExposureForGridPerDay(jqGridRequest);
        assertNotNull(obsFeedStaList);
        assertThat(obsFeedStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetFeedsExposureOrderForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsFeedSta> obsFeedStaList = obsFeedStaRepository.getFeedsExposureOrderForPivotPerDay(jqGridRequest);
        assertNotNull(obsFeedStaList);
        assertThat(obsFeedStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetFeedsExposureOrderForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsFeedSta> obsFeedStaList = obsFeedStaRepository.getFeedsExposureOrderForGridPerDay(jqGridRequest);
        assertNotNull(obsFeedStaList);
        assertThat(obsFeedStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetStoreTotalForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsStoreOvrlSta> obsStoreOvrlStaList = obsStoreOvrlStaRepository.getStoreTotalForPivotPerDay(jqGridRequest);
        assertNotNull(obsStoreOvrlStaList);
        assertThat(obsStoreOvrlStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetStoreSingleForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsStoreDetlSta> obsStoreDetlStaList = obsStoreDetlStaRepository.getStoreSingleForPivotPerDay(
                jqGridRequest);
        assertNotNull(obsStoreDetlStaList);
        assertThat(obsStoreDetlStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetStoreSingleForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsStoreDetlSta> obsStoreDetlStaList = obsStoreDetlStaRepository.getStoreSingleForGridPerDay(jqGridRequest);
        assertNotNull(obsStoreDetlStaList);
        assertThat(obsStoreDetlStaList.size(), greaterThanOrEqualTo(0));
    }
}
