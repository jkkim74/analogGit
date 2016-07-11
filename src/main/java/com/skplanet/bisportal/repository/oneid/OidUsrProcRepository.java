package com.skplanet.bisportal.repository.oneid;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.skplanet.bisportal.model.oneid.FunnelRequest;
import com.skplanet.bisportal.model.oneid.OidUsrProc;
import com.skplanet.bisportal.model.oneid.OidUsrProcRank;

/**
 * Created by mimul on 2015. 6. 25..
 */
@Repository
public class OidUsrProcRepository {
	@Resource(name = "oneidSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * oneid funnel chart용 데이터 조회
	 *
	 * @param funnelRequest
	 * @return
	 */
	public List<OidUsrProc> getOidUsrProcs(FunnelRequest funnelRequest) {
		return sqlSession.selectList("getOidUsrProcs", funnelRequest);
	}

	/**
	 * oneid funnel step별 인입/이탈 url 목록 조회
	 *
	 * @param funnelRequest
	 * @return
	 */
	public List<OidUsrProcRank> getOidUsrProcRanks(FunnelRequest funnelRequest) {
		return sqlSession.selectList("getOidUsrProcRanks", funnelRequest);
	}

	/**
	 * 프로세스별로 단계 정보 조회
	 *
	 * @param procCd
	 * @return
	 */
	public List<String> getStepCds(String procCd) {
		return sqlSession.selectList("getStepCds", procCd);
	}
}
