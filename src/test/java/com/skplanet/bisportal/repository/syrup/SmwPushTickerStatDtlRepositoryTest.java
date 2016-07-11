package com.skplanet.bisportal.repository.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwCldrWk;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
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

public class SmwPushTickerStatDtlRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private SmwPushTickerStatDtlRepository smwPushTickerStatDtlRepository;

    @Autowired
    private SvcCdRepository svcCdRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSmwPushTickerStatDtlPerDay() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141121");
        jqGridRequest.setEndDate("20141208");
        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlPerDay(jqGridRequest);
        assertNotNull(smwPushTickerStatDtlList);
    }

    @Test
    public void testGetSmwPushTickerStatDtlWeek() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141028");
        jqGridRequest.setEndDate("20141208");
        List<SmwCldrWk> smwCldrWkList = svcCdRepository.getWkStrdFrto(jqGridRequest);
        jqGridRequest = getWkFrto(jqGridRequest, smwCldrWkList);
        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlWeek(jqGridRequest);
        assertNotNull(smwPushTickerStatDtlList);
    }

    @Test
    public void testGetSmwPushTickerStatDtlPerMonth() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("201406");
        jqGridRequest.setEndDate("201412");
        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = smwPushTickerStatDtlRepository.getSmwPushTickerStatDtlPerMonth(jqGridRequest);
        assertNotNull(smwPushTickerStatDtlList);
    }
}
