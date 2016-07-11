package com.skplanet.bisportal.repository.ocb;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.common.model.JqGridRequest;
import com.skplanet.bisportal.model.ocb.ObsSrchClickBnftSta;

/**
 * The ObsSrchClickBnftStaRepository class.
 * 
 * @author HoJin-Ha (mimul@wiseeco.com)
 * 
 */
@Repository
public class ObsSrchClickBnftStaRepository {
  @Resource(name = "ocbSqlSession")
  private SqlSessionTemplate sqlSession;

  /**
   * 검색결과 클릭_혜택 - 일자별
   * 
   * @param jqGridRequest
   * @return Collection of ObsSrchClickBnftSta
   */
  public List<ObsSrchClickBnftSta> getSearchResultClickBnftPerDay(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getSearchResultClickBnftPerDay", jqGridRequest);
  }

  /**
   * 검색결과 클릭_혜택 - 주별
   * 
   * @param jqGridRequest
   * @return Collection of ObsSrchClickBnftSta
   */
  public List<ObsSrchClickBnftSta> getSearchResultClickBnftPerWeek(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getSearchResultClickBnftPerWeek", jqGridRequest);
  }

  /**
   * 검색결과 클릭_혜택 - 월별
   * 
   * @param jqGridRequest
   * @return Collection of ObsSrchClickBnftSta
   */
  public List<ObsSrchClickBnftSta> getSearchResultClickBnftPerMonth(JqGridRequest jqGridRequest) {
    return sqlSession.selectList("getSearchResultClickBnftPerMonth", jqGridRequest);
  }
}
