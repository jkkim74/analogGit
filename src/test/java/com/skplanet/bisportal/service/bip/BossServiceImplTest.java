package com.skplanet.bisportal.service.bip;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;
import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.common.model.ReportDateType;
import com.skplanet.bisportal.model.bip.HandInput;
import com.skplanet.bisportal.repository.bip.HandInputRepository;

@RunWith(MockitoJUnitRunner.class)
public class BossServiceImplTest {

  @Mock
  private HandInputRepository handInputRepository;

  @InjectMocks
  private AdminService mockBossService = new AdminServiceImpl();

  private JqGridRequest jqGridRequest;

  @Before
  public void init() {
    jqGridRequest = new JqGridRequest();
    jqGridRequest.setBasicDate("20140829");
    jqGridRequest.setDateType(ReportDateType.DAY);
  }

  @Test
  public void testGetTMapCttMappInfo() throws Exception {
    List<HandInput> handInputs = Lists.newArrayList();
    HandInput handInput = new HandInput();
    handInput.setDispDt("2014-08-01");
    handInput.setRsltVal1(new BigDecimal(1419482));
    handInput.setRsltVal3(new BigDecimal(17488674));
    handInputs.add(handInput);
    when(handInputRepository.getTMapCttMappInfoPerDay(jqGridRequest)).thenReturn(handInputs);
    assertThat(handInputs.get(0).getRsltVal1().toString(),
        is(mockBossService.getTMapCttMappInfo(jqGridRequest).get(0).getRsltVal1().toString()));
  }

  @Test
  public void testGetSyrupCttMappInfo() throws Exception {
    List<HandInput> handInputs = Lists.newArrayList();
    HandInput handInput = new HandInput();
    handInput.setDispDt("2014-08-01");
    handInput.setRsltVal1(new BigDecimal(8177));
    handInput.setRsltVal2(new BigDecimal(11885219));
    handInput.setRsltVal3(new BigDecimal(584329));
    handInputs.add(handInput);
    when(handInputRepository.getSyrupCttMappInfoPerDay(jqGridRequest)).thenReturn(handInputs);
    assertThat(handInputs.get(0).getRsltVal3().toString(),
        is(mockBossService.getSyrupCttMappInfo(jqGridRequest).get(0).getRsltVal3().toString()));
  }

  @Test
  public void testGetBatchJobCheck() throws Exception {
    when(handInputRepository.getBatchJobCheck(25)).thenReturn(0);
    int result = mockBossService.getBatchJobCheck(25);
    assertThat(result, is(0));
    verify(handInputRepository, times(1)).getBatchJobCheck(25);
    reset(handInputRepository);
  }

}
