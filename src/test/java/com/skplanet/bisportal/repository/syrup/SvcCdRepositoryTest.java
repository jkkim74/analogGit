package com.skplanet.bisportal.repository.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

public class SvcCdRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private SvcCdRepository svcCdRepository;

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
        List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrd(smwCldrWk);
        assertNotNull(smwCldrWkList);
    }

    @Test
    public void testGetWkStrds() throws Exception {
        SmwCldrWk smwCldrWk = new SmwCldrWk();
        smwCldrWk.setStrdYear("2014");
        List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrds(smwCldrWk);
        assertNotNull(smwCldrWkList);
    }

    @Test
    public void testGetWkStrdFrto() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141121");
        jqGridRequest.setEndDate("20141208");
        List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
        assertNotNull(smwCldrWkList);
    }
}
