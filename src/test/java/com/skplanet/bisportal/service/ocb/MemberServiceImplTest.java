package com.skplanet.bisportal.service.ocb;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsMbrentSta;
import com.skplanet.bisportal.repository.ocb.ObsMbrentStaRepository;

/**
 * The MemberServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {
    @Mock
    private ObsMbrentStaRepository obsMbrentStaRepository;

    @InjectMocks
    private MemberService memberService = new MemberServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetEnterForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsMbrentSta> obsMbrentStaList = obsMbrentStaRepository.getEnterForGridPerDay(jqGridRequest);
        assertNotNull(obsMbrentStaList);
        assertThat(obsMbrentStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetEnterDetailForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsMbrentSta> obsMbrentStaList = obsMbrentStaRepository.getEnterDetailForGridPerDay(jqGridRequest);
        assertNotNull(obsMbrentStaList);
        assertThat(obsMbrentStaList.size(), greaterThanOrEqualTo(0));
    }
}
