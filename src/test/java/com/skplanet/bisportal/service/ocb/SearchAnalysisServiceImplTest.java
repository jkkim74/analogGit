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
import com.skplanet.bisportal.model.ocb.ObsSrchClickAlliSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickBnftSta;
import com.skplanet.bisportal.model.ocb.ObsSrchClickStoreSta;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickAlliStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickBnftStaRepository;
import com.skplanet.bisportal.repository.ocb.ObsSrchClickStoreStaRepository;

/**
 * The SearchAnalysisServiceImplTest class.
 *
 * @author ophelisis
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchAnalysisServiceImplTest {
    @Mock
    private ObsSrchClickStoreStaRepository obsSrchClickStoreStaRepository;

    @Mock
    private ObsSrchClickAlliStaRepository obsSrchClickAlliStaRepository;

    @Mock
    private ObsSrchClickBnftStaRepository obsSrchClickBnftStaRepository;

    @InjectMocks
    private SearchAnalysisService searchAnalysisService = new SearchAnalysisServiceImpl();
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSearchResultClickStoreForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsSrchClickStoreSta> obsSrchClickStoreStaList = obsSrchClickStoreStaRepository.getSearchResultClickStoreForPivotPerDay(jqGridRequest);
        assertNotNull(obsSrchClickStoreStaList);
        assertThat(obsSrchClickStoreStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetSearchResultClickStoreForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsSrchClickStoreSta> obsSrchClickStoreStaList = obsSrchClickStoreStaRepository.getSearchResultClickStoreForGridPerDay(
                jqGridRequest);
        assertNotNull(obsSrchClickStoreStaList);
        assertThat(obsSrchClickStoreStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetSearchResultClickAlliForPivot() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsSrchClickAlliSta> obsSrchClickAlliStaList = obsSrchClickAlliStaRepository.getSearchResultClickAlliForPivotPerDay(
                jqGridRequest);
        assertNotNull(obsSrchClickAlliStaList);
        assertThat(obsSrchClickAlliStaList.size(), greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetSearchResultClickBnftForGrid() throws Exception {
        JqGridRequest jqGridRequest = new JqGridRequest();
        jqGridRequest.setStartDate("20140101");
        jqGridRequest.setEndDate("20141231");
        List<ObsSrchClickBnftSta> obsSrchClickBnftStaList = obsSrchClickBnftStaRepository.getSearchResultClickBnftPerDay(
                jqGridRequest);
        assertNotNull(obsSrchClickBnftStaList);
        assertThat(obsSrchClickBnftStaList.size(), greaterThanOrEqualTo(0));
    }
}
