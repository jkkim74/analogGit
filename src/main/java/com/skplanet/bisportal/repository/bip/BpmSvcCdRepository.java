package com.skplanet.bisportal.repository.bip;

import com.skplanet.bisportal.common.model.WhereCondition;
import com.skplanet.bisportal.model.bip.BpmSvcCd;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * The BpmSvcCdRepository class.
 *
 * @author sjune
 */
@Repository
public class BpmSvcCdRepository {

	@Resource(name = "bipSqlSession")
	private SqlSessionTemplate sqlSession;

	/**
	 * 서비스 코드 목록(조회대상 대분류)을 조회한다.
	 *
	 * @return BpmSvcCd
	 */
	public List<BpmSvcCd> getBpmSvcs() {
        // TODO BOSS와 달리 권한 없이 모든 목록을 조회한다. 추후에 권한이 생기면 join이 필요함.
		return sqlSession.selectList("getBpmSvcs");
	}

	/**
	 * 지표구분 그룹 코드(조회대상 중분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	public List<BpmSvcCd> getBpmCycleToGrps(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmCycleToGrps", whereCondition);
	}

	/**
	 * 지표구분 코드(조회대상 소분류) 목록을 조회한다.
	 *
	 * @param whereCondition
	 * @return BpmSvcCd
	 */
	public List<BpmSvcCd> getBpmGrpToCls(WhereCondition whereCondition) {
		return sqlSession.selectList("getBpmGrpToCls", whereCondition);
	}

	/**
	 * 주차 코드를 조회한다.
	 *
	 * @param wkStcStrdYmw
	 * @return BpmSvcCd
	 */
	public List<BpmSvcCd> getBpmWkStrds(String wkStcStrdYmw) {
		return sqlSession.selectList("getBpmWkStrds", wkStcStrdYmw);
	}
}
