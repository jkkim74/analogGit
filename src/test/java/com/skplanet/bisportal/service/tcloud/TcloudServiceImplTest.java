package com.skplanet.bisportal.service.tcloud;

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
import com.skplanet.bisportal.model.tcloud.TCloudMenuStat;
import com.skplanet.bisportal.repository.tcloud.CloudRepository;

/**
 * The TcloudServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class TcloudServiceImplTest {
    @Mock
    private CloudRepository cloudRepository;

    @InjectMocks
    private TcloudService tcloudService = new TcloudServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMenuStat() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<TCloudMenuStat> tCloudMenuStatList = cloudRepository.getTCloudMenuStatPerDay(jqGridRequest);
        assertNotNull(tCloudMenuStatList);
        assertThat(tCloudMenuStatList.size(), greaterThanOrEqualTo(0));
    }
}
