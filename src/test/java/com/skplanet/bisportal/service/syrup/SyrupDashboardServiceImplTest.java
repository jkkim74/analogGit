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

import com.skplanet.bisportal.common.model.OlapDimensionRequest;
import com.skplanet.bisportal.model.syrup.CustomerSituation;
import com.skplanet.bisportal.repository.syrup.SyrupDashboardRepository;

/**
 * The SyrupDashboardServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class SyrupDashboardServiceImplTest {
    @Mock
    private SyrupDashboardRepository syrupDashboardRepository;

    @InjectMocks
    private SyrupDashboardService syrupDashboardService = new SyrupDashboardServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetCustomerSituationForPivot() throws Exception {
        OlapDimensionRequest olapDimensionRequest = new OlapDimensionRequest();
        olapDimensionRequest.setStartDate("20140101");
        olapDimensionRequest.setEndDate("20141231");
        List<CustomerSituation> customerSituationList = syrupDashboardRepository.getCustomerSituationPerDay(olapDimensionRequest);
        assertNotNull(customerSituationList);
        assertThat(customerSituationList.size(), greaterThanOrEqualTo(0));
    }
}
