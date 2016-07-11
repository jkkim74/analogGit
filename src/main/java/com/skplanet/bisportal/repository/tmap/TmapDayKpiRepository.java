package com.skplanet.bisportal.repository.tmap;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.tmap.TmapDayKpi;

/**
 * The TmapDayKpiRepository class.
 *
 * Created by ophelisis on 2015. 6. 11..
 *
 * @author Hwan-Lee (ophelisis@wiseeco.com)
 */
@Repository
public class TmapDayKpiRepository {
  @Resource(name = "tmapSqlSession")
  private SqlSessionTemplate sqlSession;

  /**
   * 일별 KPI - 차트
   * 
   * @param jqGridRequest
   * @return Collection of TmapDayKpi
   */
  public List<TmapDayKpi> getDayKpiPerDayForChart(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getDayKpiPerDayForChart", jqGridRequest);
  }

  /**
   * 일별 KPI - 시트
   *
   * @param jqGridRequest
   * @return Collection of TmapDayKpi
   */
  public List<TmapDayKpi> getDayKpiPerDayForGrid(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getDayKpiPerDayForGrid", jqGridRequest);
  }
}
