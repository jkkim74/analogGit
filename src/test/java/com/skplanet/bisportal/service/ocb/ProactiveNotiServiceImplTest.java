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
import com.skplanet.bisportal.model.ocb.ObsPushCnfgSta;
import com.skplanet.bisportal.repository.ocb.ObsPushCnfgStaRepository;

/**
 * The ProactiveNotiServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class ProactiveNotiServiceImplTest {
    @Mock
    private ObsPushCnfgStaRepository obsPushCnfgStaRepository;

    @InjectMocks
    private ProactiveNotiService proactiveNotiService = new ProactiveNotiServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetNotificationSetForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsPushCnfgSta> obsPushCnfgStaList = obsPushCnfgStaRepository.getNotificationSetForPivotPerDay(jqGridRequest);
        assertNotNull(obsPushCnfgStaList);
        assertThat(obsPushCnfgStaList.size(), greaterThanOrEqualTo(0));
    }
}
