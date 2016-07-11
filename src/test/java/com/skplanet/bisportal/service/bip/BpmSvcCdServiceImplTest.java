package com.skplanet.bisportal.service.bip;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import com.skplanet.bisportal.repository.bip.BpmSvcCdRepository;

/**
 * The BpmSvcCdServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class BpmSvcCdServiceImplTest {
    @Mock
    private BpmSvcCdRepository bpmSvcCdRepository;

    @InjectMocks
    private BpmSvcCdService bpmSvcCdService = new BpmSvcCdServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetBpmSvcs() throws Exception {
        List<BpmSvcCd> bpmSvcCdList = bpmSvcCdRepository.getBpmSvcs();
        assertNotNull(bpmSvcCdList);
    }

    @Test
    public void testGetBpmCycleToGrps() throws Exception {
        WhereCondition whereCondition = new WhereCondition();
        whereCondition.setSvcId(10l);
        List<BpmSvcCd> bpmSvcCdList = bpmSvcCdRepository.getBpmCycleToGrps(whereCondition);
        assertNotNull(bpmSvcCdList);
    }

    @Test
    public void testGetBpmGrpToCls() throws Exception {
        WhereCondition whereCondition = new WhereCondition();
        whereCondition.setSvcId(10l);
        List<BpmSvcCd> bpmSvcCdList = bpmSvcCdRepository.getBpmGrpToCls(whereCondition);
        assertNotNull(bpmSvcCdList);
    }

    @Test
    public void testGetBpmWkStrds() throws Exception {
        List<BpmSvcCd> bpmSvcCdList = bpmSvcCdRepository.getBpmWkStrds("201401");
        assertNotNull(bpmSvcCdList);
    }
}
