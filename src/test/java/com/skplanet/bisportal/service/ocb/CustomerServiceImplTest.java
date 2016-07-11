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
import com.skplanet.bisportal.model.ocb.ObsActnSta;
import com.skplanet.bisportal.model.ocb.ObsSosBleSta;
import com.skplanet.bisportal.model.ocb.RtdDau;
import com.skplanet.bisportal.repository.ocb.ObsActnStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSosBleStaRepository;
import com.skplanet.bisportal.repository.ocb.RtdDauRepository;

/**
 * The CustomerServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    @Mock
    private ObsActnStaRepository obsActnStaRepository;

    @Mock
    private ObsSosBleStaRepository obsSosBleStaRepository;

    @Mock
    private RtdDauRepository rtdDauRepository;

    @InjectMocks
    private CustomerService customerService = new CustomerServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCustomersActionForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsActnSta> obsActnStaList = obsActnStaRepository.getCustomersActionPerMonth(jqGridRequest);
        assertNotNull(obsActnStaList);
        assertThat(obsActnStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetBleDiffData() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsSosBleSta> obsSosBleStaList = obsSosBleStaRepository.getBleDiffData(jqGridRequest);
        assertNotNull(obsSosBleStaList);
        assertThat(obsSosBleStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetOcbDauForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<RtdDau> rtdDauList = rtdDauRepository.getOcbDauForPivotPerDay(jqGridRequest);
        assertNotNull(rtdDauList);
        assertThat(rtdDauList.size(), greaterThanOrEqualTo(0));
    }
}
