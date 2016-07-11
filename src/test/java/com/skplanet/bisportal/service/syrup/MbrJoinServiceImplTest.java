package com.skplanet.bisportal.service.syrup;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.syrup.SmwMbrJoin;
import com.skplanet.bisportal.repository.syrup.SmwMbrJoinRepository;
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
public class MbrJoinServiceImplTest {
    @Mock
    private SmwMbrJoinRepository smwMbrJoinRepository;

    @InjectMocks
    private MbrJoinService mbrJoinService = new MbrJoinServiceImpl();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMbrJoinForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20141129");
        jqGridRequest.setEndDate("20141208");
        List<SmwMbrJoin> smwMbrJoinList = mbrJoinService.getMbrJoinForGrid(jqGridRequest);
        assertNotNull(smwMbrJoinList);
    }
}
