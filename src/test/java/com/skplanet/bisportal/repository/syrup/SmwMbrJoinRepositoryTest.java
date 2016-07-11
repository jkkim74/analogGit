package com.skplanet.bisportal.repository.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;
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

import static com.skplanet.bisportal.common.utils.DateUtil.getWkFrto;
import static org.junit.Assert.*;

public class SmwMbrJoinRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private SmwMbrJoinRepository smwMbrJoinRepository;

    @Autowired
    private SvcCdRepository svcCdRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMbrJoinPerDay() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        List<SmwMbrJoin> smwMbrJoinList = smwMbrJoinRepository.getMbrJoinPerDay(jqGridRequest);
        assertNotNull(smwMbrJoinList);
    }

    @Test
    public void testGetMbrJoinPerWeek() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141028");
        jqGridRequest.setEndDate("20141208");
        List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
        jqGridRequest = getWkFrto(jqGridRequest, smwCldrWkList);
        List<SmwMbrJoin> smwMbrJoinList = smwMbrJoinRepository.getMbrJoinPerWeek(jqGridRequest);
        assertNotNull(smwMbrJoinList);
    }

    @Test
    public void testGetMbrJoinPerMonth() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("201406");
        jqGridRequest.setEndDate("201412");
        List<SmwMbrJoin> smwMbrJoinList = smwMbrJoinRepository.getMbrJoinPerMonth(jqGridRequest);
        assertNotNull(smwMbrJoinList);
    }
}
