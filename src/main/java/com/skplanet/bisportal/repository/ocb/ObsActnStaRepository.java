package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsActnSta;

/**
 * The ObsActnStaRepository class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Repository
public class ObsActnStaRepository {
  @Resource(name = "ocbSqlSession")
  private SqlSessionTemplate sqlSession;

  /**
   * 날짜기준 고객별 액션 - 일자별
   * 
   * @param jqGridRequest
   * @return Collection of ObsActnSta
   */
  public List<ObsActnSta> getCustomersActionPerDay(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getCustomersActionPerDay", jqGridRequest);
  }

  /**
   * 날짜기준 고객별 액션 - 주별
   * 
   * @param jqGridRequest
   * @return Collection of ObsActnSta
   */
  public List<ObsActnSta> getCustomersActionPerWeek(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getCustomersActionPerWeek", jqGridRequest);
  }

  /**
   * 날짜기준 고객별 액션 - 월별
   * 
   * @param jqGridRequest
   * @return Collection of ObsActnSta
   */
  public List<ObsActnSta> getCustomersActionPerMonth(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getCustomersActionPerMonth", jqGridRequest);
  }
}
