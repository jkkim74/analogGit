package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwPushTickerStatDtl;
import com.skplanet.bisportal.repository.syrup.SmwPushTickerStatDtlRepository;
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
public class EtcServiceImplTest {
    @Mock
    private SmwPushTickerStatDtlRepository smwPushTickerStatDtlRepository;

    @InjectMocks
    private EtcService etcService = new EtcServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSmwPushTickerStatDtlGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        List<SmwPushTickerStatDtl> smwPushTickerStatDtlList = etcService.getSmwPushTickerStatDtlGrid(jqGridRequest);
        assertNotNull(smwPushTickerStatDtlList);
    }
}
