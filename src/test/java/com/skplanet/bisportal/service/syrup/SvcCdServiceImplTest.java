package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.repository.syrup.SvcCdRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SvcCdServiceImplTest {
    @Mock
    private SvcCdRepository svcCdRepository;

    @InjectMocks
    private SvcCdService svcCdService = new SvcCdServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetWkStrd() throws Exception {
        SmwCldrWk smwCldrWk = new SmwCldrWk();
        smwCldrWk.setSearchDate("20141208");
        smwCldrWk.setStrdYear("2014");
        List<SmwCldrWk> smwCldrWkList = svcCdService.getWkStrd(smwCldrWk);
        assertNotNull(smwCldrWkList);
    }

    @Test
    public void testGetWkStrds() throws Exception {
        SmwCldrWk smwCldrWk = new SmwCldrWk();
        smwCldrWk.setStrdYear("2014");
        List<SmwCldrWk> smwCldrWkList = svcCdService.getWkStrds(smwCldrWk);
        assertNotNull(smwCldrWkList);
    }
}
