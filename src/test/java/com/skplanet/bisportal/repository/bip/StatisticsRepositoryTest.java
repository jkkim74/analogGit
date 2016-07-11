package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.bip.DayVisitor;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The StatisticsRepositoryTest class.
 *
 * @author kyoungoh lee
 */
public class StatisticsRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private StatisticsRepository statisticsRepository;

    @Test
    public void testGetDayVisitor() throws Exception {
        JqGridRequest JqGridRequest = new JqGridRequest();
        JqGridRequest.setStartDate("20140101");
        JqGridRequest.setEndDate("20141231");
        List<DayVisitor> dayVisitorList = statisticsRepository.getDayVisitor(JqGridRequest);
        assertNotNull(dayVisitorList);
    }
}
