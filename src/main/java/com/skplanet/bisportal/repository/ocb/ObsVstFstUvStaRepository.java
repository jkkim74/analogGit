package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstFstUvSta;

/**
 * The ObsVstFstUvStaRepository class.
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 *
 */
@Repository
public class ObsVstFstUvStaRepository {
  @Resource(name = "ocbSqlSession")
  private SqlSessionTemplate sqlSession;
  /**
   * 날짜기준 월 최초방문자(고객타입별) - 일자별
   * 
   * @param jqGridRequest
   * @return Collection of ObsVstFstUvSta
   */
  public List<ObsVstFstUvSta> getVisitFstForPivotPerDay(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getVisitFstForPivotPerDay", jqGridRequest);
  }
}
