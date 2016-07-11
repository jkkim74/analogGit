package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstInflowUvSta;

/**
 * The ObsVstInflowUvStaRepository class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Repository
public class ObsVstInflowUvStaRepository {
  @Resource(name = "ocbSqlSession")
  private SqlSessionTemplate sqlSession;

  /**
   * 날짜기준 OCB DAU(유입경로별) - 일자별
   * 
   * @param jqGridRequest
   * @return Collection of ObsVstInflowUvSta
   */
  public List<ObsVstInflowUvSta> getVisitInflRtForPivotPerDay(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getVisitInflRtForPivotPerDay", jqGridRequest);
  }
}
