package com.skplanet.bisportal.service.ocb;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsDPushRespSta;
import com.skplanet.bisportal.repository.ocb.OcbMktPushSndRsltRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The OcbMktPushSndRsltServiceImplTest class.
 *
 * @author kyoungoh lee
 */
@RunWith(MockitoJUnitRunner.class)
public class OcbMktPushSndRsltServiceImplTest {
    @Mock
    private OcbMktPushSndRsltRepository ocbMktPushSndRsltRepository;

    @InjectMocks
    private OcbMktPushSndRsltService ocbMktPushSndRsltService = new OcbMktPushSndRsltServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMktPushSndRslt() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsDPushRespSta> ObsDPushRespStaList = ocbMktPushSndRsltService.getMktPushSndRslt(jqGridRequest);
        assertNotNull(ObsDPushRespStaList);
    }
}
