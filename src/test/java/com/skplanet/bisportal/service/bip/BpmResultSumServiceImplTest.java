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
import com.skplanet.bisportal.model.bip.BpmDlyPrst;
import com.skplanet.bisportal.model.bip.BpmMthStcPrst;
import com.skplanet.bisportal.model.bip.BpmWkStcPrst;
import com.skplanet.bisportal.repository.bip.BpmDlyPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmMthStcPrstRepository;
import com.skplanet.bisportal.repository.bip.BpmWkStcPrstRepository;

/**
 * The BpmResultSumServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class BpmResultSumServiceImplTest {
    @Mock
    private BpmDlyPrstRepository bpmDlyPrstRepository;
    @Mock
    private BpmWkStcPrstRepository bpmWkStcPrstRepository;
    @Mock
    private BpmMthStcPrstRepository bpmMthStcPrstRepository;

    @InjectMocks
    private BpmResultSumService bpmResultSumService = new BpmResultSumServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetBpmDailyResultSums() throws Exception {
        WhereCondition whereCondition = new WhereCondition();
        whereCondition.setSvcId(10l);
        whereCondition.setStartDate("20140101");
        whereCondition.setEndDate("20141231");
        List<BpmDlyPrst> bpmDlyPrstList = bpmDlyPrstRepository.getBpmDailyResultSums(whereCondition);
        assertNotNull(bpmDlyPrstList);
    }

    @Test
    public void testGetBpmWeeklyResultSums() throws Exception {
        WhereCondition whereCondition = new WhereCondition();
        whereCondition.setSvcId(10l);
        whereCondition.setStartWeekNumber("2014011");
        whereCondition.setEndWeekNumber("20141201");
        List<BpmWkStcPrst> bpmWkStcPrstList = bpmWkStcPrstRepository.getBpmWeeklyResultSums(whereCondition);
        assertNotNull(bpmWkStcPrstList);
    }

    @Test
    public void testGetBpmMonthlyResultSums() throws Exception {
        WhereCondition whereCondition = new WhereCondition();
        whereCondition.setSvcId(10l);
        whereCondition.setStartDate("20140101");
        whereCondition.setEndDate("20141231");
        List<BpmMthStcPrst> bpmMthStcPrstList = bpmMthStcPrstRepository.getBpmMonthlyResultSums(whereCondition);
        assertNotNull(bpmMthStcPrstList);
    }
}
