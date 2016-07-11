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
import com.skplanet.bisportal.model.ocb.BleNewTech;
import com.skplanet.bisportal.repository.ocb.BleNewTechRepository;

/**
 * The BleServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class BleServiceImplTest {
    @Mock
    private BleNewTechRepository bleNewTechRepository;

    @InjectMocks
    private BleService bleService = new BleServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMerchantDetail() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setSearchString("29025502");
        List<BleNewTech> bleNewTechList = bleNewTechRepository.getMerchantDetail(jqGridRequest);
        assertNotNull(bleNewTechList);
        assertThat(bleNewTechList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetServiceForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        jqGridRequest.setFirstIndex(1);
        jqGridRequest.setLastIndex(1);
        List<BleNewTech> bleNewTechList = bleNewTechRepository.getNewTechBySumTotal(jqGridRequest);
        assertNotNull(bleNewTechList);
        assertThat(bleNewTechList.size(), greaterThanOrEqualTo(0));
    }
}
