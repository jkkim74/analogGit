package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.model.bip.ComIntgComCd;
import com.skplanet.bisportal.model.bip.SvcComCd;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sjune on 2014-05-28.
 * 
 * @author sjune
 */
@Repository
public class ComIntgComCdRepository {
	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 날짜기준 고객별 액션 - 일자별
	 * 
	 * @param comIntgComCd
	 * @return Collection of ComIntgComCd
	 */
	public List<ComIntgComCd> getComIntgComCds(ComIntgComCd comIntgComCd) {
		return sqlSession.selectList("getComIntgComCds", comIntgComCd);
	}

	/**
	 * Oneid 검색박스 조회조건 목록 조회
	 *
	 * @param whereConditions
	 * @return list of SvcComCd
	 */
	public List<SvcComCd> getSvcComCds(List<SvcComCd> whereConditions) {
		return sqlSession.selectList("getSvcComCds", whereConditions);
	}
}
