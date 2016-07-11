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
import com.skplanet.bisportal.model.ocb.ObsVstLngSta;
import com.skplanet.bisportal.model.ocb.ObsVstPageSta;
import com.skplanet.bisportal.model.ocb.ObsVstSexAgeSta;
import com.skplanet.bisportal.model.ocb.ObsVstSta;
import com.skplanet.bisportal.repository.ocb.ObsVstLngStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsVstPageStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsVstSexAgeStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsVstStaRepository;

/**
 * The VisitServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class VisitServiceImplTest {
    @Mock
    private ObsVstStaRepository obsVstStaRepository;

    @Mock
    private ObsVstSexAgeStaRepository obsVstSexAgeStaRepository;

    @Mock
    private ObsVstLngStaRepository obsVstLngStaRepository;

    @Mock
    private ObsVstPageStaRepository obsVstPageStaRepository;

    @InjectMocks
    private VisitService visitService = new VisitServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetVisitorListForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsVstSta> obsVstStaList = obsVstStaRepository.getVisitorListForGridPerDay(jqGridRequest);
        assertNotNull(obsVstStaList);
        assertThat(obsVstStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetVisitSexAgeForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsVstSexAgeSta> obsVstSexAgeStaList = obsVstSexAgeStaRepository.getVisitorsSexAgePerDay(jqGridRequest);
        assertNotNull(obsVstSexAgeStaList);
        assertThat(obsVstSexAgeStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetVisitsLangForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsVstLngSta> obsVstLngStaList = obsVstLngStaRepository.getVisitorsLangPerMonth(jqGridRequest);
        assertNotNull(obsVstLngStaList);
        assertThat(obsVstLngStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetVisitorsPageForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsVstPageSta> obsVstPageStaList = obsVstPageStaRepository.getVisitorsPagePerMonth(jqGridRequest);
        assertNotNull(obsVstPageStaList);
        assertThat(obsVstPageStaList.size(), greaterThanOrEqualTo(0));
    }
}
