package com.skplanet.bisportal.repository.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;
import com.skplanet.bisportal.testsupport.AbstractContextLoadingTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
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
 * The OcbMktPushSndRsltRepositoryTest class.
 *
 * @author kyoungoh lee
 */
public class OcbMktPushSndRsltRepositoryTest extends AbstractContextLoadingTest {
    @Autowired
    private OcbMktPushSndRsltRepository ocbMktPushSndRsltRepository;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMktPushSndRslt() throws Exception {
        JqGridRequest JqGridRequest = new JqGridRequest();
        JqGridRequest.setStartDate("20140101");
        JqGridRequest.setEndDate("20141231");
        List<ObsDPushRespSta> obsDPushRespStaList = ocbMktPushSndRsltRepository.getMktPushSndRslt(JqGridRequest);
        assertNotNull(obsDPushRespStaList);
//        for (ObsDPushRespSta obsDPushRespStas : obsDPushRespStaList) {
//            assertNotNull(obsDPushRespStas.getStdDt());
//            assertNotNull(obsDPushRespStas.getPushTyp());
//            assertNotNull(obsDPushRespStas.getSndMsg());
//            assertNotNull(obsDPushRespStas.getAppVer());
//            assertNotNull(obsDPushRespStas.getSndCustCnt());
//            assertNotNull(obsDPushRespStas.getRcvCustCnt());
//            assertNotNull(obsDPushRespStas.getLchCustCnt());
//            assertNotNull(obsDPushRespStas.getLchRate());
//            assertNotNull(obsDPushRespStas.getRcvRate());
//        }
    }
}
