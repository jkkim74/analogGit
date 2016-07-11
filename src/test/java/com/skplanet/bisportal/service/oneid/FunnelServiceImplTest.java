package com.skplanet.bisportal.service.oneid;

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

import com.skplanet.bisportal.model.oneid.FunnelRequest;
import com.skplanet.bisportal.model.oneid.OidUsrProc;
import com.skplanet.bisportal.repository.oneid.OidUsrProcRepository;

/**
 * The FunnelServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class FunnelServiceImplTest {
    @Mock
    private OidUsrProcRepository oidUsrProcRepository;

    @InjectMocks
    private FunnelService funnelService = new FunnelServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetOidFunnels() throws Exception {
        FunnelRequest funnelRequest = new FunnelRequest();
        funnelRequest.setStartDate("20150101");
        funnelRequest.setEndDate("20151231");
        funnelRequest.setSvcId("23");
        funnelRequest.setProcCd("p1");
        funnelRequest.setPoc("m");
        List<OidUsrProc> oidUsrProcs = oidUsrProcRepository.getOidUsrProcs(funnelRequest);
        assertNotNull(oidUsrProcs);
        assertThat(oidUsrProcs.size(), greaterThanOrEqualTo(0));
    }
}
