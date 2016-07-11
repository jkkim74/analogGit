package com.skplanet.bisportal.service.syrup;

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
import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.SmwAppClickN;
import com.skplanet.bisportal.model.syrup.SmwAppExec;
import com.skplanet.bisportal.model.syrup.SmwSyrupDauFunnels;
import com.skplanet.bisportal.repository.syrup.SmwAppClickNRepository;
import com.skplanet.bisportal.repository.syrup.SmwAppExecRepository;
import com.skplanet.bisportal.repository.syrup.SmwSyrupDauFunnelsRepository;

/**
 * The VisitSituationServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class VisitSituationServiceImplTest {
    @Mock
    private SmwAppExecRepository smwAppExecRepository;

    @Mock
    private SmwAppClickNRepository smwAppClickNRepository;

    @Mock
    private SmwSyrupDauFunnelsRepository smwSyrupDauFunnelsRepository;

    @InjectMocks
    private VisitSituationService visitSituationService = new VisitSituationServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAppVisitSituation() throws Exception {
        OlapDimensionRequest olapDimensionRequest = new OlapDimensionRequest();
        olapDimensionRequest.setStartDate("20140101");
        olapDimensionRequest.setEndDate("20141231");
        List<SmwAppExec> smwAppExecList = smwAppExecRepository.getAppVisitSituationPerDay(olapDimensionRequest);
        assertNotNull(smwAppExecList);
        assertThat(smwAppExecList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetMenuVisitSituation() throws Exception {
        OlapDimensionRequest olapDimensionRequest = new OlapDimensionRequest();
        olapDimensionRequest.setStartDate("20140101");
        olapDimensionRequest.setEndDate("20141231");
        List<SmwAppClickN> smwAppClickNList = smwAppClickNRepository.getMenuVisitSituationPerDay(olapDimensionRequest);
        assertNotNull(smwAppClickNList);
        assertThat(smwAppClickNList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetInflRtVisitSituation() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<SmwSyrupDauFunnels> smwSyrupDauFunnelsList = smwSyrupDauFunnelsRepository.getInflRtVisitSituationPerDay(
                jqGridRequest);
        assertNotNull(smwSyrupDauFunnelsList);
        assertThat(smwSyrupDauFunnelsList.size(), greaterThanOrEqualTo(0));
    }
}
