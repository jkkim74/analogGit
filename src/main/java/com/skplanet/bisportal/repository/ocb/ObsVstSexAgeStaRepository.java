package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsVstSexAgeSta;

/**
 * The ObsVstSexAgeStaRepository class.
 *
 * @author HoJin-Ha (mimul@wiseeco.com)
 *
 */
@Repository
public class ObsVstSexAgeStaRepository {
  @Resource(name = "ocbSqlSession")
  private SqlSessionTemplate sqlSession;

  /**
   * 날짜기준 방문자(성별, 연령별) - 일자별
   * 
   * @param jqGridRequest
   * @return Collection of ObsVstSexAgeSta
   */
  public List<ObsVstSexAgeSta> getVisitorsSexAgePerDay(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getVisitorsSexAgePerDay", jqGridRequest);
  }

  /**
   * 날짜기준 방문자(성별, 연령별) - 주별
   * 
   * @param jqGridRequest
   * @return Collection of ObsVstSexAgeSta
   */
  public List<ObsVstSexAgeSta> getVisitorsSexAgePerWeek(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getVisitorsSexAgePerWeek", jqGridRequest);
  }

  /**
   * 날짜기준 방문자(성별, 연령별) - 월별
   * 
   * @param jqGridRequest
   * @return Collection of ObsVstSexAgeSta
   */
  public List<ObsVstSexAgeSta> getVisitorsSexAgePerMonth(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getVisitorsSexAgePerMonth", jqGridRequest);
  }
}
