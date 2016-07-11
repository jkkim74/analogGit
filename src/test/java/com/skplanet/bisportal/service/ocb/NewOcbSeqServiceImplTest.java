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
import com.skplanet.bisportal.model.ocb.ObsNewOcbSegRpt;
import com.skplanet.bisportal.repository.ocb.ObsNewOcbSegRptRepository;

/**
 * The NewOcbSeqServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class NewOcbSeqServiceImplTest {
    @Mock
    private ObsNewOcbSegRptRepository obsNewOcbSegRptRepository;

    @InjectMocks
    private NewOcbSegService newOcbSegService = new NewOcbSeqServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetNewOcbSegForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setEndDate("201412");
        List<ObsNewOcbSegRpt> obsNewOcbSegRptList = obsNewOcbSegRptRepository.getNewOcbSegPerMonth(jqGridRequest);
        assertNotNull(obsNewOcbSegRptList);
        assertThat(obsNewOcbSegRptList.size(), greaterThanOrEqualTo(0));
    }
}
